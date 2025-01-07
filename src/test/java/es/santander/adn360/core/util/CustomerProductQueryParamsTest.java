package es.santander.adn360.core.util;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureObservability
class CustomerProductQueryParamsTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void checkSituations_indicatorSettedACTIVES() {

        CustomerProductQueryParams params = CustomerProductQueryParams.getBuilder()
                                                                        .customer_id("F000000583")
                                                                        .situations_indicator("ACTIVES")
                                                                        .build();


        Assertions.assertThat(params.getSituations_indicator()).isEqualTo("ACTIVES");
    }

    @Test
    void checkSituations_indicatorISdActivedByDefect() {

        CustomerProductQueryParams params = CustomerProductQueryParams.getBuilder()
                .customer_id("F000000583")
                .build();


        Assertions.assertThat(params.getSituations_indicator()).isEqualTo("ACTIVES");
    }

    @Test
    void checkSituations_indicatorSettedCANCELED() {

        CustomerProductQueryParams params = CustomerProductQueryParams.getBuilder()
                .customer_id("F000000583")
                .situations_indicator("CANCELED")
                .build();


        Assertions.assertThat(params.getSituations_indicator()).isEqualTo("CANCELED");
    }

    @Test
    void checkCancelledSituations_indicator() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/test/statusContract")
                .queryParam("application", "ADN360")
                .queryParam("segment", "EMPR")
                .queryParam("customer_id", "J087293443")
                .queryParam("situations_indicator", "CANCELED");

        ResponseEntity<String> out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, String.class);
        assertEquals(HttpStatus.OK, out.getStatusCode());
        assertEquals("CANCELED", out.getBody());
    }


    @Test
    void checkCDefectSituations_indicator() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/test/statusContract")
                .queryParam("application", "ADN360")
                .queryParam("segment", "EMPR")
                .queryParam("customer_id", "J087293443");

        ResponseEntity<String> out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, String.class);
        assertEquals(HttpStatus.OK, out.getStatusCode());
        assertEquals("ACTIVES", out.getBody());
    }

    @Test
    void check_invalid_Situations_indicator() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/test/statusContract")
                .queryParam("application", "ADN360")
                .queryParam("segment", "EMPR")
                .queryParam("customer_id", "J087293443")
                .queryParam("situations_indicator", "ERROR");

        ResponseEntity<String> out = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, out.getStatusCode());
    }


}
