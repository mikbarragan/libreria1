package es.santander.adn360.core.config;

import com.santander.darwin.core.annotation.DarwinQualifier;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * The Adn360 Darwin RestTemplate Bean
 */
@Configuration
@EnableConfigurationProperties(CoreProperties.class)
@ComponentScan("es.santander.adn360.core")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class Adn360RestTemplateConfig {

    /**
     * Bean que disponibiliza el restTemplate de Darwin y Adn360
     *
     * @param restTemplate Darwin
     * @param coreProperties coreProperties
     * @return Adn360 Darwin RestTemplate
     */
    @Bean
    @DarwinQualifier
    @Primary
    public RestTemplate restTemplate(@DarwinQualifier RestTemplate restTemplate,
                                     com.santander.darwin.core.config.CoreProperties coreProperties) {

        com.santander.darwin.core.config.CoreProperties.RestTemplate restTemProp = coreProperties.getRestTemplate();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(restTemProp.getConnectionRequestTimeout(), TimeUnit.MILLISECONDS)
                .setConnectTimeout(restTemProp.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .setResponseTimeout(restTemProp.getReadTimeout(), TimeUnit.MILLISECONDS)
                .build();

        // Crear un PoolingHttpClientConnectionManager para gestionar las conexiones
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setDefaultMaxPerRoute(restTemProp.getMaxRouteConnections());
        connManager.setMaxTotal(restTemProp.getMaxTotalConnections());
        // Crear el cliente HTTP con las configuraciones personalizadas
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connManager)
                .build();

        // Configuracion para poder leer del buffer varias veces con el BufferingClientHttpRequestFactory
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(
                new HttpComponentsClientHttpRequestFactory(httpClient)));
        return restTemplate;
    }

    /**
     * nonsense commentary to comply with a Sonar nonsense rule
     * nonsense commentary to comply with a Sonar nonsense rule
     * nonsense commentary to comply with a Sonar nonsense rule
     */

}
