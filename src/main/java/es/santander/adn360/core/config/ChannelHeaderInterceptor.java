package es.santander.adn360.core.config;

import es.santander.adn360.core.web.WebSecurityFilter;
import es.santander.adn360.core.web.WebUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Optional;

/**
 * ClientHttpRequestInterceptor for propagate the Santander channel header in requests
 */
public class ChannelHeaderInterceptor implements ClientHttpRequestInterceptor {

    /**
     * Interceptor
     * @param httpRequest request http
     * @param bytes       bytes
     * @param clientHttpRequestExecution execution client
     * @return  client http response
     * @throws IOException  input output exception
     */
    @Override
    public ClientHttpResponse intercept(
            final HttpRequest httpRequest,
            final byte[] bytes,
            final ClientHttpRequestExecution clientHttpRequestExecution
    ) throws IOException {

        // Add channel header to requests.
        Optional.ofNullable(WebUtils.getSantanderChannel())
                .ifPresent(channel -> httpRequest.getHeaders().add(WebSecurityFilter.HEADER_CHANNEL, channel));

        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }
}
