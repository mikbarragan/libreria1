package es.santander.adn360.core.config;

import com.santander.darwin.core.annotation.DarwinQualifier;
import com.santander.darwin.core.interceptor.DarwinContextInterceptor;
import es.santander.adn360.core.web.Adn360RestTemplateResponseErrorHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureObservability
class Adn360RestTemplatePostProcessorTest {

    @Autowired
    @DarwinQualifier
    private RestTemplate restTemplate;

    @Test
    void checkAdn360DarwinRestTemplateMerge() {

        boolean hasDarwinInterceptor = restTemplate.getInterceptors().stream()
                .anyMatch(interceptor -> interceptor instanceof DarwinContextInterceptor);

        assertThat(hasDarwinInterceptor).isTrue();

        boolean hasAdn360Handler = restTemplate.getErrorHandler() instanceof Adn360RestTemplateResponseErrorHandler;

        assertThat(hasAdn360Handler).isTrue();
    }

}
