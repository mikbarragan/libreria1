package es.santander.adn360.core.util;

import es.santander.adn360.core.model.dto.response.GlobalExceptionResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Enumeration to define the info content of the functional exceptions
 *
 * @author Javier Moreno
 */
public enum ExceptionEnum {

    /**
     * Incalid input
     */
    INVALID_INPUT_PARAMETERS(HttpStatus.BAD_REQUEST, "invalid.input.parameters"),
    /**
     * Not found
     */
    NOT_FOUND(HttpStatus.NOT_FOUND, "not.found"),
    /**
     * No auth access
     */
    NO_AUTH_ACCESS(HttpStatus.UNAUTHORIZED, "unauthorized"),
    /**
     * Forbidden
     */
    FORBIDDEN(HttpStatus.FORBIDDEN, "forbidden"),
    /**
     * No auth info
     */
    NO_AUTH_INFO(HttpStatus.NON_AUTHORITATIVE_INFORMATION, "unauthorized"),
    /**
     * No data found
     */
    NO_DATA_FOUND(HttpStatus.NO_CONTENT, "no.data.found"),
    /**
     * Confidentiality error
     */
    CONFIDENTIALITY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "confidentiality.error"),
    /**
     * Feign client error
     */
    FEIGN_CLIENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "feign.client.error"),
    /**
     * Internal server error
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal.error"),
    /**
     * Ws error
     */
    WS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "webservice.error"),
    /**
     * Contract situation error
     */
    CONTRACT_SITUATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "F00006");


	/**
	 * Http status
	 */
    @Schema(description = "Http status")
    @Getter
    private HttpStatus httpStatus;

	/**
	 * Error message
	 */
    @Schema(description = "Error message")
    @Getter
    private String msg;

    /**
     * @param httpStatus http status code
     * @param msg the message
     */
    ExceptionEnum(final HttpStatus httpStatus, final String msg) {
        this.httpStatus = httpStatus;
        this.msg = msg;
    }

    /**
     * Gets enum value from httpStatus.
     *
     * @param httpStatus to map.
     * @return ExceptionEnum
     */
    public static ExceptionEnum toExceptionEnum(HttpStatus httpStatus) {

        ExceptionEnum enumeration;

        switch (httpStatus) {
            case BAD_REQUEST:
                enumeration = ExceptionEnum.INVALID_INPUT_PARAMETERS;
                break;
            case NOT_FOUND:
                enumeration = ExceptionEnum.NOT_FOUND;
                break;
            case NO_CONTENT:
                enumeration = ExceptionEnum.NO_DATA_FOUND;
                break;
            case UNAUTHORIZED:
                enumeration = ExceptionEnum.NO_AUTH_ACCESS;
                break;
            case FORBIDDEN:
                enumeration = ExceptionEnum.FORBIDDEN;
                break;
            case NON_AUTHORITATIVE_INFORMATION:
                enumeration = ExceptionEnum.NO_AUTH_INFO;
                break;
            default:
                enumeration = ExceptionEnum.INTERNAL_SERVER_ERROR;
        }

        return enumeration;
    }

    /**
     * Gets GlobalExceptionResponse from enum value.
     *
     * @param moreInformation More information response parameter.
     * @return GlobalExceptionResponse
     */
    public GlobalExceptionResponse toResponse(String moreInformation) {
        return GlobalExceptionResponse.builder()
                .httpCode(this.httpStatus.value())
                .httpMessage(this.msg)
                .moreInformation(moreInformation)
                .build();
    }
}
