package es.santander.adn360.core.web;

import com.santander.darwin.core.exceptions.ErrorModelFactory;
import com.santander.darwin.core.exceptions.web.DarwinExceptionHandlerController;
import es.santander.adn360.core.model.dto.response.GlobalExceptionResponse;
import es.santander.adn360.core.model.exception.FunctionalException;
import es.santander.adn360.core.util.ExceptionEnum;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Global controller advice to handle exceptions globally.
 *
 * @author Javier Moreno
 */
@Slf4j
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Component
@ResponseBody
public class GlobalExceptionHandler<T, S> extends DarwinExceptionHandlerController<T, S> {


    public GlobalExceptionHandler(ErrorModelFactory<T, S> errorModelFactory, Supplier<S> errorAttributesContext) {
        super(errorModelFactory, errorAttributesContext);
    }

    /**
     * Handle missing parameters exceptions
     *
     * @param ex Exception
     * @return GlobalExceptionResponse
     */
    @ExceptionHandler({
            IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GlobalExceptionResponse missingParameterHandler(Exception ex) {

        log.error("Missing parameters: {}", ex.getMessage());

        return new GlobalExceptionResponse(ExceptionEnum.INVALID_INPUT_PARAMETERS.getHttpStatus().value(),
                ExceptionEnum.INVALID_INPUT_PARAMETERS.getMsg(), ex.getMessage());
    }

    /**
     * Handle constraint violation exceptions
     *
     * @param ex ConstraintViolationException
     * @return GlobalExceptionResponse
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<T> invalidParameterHandler(ConstraintViolationException ex) {
        String moreInfo = getMoreInfo(ex.getConstraintViolations());

        log.error("Constraint violation: {}", moreInfo);

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        Map<String, Object> errorModelAttributes = createErrorModelAttributes("constrain-validation-exception", 0,
                httpStatus, ex.getMessage(), moreInfo, null);

        T errorModel = errorModelFactory.getErrorModel(errorModelAttributes, errorAttributesContext.get());

        return ResponseEntity.status(httpStatus).body(errorModel);
    }

    /**
     * Handle functional exceptions
     *
     * @param response HttpServletResponse
     * @param ex       FunctionalException
     * @return GlobalExceptionResponse
     */
    @ExceptionHandler(FunctionalException.class)
    public ResponseEntity<T> functionalExceptionHandler(HttpServletResponse response, FunctionalException ex) {

        log.error("Received controlled exception in the Darwin framework ", ex);

        HttpStatus httpStatus = ex.getInfo().getHttpStatus();

        Map<String, Object> errorModelAttributes = createErrorModelAttributes(ex.getInfo().toString(), 0,
                httpStatus, ex.getMessage(), ex.getMoreInformation(), null);

        T errorModel = errorModelFactory.getErrorModel(errorModelAttributes, errorAttributesContext.get());

        return ResponseEntity.status(httpStatus).body(errorModel);

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

    private static Map<String, Object> createErrorModelAttributes(String errorName, int internalCode, HttpStatus status,
                                                                  String shortMessage, String detailedMessage, Map mapExtendedMessage) {
        Map<String, Object> errorModelAttributes = new HashMap<>();
        errorModelAttributes.put("errorName", errorName);
        errorModelAttributes.put("internalCode", internalCode);
        errorModelAttributes.put("shortMessage", shortMessage);
        errorModelAttributes.put("detailedMessage", detailedMessage);
        errorModelAttributes.put("mapExtendedMessage", mapExtendedMessage);
        errorModelAttributes.put("status", status.value());

        return errorModelAttributes;
    }
}
