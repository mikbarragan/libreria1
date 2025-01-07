package es.santander.adn360.core.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Generic response for all exceptions
 *
 * @author ADN360
 */
@Data
@Builder
@RequiredArgsConstructor
public class GlobalExceptionResponse {

    /**
     * Http status code
     */
    @Schema(description = "Http status code")
    private final int httpCode;

    /**
     * "Http status message
     */
    @Schema(description = "Http status message")
    private final String httpMessage;

    /**
     * More error information
     */
    @Schema(description = "More error information")
    private final String moreInformation;

    /**
     * Global response
     *
     * @param httpCode    integer with code on response
     * @param httpMessage message response
     */
    public GlobalExceptionResponse(int httpCode, String httpMessage) {
        super();
        this.httpCode = httpCode;
        this.httpMessage = httpMessage;
        this.moreInformation = null;
    }
}
