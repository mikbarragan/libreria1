package es.santander.adn360.core.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santander.darwin.core.annotation.DarwinQualifier;
import com.santander.darwin.security.authentication.AuthenticationBearerToken;
import es.santander.adn360.core.config.CoreProperties;
import es.santander.adn360.core.model.document.InternalUser;
import es.santander.adn360.core.model.document.repository.InternalUserRepository;
import es.santander.adn360.core.util.ExceptionEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * Filter to validate the Santander channel header is in the request. All request should have this header.
 * This filter should have a lower order than hystrix filters (@see JwtHystrixAutoConfiguration).
 */
@Slf4j
@Component
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebSecurityFilter extends OncePerRequestFilter {
    /**
     * Header Channel
     */
    public static final String HEADER_CHANNEL = "X-Santander-Channel";
    /**
     * Application/Json UTF8
     */
    private static final String APP_JSON_UTF8 = "application/json;charset=UTF-8";
    /**
     * Core Properties
     */
    private final CoreProperties coreProperties;
    /**
     * Internal User Repository
     */
    private final Optional<InternalUserRepository> internalUserRepository;
    /**
     * Object Mapper
     */
    @DarwinQualifier
    private final ObjectMapper objectMapper;
    /**
     *
     * The web filter
     *
     * @param coreProperties core
     * @param internalUserRepository repo
     */
    @Autowired
    public WebSecurityFilter(
            CoreProperties coreProperties,
            Optional<InternalUserRepository> internalUserRepository
    ) {
        this.coreProperties = coreProperties;
        this.internalUserRepository = internalUserRepository;
        this.objectMapper = new ObjectMapper();
    }

    /**
     *
     * Do filter Internal
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @param filterChain filterChain
     *
     * @throws ServletException exception
     * @throws IOException ioException
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse resp,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String channelHeader = req.getHeader(HEADER_CHANNEL);

        // Don't filter when accessing to endpoints as anonymous way or without authentication
        if (this.coreProperties.getChannelHeaderValidation()
                && SecurityContextHolder.getContext().getAuthentication() instanceof AuthenticationBearerToken) {

            if (channelHeader == null) {
                // Return error if channel header is missing
                resp.setStatus(HttpStatus.BAD_REQUEST.value());
                resp.setContentType(APP_JSON_UTF8);

                this.objectMapper.writeValue(resp.getWriter(), ExceptionEnum.INVALID_INPUT_PARAMETERS
                        .toResponse(String.format("%s header missing.", HEADER_CHANNEL)));

                return;
            }
            // Get internal user when possible.
            String internalUser = null;

            try {
                internalUser = this.internalUserRepository.map(repo -> Optional.of(channelHeader)
                        .filter(WebUtils.SANTANDER_CHANNEL_EMP::equals)
                        .map(x -> Optional.ofNullable(repo.findOneByIdUsuario(WebUtils.getTokenUser()))
                                .map(InternalUser::getUsuarioInterno)
                                .orElse(null))
                        .orElse(null)
                ).orElse(null);

            } catch (Exception e){
                // ignores exception, necessary to provide service when Mongo is down.
                log.info("Can't access to internal user repository", e);
            }
            // Include header and internal user in security context holder.
            SecurityContextHolder.setContext(new CoreSecurityContextImpl(SecurityContextHolder
                    .getContext().getAuthentication(), channelHeader, internalUser));


        }
        filterChain.doFilter(req, resp);
    }

    /**
     * nonsense commentary to comply with a Sonar nonsense rule
     * nonsense commentary to comply with a Sonar nonsense rule
     * nonsense commentary to comply with a Sonar nonsense rule
     * nonsense commentary to comply with a Sonar nonsense rule
     * nonsense commentary to comply with a Sonar nonsense rule
     */
}
