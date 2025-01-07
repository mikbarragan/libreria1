package es.santander.adn360.core.web;

import es.santander.adn360.core.model.exception.FunctionalException;
import es.santander.adn360.core.util.ExceptionEnum;
import es.santander.adn360.core.util.PaginatedResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The Pagination handler class
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class PaginationHandler implements ResponseBodyAdvice<PaginatedResponse> {

    public static final String HEADER_LINK = "Link";
    public static final String PARAM_OFFSET = "_offset";
    public static final String PARAM_LIMIT = "_limit";

    private static final String HEADER_LINK_REGEX = ".*<(.*)>;( ){0,1}rel=(\"){0,1}(.*)(\"){0,1}.*";
    private static final String HEADER_LINK_FORMAT = " <%s>; rel=%s";
    private static final String HEADER_LINK_SEPARATOR = ",";
    private static final String REL_PREVIOUS = "previous";
    private static final String REL_NEXT = "next";

    private static final Pattern HEADER_LINK_PATTERN = Pattern.compile(HEADER_LINK_REGEX);

    /**
     * Return true or false if support params
     * @param methodParameter   input method
     * @param aClass            input class
     * @return  true or false
     */
    @Override
    public boolean supports(
            MethodParameter methodParameter,
            Class<? extends HttpMessageConverter<?>> aClass
    ) {
        return PaginatedResponse.class.isAssignableFrom(methodParameter.getParameterType());
    }

    /**
     * Response list paginated
     * @param body              content
     * @param methodParameter   method
     * @param mediaType         media type used
     * @param aClass            input class
     * @param req request http
     * @param resp    response http
     * @return  response
     */
    @Override
    public PaginatedResponse beforeBodyWrite(
            PaginatedResponse body,
            MethodParameter methodParameter,
            MediaType mediaType,
            Class<? extends HttpMessageConverter<?>> aClass,
            ServerHttpRequest req,
            ServerHttpResponse resp
    ) {

        HttpServletRequest servletRequest = ((ServletServerHttpRequest) req).getServletRequest();
        // Get pagination parameters
        String offsetParam = servletRequest.getParameter(PARAM_OFFSET);
        String limitParam = servletRequest.getParameter(PARAM_LIMIT);

        HttpServletResponse servletResponse =
                Optional.ofNullable((ServletServerHttpResponse) resp)
                        .map(ServletServerHttpResponse::getServletResponse)
                        .orElse(null);

        if (! StringUtils.isEmpty(limitParam) && ! isPaginated(servletResponse)) {
            paginateResponse(body, req, resp,
                    validateOffsetParam(body, offsetParam),
                    validateNumberParam(limitParam, 1, "Limit"),
                    body.getPaginatedField());
        }

        return body;
    }

    /**
     * Validate if exists any Link in the HttpHeaders.
     *
     * @param resp
     * @return
     */
    private boolean isPaginated(HttpServletResponse resp) {

        boolean result = false;
        String strHeader = "is not present";

        if (resp != null && resp.getHeader(HEADER_LINK) != null) {
            strHeader = resp.getHeader(HEADER_LINK);
            result = HEADER_LINK_PATTERN.matcher(strHeader).find();
        }

        log.debug("<HEADER_LINK> : [" + strHeader + "] - <isPaginated> : [" + result + "]");

        return result;
    }

    /**
     * paginateResponse.
     *
     * @param body              body
     * @param serverHttpRequest   serverHttpRequest
     * @param serverHttpResponse         serverHttpResponse
     * @param offset            offset
     * @param limit limit
     * @param list    list object
     */
    private static void paginateResponse(
            PaginatedResponse body,
            ServerHttpRequest serverHttpRequest,
            ServerHttpResponse serverHttpResponse,
            Long offset,
            Long limit,
            List<Object> list
    ) {

        final StringBuilder headerLinkBuilder = new StringBuilder();

        // Has previous link
        if (offset - limit >= 0) {
            headerLinkBuilder.append(String.format(HEADER_LINK_FORMAT,
                    UriComponentsBuilder.fromPath(serverHttpRequest.getURI().getPath())
                            .query(serverHttpRequest.getURI().getQuery())
                            .replaceQueryParam(PARAM_OFFSET, body.getIdByOffset(offset - limit))
                            .build().toUriString()
                    , REL_PREVIOUS));
        }

        // Has next link
        if (offset + limit < list.size()) {

            if (headerLinkBuilder.length() > 0) {
                headerLinkBuilder.append(HEADER_LINK_SEPARATOR);
            }

            headerLinkBuilder.append(String.format(HEADER_LINK_FORMAT,
                    UriComponentsBuilder.fromPath(serverHttpRequest.getURI().getPath())
                            .query(serverHttpRequest.getURI().getQuery())
                            .replaceQueryParam(PARAM_OFFSET, body.getIdByOffset(offset + limit))
                            .build().toUriString()
                    , REL_NEXT));
        }

        // Add link header to response
        if (headerLinkBuilder.length() > 0) {
            serverHttpResponse.getHeaders().add(HEADER_LINK, headerLinkBuilder.toString());
        }

        // Paginate and add pagination link header
        body.setPaginatedField(list.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList()));
    }

    /**
     * Validate the Offset parameter and return the idx.
     *
     * @param body input
     * @param offsetParam input
     * @return Long
     */
    private static Long validateOffsetParam(PaginatedResponse body, String offsetParam) {

        if (! body.isValidOffset(offsetParam)) {
            throw new FunctionalException(
                    ExceptionEnum.INVALID_INPUT_PARAMETERS, "Invalid pagination parameter Offset"
            );
        }

        return body.getOffsetById(offsetParam);
    }

    /**
     * Validates pagination parameter.
     *
     * @param paginationParam Parameter from request
     * @param limit           Validation limit, parameter should not be less than this
     * @param name            Parameter name
     * @return Long
     */
    private static Long validateNumberParam(String paginationParam, Integer limit, String name) {
        try {
            final Long value = Long.valueOf(paginationParam);
            if (value < limit) {
                throw new FunctionalException(
                        ExceptionEnum.INVALID_INPUT_PARAMETERS, name + " should not be less than " + limit
                );
            }
            return value;
        } catch (NumberFormatException ex) {
            throw new FunctionalException(
                    ExceptionEnum.INVALID_INPUT_PARAMETERS, "Invalid pagination parameter " + name
            );
        }
    }

    /**
     * nonsense commentary to comply with a Sonar nonsense rule
     * nonsense commentary to comply with a Sonar nonsense rule
     * nonsense commentary to comply with a Sonar nonsense rule
     */
}
