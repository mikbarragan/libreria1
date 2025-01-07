package es.santander.adn360.core.util.swagger;

import es.santander.adn360.core.model.dto.response.GlobalExceptionResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static es.santander.adn360.core.util.swagger.SwaggerSampleConstants.FORBIDDEN;
import static es.santander.adn360.core.util.swagger.SwaggerSampleConstants.INTERNAL_ERROR;
import static es.santander.adn360.core.util.swagger.SwaggerSampleConstants.INVALID_PARAMETERS;
import static es.santander.adn360.core.util.swagger.SwaggerSampleConstants.UNAUTHORIZED;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "400",
                description = "Invalid parameters",

                content = @Content(
                        schema = @Schema(implementation = GlobalExceptionResponse.class),
                        examples = @ExampleObject(description = "Invalid parameters", value = INVALID_PARAMETERS)
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized. Not Authenticated",
                content = @Content(
                        schema = @Schema(implementation = GlobalExceptionResponse.class),
                        examples = @ExampleObject(description = "Internal Error", value = UNAUTHORIZED)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Call not authorized",
                content = @Content(
                        schema = @Schema(implementation = GlobalExceptionResponse.class),
                        examples = @ExampleObject(description = "Internal Error", value = FORBIDDEN)
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Server error",
                content = @Content(
                        schema = @Schema(implementation = GlobalExceptionResponse.class),
                        examples = @ExampleObject(description = "Internal Error", value = INTERNAL_ERROR)
                )
        )
})
/**
 * Annotation to display error responses on Swagger
 * Annotation to display error responses on Swagger
 */
public @interface Adn360ErrorResponses {}
