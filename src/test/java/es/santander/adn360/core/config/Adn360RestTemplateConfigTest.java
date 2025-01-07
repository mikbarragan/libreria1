package es.santander.adn360.core.config;

import com.santander.darwin.core.annotation.DarwinQualifier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.doReturn;
import static org.wildfly.common.Assert.assertNotNull;
import static org.wildfly.common.Assert.assertTrue;

@SpringBootTest
@AutoConfigureObservability
class Adn360RestTemplateConfigTest {

    @MockBean
    @DarwinQualifier
    private RestTemplate restTemplate;

    @Autowired
    private Adn360RestTemplateConfig adn360RestTemplateConfig;
    @Autowired
    private com.santander.darwin.core.config.CoreProperties coreProperties;


    @Test
    void restTemplate() {
        ClientHttpRequestFactory requestFactory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        doReturn(requestFactory).when(restTemplate).getRequestFactory();

        RestTemplate result = adn360RestTemplateConfig.restTemplate(restTemplate, coreProperties);

        assertNotNull(result);
        ClientHttpRequestFactory resultRequestFactory = result.getRequestFactory();
        assertTrue(resultRequestFactory instanceof BufferingClientHttpRequestFactory);
    }
}
