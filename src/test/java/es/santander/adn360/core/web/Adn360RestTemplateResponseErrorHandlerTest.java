package es.santander.adn360.core.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.santander.adn360.core.model.dto.response.GlobalExceptionResponse;
import es.santander.adn360.core.model.exception.FunctionalException;
import es.santander.adn360.core.util.ExceptionEnum;
import com.santander.darwin.core.annotation.DarwinQualifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.DefaultResponseCreator;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

/**
 * @author ADN360
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureObservability
class Adn360RestTemplateResponseErrorHandlerTest {

    MockRestServiceServer mockServer;
    @DarwinQualifier
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    @DarwinQualifier
    RestTemplate restTemplate;

    DefaultResponseCreator successResponse = MockRestResponseCreators.withSuccess();
    DefaultResponseCreator badRequestResponse = MockRestResponseCreators.withStatus(HttpStatus.BAD_REQUEST);
    DefaultResponseCreator notFoundResponse = MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND);
    DefaultResponseCreator internalServerErrorResponse = MockRestResponseCreators.withStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    DefaultResponseCreator noContentResponse = MockRestResponseCreators.withStatus(HttpStatus.NO_CONTENT);
    DefaultResponseCreator unauthorizedResponse = MockRestResponseCreators.withStatus(HttpStatus.UNAUTHORIZED);
    DefaultResponseCreator forbiddenResponse = MockRestResponseCreators.withStatus(HttpStatus.FORBIDDEN);
    DefaultResponseCreator naiResponse = MockRestResponseCreators.withStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION);

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        final ClientHttpRequestFactory requestFactory =
                (ClientHttpRequestFactory) ReflectionTestUtils.getField(restTemplate, "requestFactory");
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(requestFactory));
    }

    @Test
    void testSuccess_DontThrowException() {
        successResponse.body("respuesta");
        mockServer.expect(requestTo("/successURI"))
                .andRespond(successResponse);

        String response = restTemplate.getForObject("/successURI", String.class);
        assertThat(response).isEqualTo("respuesta");
    }

    @Test
    void testNoContent_DontThrowException() {

        mockServer.expect(requestTo("/noContentURI"))
                .andRespond(noContentResponse);

        String response = restTemplate.getForObject("/noContentURI", String.class);
        assertThat(response).isNull();
    }

    @Test
    void testBadRequest_ThrowHttpClientErrorException() {
        mockServer.expect(requestTo("/badRequestURI"))
                .andRespond(badRequestResponse);

        assertThrows(HttpClientErrorException.class, () -> {
            restTemplate.getForObject("/badRequestURI", String.class);
        });
    }

    @Test
    void testNotFound_ShouldThrowFunctionalException() throws JsonProcessingException {
        notFoundResponse.body(
                objectMapper.writeValueAsString(
                        new GlobalExceptionResponse(404, "not.found")));

        mockServer.expect(requestTo("/notFoundURI"))
                .andRespond(notFoundResponse);

        assertThrows(FunctionalException.class, () -> {
            restTemplate.getForObject("/notFoundURI", GlobalExceptionResponse.class);
        });
    }

    @Test
    void testBadRequest_ShouldThrowFunctionalException() throws JsonProcessingException {
        badRequestResponse.body(
                objectMapper.writeValueAsString(
                        new GlobalExceptionResponse(400, "invalid.input.parameters")));

        mockServer.expect(requestTo("/badRequestURI"))
                .andRespond(badRequestResponse);

        assertThrows(FunctionalException.class, () -> {
            restTemplate.getForObject("/badRequestURI", GlobalExceptionResponse.class);
        });
    }

    @Test
    void testDarwinUnauthorized_ShouldThrowFunctionalException() {
        String body = "{" +
                "    \"appName\": \"adn360-product-groups\"," +
                "    \"timeStamp\": \"1581835165886\"," +
                "    \"errorName\": \"UNAUTHORIZED\"," +
                "    \"status\": 401," +
                "    \"internalCode\": 401," +
                "    \"shortMessage\": \"UNAUTHORIZED\"," +
                "    \"detailedMessage\": \"Invalid credentials\"" +
                "}";
        unauthorizedResponse.body(body);

        mockServer.expect(requestTo("/unauthorizedURI"))
                .andRespond(unauthorizedResponse);

        assertThrows(FunctionalException.class, () -> {
            restTemplate.getForObject("/unauthorizedURI", String.class);
        });
    }

    @Test
    void testUnauthorized_ShouldThrowFunctionalException() throws JsonProcessingException {
        unauthorizedResponse.body(
                objectMapper.writeValueAsString(
                        new GlobalExceptionResponse(401, "unauthorized")));

        mockServer.expect(requestTo("/unauthorizedURI"))
                .andRespond(unauthorizedResponse);

        assertThrows(FunctionalException.class, () -> {
            restTemplate.getForObject("/unauthorizedURI", String.class);
        });
    }

    @Test
    void testForbidden_ShouldThrowFunctionalException() throws JsonProcessingException {
        forbiddenResponse.body(
                objectMapper.writeValueAsString(
                        new GlobalExceptionResponse(403, "forbidden")));

        mockServer.expect(requestTo("/forbiddenURI"))
                .andRespond(forbiddenResponse);

        assertThrows(FunctionalException.class, () -> {
            restTemplate.getForObject("/forbiddenURI", String.class);
        });
    }

    @Test
    void testNonAuthoritativeInformation_ShouldThrowFunctionalException() throws JsonProcessingException {
        naiResponse.body(
                objectMapper.writeValueAsString(
                        new GlobalExceptionResponse(203, "unauthorized")));

        mockServer.expect(requestTo("/naiURI"))
                .andRespond(naiResponse);

        assertThrows(FunctionalException.class, () -> {
            restTemplate.getForObject("/naiURI", String.class);
        });
    }

    @Test
    void testInternalServerError_ShouldThrowFeignError() throws JsonProcessingException {
        internalServerErrorResponse.body(
                objectMapper.writeValueAsString(
                        new GlobalExceptionResponse(500, "feign.client.error", "More Information")));

        mockServer.expect(requestTo("/internalServerErrorURI"))
                .andRespond(internalServerErrorResponse);

        String moreInfo = "";

        try {
            String response = restTemplate.getForObject("/internalServerErrorURI", String.class);

        } catch (FunctionalException ex) {
            assertThat(ex.getMessage()).isEqualTo(ExceptionEnum.FEIGN_CLIENT_ERROR.getMsg());
            moreInfo = ex.getMoreInformation();
        }

        assertThat(moreInfo).isEqualToIgnoringCase("More Information");
    }

    @Test
    void testInternalServerError_ShouldThrowConfidentialityError() throws JsonProcessingException {
        internalServerErrorResponse.body(
                objectMapper.writeValueAsString(
                        new GlobalExceptionResponse(500, "confidentiality.error", "More Information")));

        mockServer.expect(requestTo("/internalServerErrorURI"))
                .andRespond(internalServerErrorResponse);

        String moreInfo = "";
        try {
            String response = restTemplate.getForObject("/internalServerErrorURI", String.class);

        } catch (FunctionalException ex) {
            assertThat(ex.getMessage()).isEqualTo(ExceptionEnum.CONFIDENTIALITY_ERROR.getMsg());
            moreInfo = ex.getMoreInformation();
        }

        assertThat(moreInfo).isEqualToIgnoringCase("More Information");
    }

    @Test
    void testInternalServerError_ShouldThrowWSError() throws JsonProcessingException {
        internalServerErrorResponse.body(
                objectMapper.writeValueAsString(
                        new GlobalExceptionResponse(500, "webservice.error", "More Information")));

        mockServer.expect(requestTo("/internalServerErrorURI"))
                .andRespond(internalServerErrorResponse);

        String moreInfo = "";
        try {
            String response = restTemplate.getForObject("/internalServerErrorURI", String.class);

        } catch (FunctionalException ex) {
            assertThat(ex.getMessage()).isEqualTo(ExceptionEnum.WS_ERROR.getMsg());
            moreInfo = ex.getMoreInformation();
        }

        assertThat(moreInfo).isEqualToIgnoringCase("More Information");
    }

    @Test
    void testInternalServerError_ShouldThrowContractSituationError() throws JsonProcessingException {
        internalServerErrorResponse.body(
                objectMapper.writeValueAsString(
                        new GlobalExceptionResponse(500, "F00006", "More Information")));

        mockServer.expect(requestTo("/internalServerErrorURI"))
                .andRespond(internalServerErrorResponse);

        String moreInfo = "";
        try {
            String response = restTemplate.getForObject("/internalServerErrorURI", String.class);

        } catch (FunctionalException ex) {
            assertThat(ex.getMessage()).isEqualTo(ExceptionEnum.CONTRACT_SITUATION_ERROR.getMsg());
            moreInfo = ex.getMoreInformation();
        }

        assertThat(moreInfo).isEqualToIgnoringCase("More Information");
    }

    @Test
    void testInternalServerError_ShouldThrowInternalServerError() throws JsonProcessingException {
        final String XXXX = "XXXX";
        internalServerErrorResponse.body(
                objectMapper.writeValueAsString(
                        new GlobalExceptionResponse(500, XXXX, "More Information")));

        mockServer.expect(requestTo("/internalServerErrorURI"))
                .andRespond(internalServerErrorResponse);

        String moreInfo = "";
        try {
            String response = restTemplate.getForObject("/internalServerErrorURI", String.class);

        } catch (FunctionalException ex) {
            assertThat(ex.getInfo()).isEqualTo(ExceptionEnum.INTERNAL_SERVER_ERROR);
            assertThat(ex.getMessage()).isEqualTo(XXXX);
            moreInfo = ex.getMoreInformation();
        }

        assertThat(moreInfo).isEqualToIgnoringCase("More Information");
    }

}
