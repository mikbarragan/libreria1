package es.santander.adn360.core.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;


/**
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureObservability
class GlobalExceptionHandlerTest {

    private final String TEST_STR_REGEX_OK = "F087293443";
    private final String TEST_STR_REGEX_KO = "3443";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void exceptionHandlerTest() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/test/commonException");

        ResponseEntity<String> out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, String.class);

        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void missingParameterHandlerTest() {
        UriComponentsBuilder builder;
        ResponseEntity<String> out;

        builder = UriComponentsBuilder.fromPath("/test/missingParametersException");
        out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, String.class);

        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        builder = UriComponentsBuilder.fromPath("/test/missingParametersException")
                .queryParam("strTest", TEST_STR_REGEX_OK);
        out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, String.class);

        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(out.getBody()).isEqualTo(TEST_STR_REGEX_OK);
    }

    @Test
    void invalidParameterHandlerTest() {
        UriComponentsBuilder builder;
        ResponseEntity<String> out;

        builder = UriComponentsBuilder.fromPath("/test/constraintViolationException")
                .queryParam("strTest", TEST_STR_REGEX_KO);
        out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, String.class);

        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        builder = UriComponentsBuilder.fromPath("/test/constraintViolationException")
                .queryParam("strTest", TEST_STR_REGEX_OK);
        out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, String.class);

        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(out.getBody()).isEqualTo(TEST_STR_REGEX_OK);
    }

    @Test
    void invalidBindingException() {
        UriComponentsBuilder builder;
        ResponseEntity<String> out;

        builder = UriComponentsBuilder.fromPath("/test/invalidBindException");
        out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, String.class);

        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        builder = UriComponentsBuilder.fromPath("/test/invalidBindException")
                .queryParam("strTest", TEST_STR_REGEX_OK);
        out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, String.class);

        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(out.getBody()).isEqualTo(TEST_STR_REGEX_OK);
    }

    @Test
    void functionalExceptionHandlerTest() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/test/functionalNoDataFoundException");

        ResponseEntity<String> out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, String.class);

        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void validationCoordinateParams() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/test/coordinates")
                .queryParam("application", "ADN360")
                .queryParam("segment", "EMPR")
                .queryParam("customer_id", "J087293443");

        ResponseEntity<String> out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, String.class);
        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void not_present_CoordinateParams_returns_OK_Request() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/test/coordinates");

        ResponseEntity<String> out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, String.class);
        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void not_present_segment_returns_OK_Request() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/test/coordinates")
                .queryParam("application", "ADN360");

        ResponseEntity<String> out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, String.class);
        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
