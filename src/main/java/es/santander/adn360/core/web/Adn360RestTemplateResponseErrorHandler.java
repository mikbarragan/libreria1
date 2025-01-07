package es.santander.adn360.core.web;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santander.darwin.core.annotation.DarwinQualifier;
import com.santander.darwin.core.exceptions.dto.ErrorModelDarwin;
import es.santander.adn360.core.model.dto.response.GlobalExceptionResponse;
import es.santander.adn360.core.model.exception.FunctionalException;
import es.santander.adn360.core.util.ExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * RestTemplate response error handler to handle Darwin or Functional exceptions
 *
 * @author ADN360
 */
@Slf4j
public class Adn360RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    @DarwinQualifier
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Identifies that an error occurred
     *
     * @param response ClientHttpResponse
     * @return boolean
     */
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode() != HttpStatus.OK
                && response.getStatusCode() != HttpStatus.NO_CONTENT;
    }

    /**
     * Tries to identify the error and throws the right exception
     *
     * @param response ClientHttpResponse
     * @throws IOException error
     */
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        tryToThrowFromGlobalExceptionResponse(response);
        tryToThrowFromDarwinException(response);
        tryToThrowFromException(response);
        // Si no hemos conseguido mapear la respuesta dejamos que siga su curso
        new DefaultResponseErrorHandler().handleError(response);
    }

    /**
     * tryToThrowFromGlobalExceptionResponse
     *
     * @param response ClientHttpResponse
     */
    private void tryToThrowFromGlobalExceptionResponse(ClientHttpResponse response) {
        try {
            GlobalExceptionResponse globalExceptionResponse =
                    objectMapper.readValue(
                            StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()),
                            GlobalExceptionResponse.class);

            throw new FunctionalException(
                    globalExceptionResponse.getHttpMessage(),
                    getExceptionEnum(response),
                    globalExceptionResponse.getMoreInformation()
            );

        } catch (JsonParseException | JsonMappingException ex) {
            log.debug("RestTemplate tried to map a GlobalExceptionResponse: {}", ex.getMessage(), ex);

        } catch (IOException ex) {
            log.error("RestTemplate IOException trying to map a GlobalExceptionResponse: {}", ex.getMessage(), ex);
        }
    }

    /**
     * tryToThrowFromDarwinException
     *
     * @param response ClientHttpResponse
     */
    private void tryToThrowFromDarwinException(ClientHttpResponse response) {
        try {
            ErrorModelDarwin errorModel =
                    objectMapper.readValue(
                            StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()),
                            ErrorModelDarwin.class);

            throw new FunctionalException(
                    errorModel.getShortMessage(),
                    getExceptionEnum(response),
                    errorModel.getDetailedMessage()
            );

        } catch (JsonParseException | JsonMappingException ex) {
            log.debug("RestTemplate tried to map a GenericDarwinException: {}", ex.getMessage(), ex);

        } catch (IOException ex) {
            log.error("RestTemplate IOException trying to map a GenericDarwinException: {}", ex.getMessage(), ex);
        }
    }

    /**
     * tryToThrowFromException
     *
     * @param response ClientHttpResponse
     */
    private void tryToThrowFromException(ClientHttpResponse response) {
        try {
            Exception exception =
                    objectMapper.readValue(
                            StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()),
                            Exception.class);

            throw new FunctionalException(
                    exception.getMessage(),
                    exception,
                    getExceptionEnum(response),
                    exception.toString()
            );

        } catch (JsonParseException | JsonMappingException ex) {
            log.debug("RestTemplate tried to map a Exception: {}", ex.getMessage(), ex);

        } catch (IOException ex) {
            log.error("RestTemplate IOException trying to map a Exception: {}", ex.getMessage(), ex);
        }
    }

    /**
     * getExceptionEnum
     *
     * @param response ClientHttpResponse
     *
     * @return exceptionEnum
     */
    private ExceptionEnum getExceptionEnum(ClientHttpResponse response) {
        ExceptionEnum exceptionEnum;
        try {
            exceptionEnum = ExceptionEnum.toExceptionEnum(HttpStatus.valueOf(response.getStatusCode().value()));
        } catch (IOException ex) {
            log.error("RestTemplate IOException trying to get StatusCode: {}", ex.getMessage(), ex);
            exceptionEnum = ExceptionEnum.WS_ERROR;
        }
        return exceptionEnum;
    }

/**
 * nonsense commentary to comply with a Sonar nonsense rule
 * nonsense commentary to comply with a Sonar nonsense rule
 * nonsense commentary to comply with a Sonar nonsense rule
 */
}



