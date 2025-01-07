package es.santander.adn360.core.web;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureObservability
class PaginationHandlerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testPaginationInvalidOffset() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/test/pagination")
                .queryParam(PaginationHandler.PARAM_OFFSET, "aaaaaaaa")
                .queryParam(PaginationHandler.PARAM_LIMIT, 1);

        ResponseEntity<TestController.TestPaginatedResponse> out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, TestController.TestPaginatedResponse.class);
        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testPaginationInvalidOffset2() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/test/pagination")
                .queryParam(PaginationHandler.PARAM_OFFSET, -1)
                .queryParam(PaginationHandler.PARAM_LIMIT, 1);

        ResponseEntity<TestController.TestPaginatedResponse> out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, TestController.TestPaginatedResponse.class);
        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testPaginationInvalidLimit() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/test/pagination")
                .queryParam(PaginationHandler.PARAM_OFFSET, "primero")
                .queryParam(PaginationHandler.PARAM_LIMIT, "invalid");

        ResponseEntity<TestController.TestPaginatedResponse> out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, TestController.TestPaginatedResponse.class);
        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testPaginationInvalidLimit2() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/test/pagination")
                .queryParam(PaginationHandler.PARAM_OFFSET, "primero")
                .queryParam(PaginationHandler.PARAM_LIMIT, 0);

        ResponseEntity<TestController.TestPaginatedResponse> out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, TestController.TestPaginatedResponse.class);
        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testPaginationOK() {
        // The response is a list with "primero", "segundo", "tercero", "cuarto", "quinto", "sexto", "septimo" strings
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/test/pagination")
                .queryParam(PaginationHandler.PARAM_OFFSET, "primero")
                .queryParam(PaginationHandler.PARAM_LIMIT, 3);

        ResponseEntity<TestController.TestPaginatedResponse> out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, TestController.TestPaginatedResponse.class);
        // Check if response is what exptected
        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(out.getBody().getList()).isEqualTo(Arrays.asList("primero", "segundo", "tercero"));
        // Check Link header
        List<String> linkHeader = out.getHeaders().get(PaginationHandler.HEADER_LINK);
        assertThat(linkHeader).hasSize(1);
        String[] links = StringUtils.substringsBetween(linkHeader.get(0), "<", ">");
        // Only next link is provided
        assertThat(links).hasSize(1);

        // Make a call with next link provided and check response
        builder = builder.fromUriString(links[0]);
        out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, TestController.TestPaginatedResponse.class);
        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(out.getBody().getList()).isEqualTo(Arrays.asList("cuarto", "quinto", "sexto"));
        // Check Link header
        linkHeader = out.getHeaders().get(PaginationHandler.HEADER_LINK);
        assertThat(linkHeader).hasSize(1);
        links = StringUtils.substringsBetween(linkHeader.get(0), "<", ">");
        // Only next link is provided
        assertThat(links).hasSize(2); // Now previous and next links are provided

        // Call previous link and check response
        builder = builder.fromUriString(links[0]);
        out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, TestController.TestPaginatedResponse.class);
        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(out.getBody().getList()).isEqualTo(Arrays.asList("primero", "segundo", "tercero"));

        // Call next link and check response
        builder = builder.fromUriString(links[1]);
        out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, TestController.TestPaginatedResponse.class);
        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(out.getBody().getList()).isEqualTo(Arrays.asList("septimo"));
        // As it is the last response, only previous link us provided
        linkHeader = out.getHeaders().get(PaginationHandler.HEADER_LINK);
        assertThat(linkHeader).hasSize(1);
        links = StringUtils.substringsBetween(linkHeader.get(0), "<", ">");
        assertThat(links).hasSize(1);
    }

    @Test
    void testPaginationWhenFoundHeadersInTheHttpServletResponse() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/test/paginationEvaluateNullHeaders")
                .queryParam(PaginationHandler.PARAM_OFFSET, "primero")
                .queryParam(PaginationHandler.PARAM_LIMIT, 3);

        ResponseEntity<TestController.TestPaginatedResponse> out = restTemplate.exchange(builder.build().toUri(),
                HttpMethod.GET,
                null,
                TestController.TestPaginatedResponse.class);

//        List[3], Headers[4] & (isPaginated -> false)
        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(out.getHeaders().size()).isEqualTo(6);
        assertThat(out.getBody().getList()).isEqualTo(Arrays.asList("primero", "segundo", "tercero"));

        builder = UriComponentsBuilder.fromPath("/test/paginationEvaluateExampleHeader")
                .queryParam(PaginationHandler.PARAM_OFFSET, "primero")
                .queryParam(PaginationHandler.PARAM_LIMIT, 3);

        out = restTemplate.exchange(builder.build().toUri(),
                HttpMethod.GET,
                null,
                TestController.TestPaginatedResponse.class);

//        List[3], Headers[5] & Header[Example-Header] exist (isPaginated -> false)
        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(out.getHeaders().size()).isEqualTo(7);
        assertThat(out.getHeaders().containsKey("Example-Header")).isTrue();
        assertThat(out.getHeaders().get("Example-Header").get(0)).isEqualToIgnoringCase("value");
        assertThat(out.getBody().getList()).isEqualTo(Arrays.asList("primero", "segundo", "tercero"));

        builder = UriComponentsBuilder.fromPath("/test/paginationEvaluateInvalidLinkHeader")
                .queryParam(PaginationHandler.PARAM_OFFSET, "primero")
                .queryParam(PaginationHandler.PARAM_LIMIT, 3);

        out = restTemplate.exchange(builder.build().toUri(),
                HttpMethod.GET,
                null,
                TestController.TestPaginatedResponse.class);

//        List[3], Headers[4], Link[2] & contains invalid link value (isPaginated -> false)
        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(out.getHeaders().size()).isEqualTo(6);
        assertThat(out.getHeaders().containsKey("Link")).isTrue();
        assertThat(out.getHeaders().get("Link").size()).isEqualTo(2);
        assertThat(out.getHeaders().get("Link").contains("</test/paginationEvaluateValidLinkHeader?_limit=3&_offset=septimo>;other=\"self\"")).isTrue();
        assertThat(out.getBody().getList()).isEqualTo(Arrays.asList("primero", "segundo", "tercero"));

        builder = UriComponentsBuilder.fromPath("/test/paginationEvaluateValidLinkHeader")
                .queryParam(PaginationHandler.PARAM_OFFSET, "primero")
                .queryParam(PaginationHandler.PARAM_LIMIT, 3);

        out = restTemplate.exchange(builder.build().toUri(),
                HttpMethod.GET,
                null,
                TestController.TestPaginatedResponse.class);

//        List[7], Headers[4] Link[1] (isPaginated -> true)
        assertThat(out.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(out.getHeaders().size()).isEqualTo(6);
        assertThat(out.getHeaders().containsKey("Link")).isTrue();
        assertThat(out.getHeaders().get("Link").size()).isEqualTo(1);
        assertThat(out.getHeaders().get("Link").get(0)).isEqualToIgnoringCase("</test/paginationEvaluateValidLinkHeader?_limit=3&_offset=septimo>; rel=\"next\"");
        assertThat(out.getBody().getList()).isEqualTo(Arrays.asList("primero", "segundo", "tercero", "cuarto", "quinto", "sexto", "septimo"));
    }
}