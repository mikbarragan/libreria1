package es.santander.adn360.core.web;

import com.santander.darwin.security.authentication.AuthenticationBearerToken;
import es.santander.adn360.core.config.CoreProperties;
import es.santander.adn360.core.model.document.InternalUser;
import es.santander.adn360.core.model.document.repository.InternalUserReactiveRepository;
import es.santander.adn360.core.model.exception.FunctionalException;
import es.santander.adn360.core.util.ExceptionEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.ReactorContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static es.santander.adn360.core.web.WebSecurityReactiveFilter.HEADER_CHANNEL;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.main.web-application-type=reactive",
        "spring.main.allow-bean-definition-overriding=true"})
@AutoConfigureObservability
class WebSecurityReactiveFilterTest {

    private final ServerHttpRequest httpRequest = Mockito.mock(ServerHttpRequest.class);
    private final ServerWebExchange exchange = Mockito.mock(ServerWebExchange.class);
    private final SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    private final HttpHeaders headers = Mockito.mock(HttpHeaders.class);

    private final WebFilterChain filterChain = Mockito.mock(WebFilterChain.class);
    private final Authentication authentication = Mockito.mock(Authentication.class);
    private final AuthenticationBearerToken authenticationBearer = Mockito.mock(AuthenticationBearerToken.class);

    private ReactiveContextUtils reactiveContextUtils = Mockito.mock(ReactiveContextUtils.class);
    private CoreProperties coreProperties = Mockito.mock(CoreProperties.class);
    private InternalUserReactiveRepository internalUserRepository = Mockito.mock(InternalUserReactiveRepository.class);

    private TestExecutionListener reactorContextTestExecutionListener = new ReactorContextTestExecutionListener();

    @Autowired
    private WebSecurityReactiveFilter webSecurityFilter;

    @BeforeEach
    void setUp() {

        webSecurityFilter.setCoreProperties(coreProperties);
        webSecurityFilter.setReactiveContextUtils(reactiveContextUtils);
        webSecurityFilter.setInternalUserRepository(Optional.of(internalUserRepository));

        Mockito.when(this.exchange.getRequest()).thenReturn(this.httpRequest);
        Mockito.when(this.httpRequest.getHeaders()).thenReturn(this.headers);
        Mockito.when(this.headers.getFirst(HEADER_CHANNEL)).thenReturn("EMP");
        Mockito.when(this.coreProperties.getChannelHeaderValidation()).thenReturn(true);
        Mockito.when(this.securityContext.getAuthentication()).thenReturn(authenticationBearer);
        Mockito.when(this.authenticationBearer.getName()).thenReturn("token");
        Mockito.when(this.reactiveContextUtils.getContext()).thenReturn(Mono.just(securityContext));
        Mockito.when(this.filterChain.filter(exchange)).thenReturn(Mono.empty());
        Mockito.when(this.internalUserRepository.findOneByIdUsuario(any())).thenReturn(
                Mono.just(InternalUser.builder().usuarioInterno("testUser").build()));

    }

    @Test
    void testFilterNoChannelHeaderValidation() {

        Mockito.when(this.coreProperties.getChannelHeaderValidation()).thenReturn(null);

        StepVerifier
                .create(this.webSecurityFilter.filter(this.exchange,this.filterChain))
                .expectComplete()
                .verify();

        Mockito.verify(this.reactiveContextUtils, Mockito.never()).withSecurityContext(any());
        Mockito.verify(this.filterChain).filter(any());

    }

    @Test
    void testFilterNoBearerAuthenticationToken() {

        Mockito.when(this.securityContext.getAuthentication()).thenReturn(authentication);

        StepVerifier
                .create(this.webSecurityFilter.filter(this.exchange,this.filterChain))
                .expectComplete()
                .verify();

        Mockito.verify(this.reactiveContextUtils, Mockito.never()).withSecurityContext(any());
        Mockito.verify(this.filterChain).filter(any());

    }

    @Test
    void testFilterNoAuthentication() {

        Mockito.when(this.securityContext.getAuthentication()).thenReturn(null);

        StepVerifier
                .create(this.webSecurityFilter.filter(this.exchange,this.filterChain))
                .expectComplete()
                .verify();

        Mockito.verify(this.reactiveContextUtils, Mockito.never()).withSecurityContext(any());
        Mockito.verify(this.filterChain).filter(any());

    }

    @Test
    void testFilterNoChannel() {

        Mockito.when(this.headers.getFirst(HEADER_CHANNEL)).thenReturn(null);
        StepVerifier
                .create(this.webSecurityFilter.filter(this.exchange,this.filterChain))
                .expectErrorMatches(throwable -> throwable instanceof FunctionalException
                        && ExceptionEnum.INVALID_INPUT_PARAMETERS.equals(((FunctionalException) throwable).getInfo())
                        && "X-Santander-Channel header missing.".equals(((FunctionalException) throwable).getMoreInformation()))
                .verify();

        Mockito.verify(this.reactiveContextUtils, Mockito.never()).withSecurityContext(any());
        Mockito.verify(this.filterChain, Mockito.never()).filter(any());

    }

    @Test
    void testFilterOKNoEmpChannel() {

        Mockito.when(this.headers.getFirst(HEADER_CHANNEL)).thenReturn("OFI");
        StepVerifier
                .create(this.webSecurityFilter.filter(this.exchange,this.filterChain))
                .expectComplete()
                .verify();

        Mockito.verify(this.reactiveContextUtils).withSecurityContext(any());
        Mockito.verify(this.internalUserRepository, Mockito.never()).findOneByIdUsuario(any());
        Mockito.verify(this.filterChain).filter(any());

    }

    @Test
    void testFilterOKEmpChannel() {

        StepVerifier
                .create(this.webSecurityFilter.filter(this.exchange,this.filterChain))
                .expectComplete()
                .verify();

        Mockito.verify(this.reactiveContextUtils).withSecurityContext(any());
        Mockito.verify(this.internalUserRepository).findOneByIdUsuario(any());
        Mockito.verify(this.filterChain).filter(any());

    }

    @Test
    void testFilterErrorCallingInternalUserRepository() {

        Mockito.when(this.internalUserRepository.findOneByIdUsuario(any())).thenThrow(new RuntimeException("whatever"));

        StepVerifier
                .create(this.webSecurityFilter.filter(this.exchange,this.filterChain))
                .expectComplete()
                .verify();

        Mockito.verify(this.reactiveContextUtils).withSecurityContext(any());
        Mockito.verify(this.internalUserRepository).findOneByIdUsuario(any());
        Mockito.verify(this.filterChain).filter(any());

    }

}
