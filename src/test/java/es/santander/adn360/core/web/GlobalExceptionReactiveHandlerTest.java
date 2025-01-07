package es.santander.adn360.core.web;

import es.santander.adn360.core.model.exception.FunctionalException;
import es.santander.adn360.core.util.ExceptionEnum;
import jakarta.validation.ConstraintViolationException;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * GlobalExceptionReactiveHandlerTest unit tests
 */
class GlobalExceptionReactiveHandlerTest {


    GlobalExceptionReactiveHandler globalExceptionReactiveHandler;

    @BeforeEach
    void setUp() {

        globalExceptionReactiveHandler = new GlobalExceptionReactiveHandler(null, null);
    }

    @Test
    void missingServletRequestParameterExceptionHandlerTest() {
        Map<String, Object> response = globalExceptionReactiveHandler
                .fromManagedExceptions(new MissingServletRequestParameterException("param1", ""), null);

        assertThat(response).isNotEmpty();
        assertThat(response.get("httpCode")).isEqualTo(400);

    }

    @Test
    void illegalArgumentExceptionTest() {
        Map<String, Object> response = globalExceptionReactiveHandler
                .fromManagedExceptions(new IllegalArgumentException(null, null), null);

        assertThat(response).isNotEmpty();
        assertThat(response.get("httpCode")).isEqualTo(400);

    }

    @Test
    void constraintViolationExceptionTest() {
        Map<String, Object> response = globalExceptionReactiveHandler
                .fromManagedExceptions(new ConstraintViolationException(null, null), null);

        assertThat(response).isNotEmpty();
        assertThat(response.get("httpCode")).isEqualTo(400);

    }

    @Test
    void bindExceptionTest() {
        Map<String, Object> response = globalExceptionReactiveHandler
                .fromManagedExceptions(new BindException(new TestPojo(), "attr"), null);

        assertThat(response).isNotEmpty();
        assertThat(response.get("httpCode")).isEqualTo(400);
    }

    @Test
    void functionalExceptionTest() {
        Map<String, Object> response = globalExceptionReactiveHandler
                .fromManagedExceptions(new FunctionalException(ExceptionEnum.NO_AUTH_INFO), null);

        assertThat(response).isNotEmpty();
        assertThat(response.get("httpCode")).isEqualTo(ExceptionEnum.NO_AUTH_INFO.getHttpStatus().value());

    }

    @Data
    private class TestPojo {
        String attr = "test attr";
    }



}
