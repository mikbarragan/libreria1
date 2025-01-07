package es.santander.adn360.core.web;

import com.santander.darwin.core.exceptions.ErrorModelFactory;
import com.santander.darwin.core.exceptions.reactive.DarwinErrorAttributes;
import es.santander.adn360.core.model.dto.response.GlobalExceptionResponse;
import es.santander.adn360.core.model.exception.FunctionalException;
import es.santander.adn360.core.util.ExceptionEnum;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Global controller advice to handle exceptions globally.
 *
 * @author ADN360
 */
@Slf4j
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Component
public class GlobalExceptionReactiveHandler<T, S> extends DarwinErrorAttributes<T, S> {


    public GlobalExceptionReactiveHandler(ErrorModelFactory<T, S> errorModelFactory, Function<ServerRequest, S> errorAttributesContext) {
        super(errorModelFactory, errorAttributesContext);
    }

    @Override
    protected Map<String, Object> fromManagedExceptions(Throwable error, ServerRequest request) {

        if (error instanceof MissingServletRequestParameterException
                || error instanceof MethodArgumentNotValidException
                || error instanceof IllegalArgumentException) {

            log.error("Missing parameters: {}", error.getMessage());

            Map<String, Object> managedErrorAttributes = objectMapper.convertValue(new GlobalExceptionResponse(ExceptionEnum.INVALID_INPUT_PARAMETERS.getHttpStatus().value(),
                    ExceptionEnum.INVALID_INPUT_PARAMETERS.getMsg(), error.getMessage()), Map.class);

            // And put one extra field to get response status
            managedErrorAttributes.put(RESPONSE_STATUS_KEY, HttpStatus.BAD_REQUEST.value());
            return managedErrorAttributes;

        } else if (error instanceof ConstraintViolationException) {

            log.error("Constraint violation: {}", getMoreInfo(((ConstraintViolationException) error).getConstraintViolations()));

            Map<String, Object> managedErrorAttributes = objectMapper.convertValue(new GlobalExceptionResponse(ExceptionEnum.INVALID_INPUT_PARAMETERS.getHttpStatus().value(),
                    ExceptionEnum.INVALID_INPUT_PARAMETERS.getMsg(), error.getMessage()), Map.class);

            // And put one extra field to get response status
            managedErrorAttributes.put(RESPONSE_STATUS_KEY, HttpStatus.BAD_REQUEST.value());
            return managedErrorAttributes;
        } else if (error instanceof BindException) {

            log.error("Invalid  parameters: {}", error.getMessage());

            Map<String, Object> managedErrorAttributes = objectMapper.convertValue(new GlobalExceptionResponse(ExceptionEnum.INVALID_INPUT_PARAMETERS.getHttpStatus().value(),
                    ExceptionEnum.INVALID_INPUT_PARAMETERS.getMsg(), error.getMessage()), Map.class);

            // And put one extra field to get response status
            managedErrorAttributes.put(RESPONSE_STATUS_KEY, HttpStatus.BAD_REQUEST.value());
            return managedErrorAttributes;
        } else if (error instanceof FunctionalException) {
            log.error("Functional Exception: {}", ((FunctionalException) error).getInfo().getMsg());

            Map<String, Object> managedErrorAttributes = objectMapper.convertValue(new GlobalExceptionResponse(
                    ((FunctionalException) error).getInfo().getHttpStatus().value(),
                    error.getMessage() == null ? ((FunctionalException) error).getInfo().getMsg() : error.getMessage(),
                    ((FunctionalException) error).getMoreInformation()), Map.class);

            // And put one extra field to get response status
            managedErrorAttributes.put(RESPONSE_STATUS_KEY, ((FunctionalException) error).getInfo().getHttpStatus().value());
            return managedErrorAttributes;
        }
        return super.fromManagedExceptions(error, request);
    }

    /**
     * Transform constraint list to a message
     *
     * @param constraintViolations ConstraintViolation set
     * @return String
     */
    private static String getMoreInfo(Set<ConstraintViolation<?>> constraintViolations) {
        StringBuilder resp = new StringBuilder();

        if (constraintViolations != null && !constraintViolations.isEmpty()) {
            for (ConstraintViolation<?> constraint : constraintViolations) {
                resp.append("[Field: <");
                resp.append(constraint.getPropertyPath());
                resp.append("> Value: <");
                resp.append(constraint.getInvalidValue());
                resp.append("> Error: <");
                resp.append(constraint.getMessage());
                resp.append(">];");
            }
        }

        return String.valueOf(resp);
    }
}
