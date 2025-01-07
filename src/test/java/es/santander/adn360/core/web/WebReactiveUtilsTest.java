package es.santander.adn360.core.web;

import com.santander.darwin.core.context.DarwinContext;
import com.santander.darwin.core.context.DarwinInfo;
import com.santander.darwin.core.context.ReactiveDarwinContextHolder;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@TestPropertySource(properties = {
        "spring.main.web-application-type=reactive",
        "spring.main.allow-bean-definition-overriding=true"})
class WebReactiveUtilsTest {

    @Test
    void testGetChannel() {

        ReactiveDarwinContextHolder.withDarwinContext(createDarwinContextWithAuthentication());
        StepVerifier
                .create(WebReactiveUtils.getSantanderChannel())
                .expectComplete()
                .verify();
    }

    @Test
    void testGetSantanderChannelWithChannel() {
        DarwinContext darwinContext = createDarwinContextWithAuthentication();
        ReactiveDarwinContextHolder.withDarwinContext(darwinContext)
                .stream().map(context -> {
                    Mono<String> result = WebReactiveUtils.getSantanderChannel();
                    return StepVerifier.create(result)
                            .expectNext("OFI")
                            .verifyComplete();
                });
    }

    @Test
    void testGetSantanderChannelWithoutChannel() {
        DarwinContext darwinContext = createEmptyDarwinContext();
        ReactiveDarwinContextHolder.withDarwinContext(darwinContext)
                .stream().map(context -> {
                    Mono<String> result = WebReactiveUtils.getSantanderChannel();
                    return StepVerifier.create(result)
                            .verifyComplete();
                });
    }

    private DarwinContext createDarwinContextWithAuthentication() {
        DarwinInfo darwinInfo = DarwinInfo.builder().channel("OFI").build();
        DarwinContext darwinContext = DarwinContext.builder().darwinInfo(darwinInfo).build();
        return darwinContext;
    }
    private DarwinContext createEmptyDarwinContext() {
        return DarwinContext.builder().build();
    }

    @Test
    void testGetToken() {

        Mono<String> actualToken = WebReactiveUtils.getTokenUser();
        StepVerifier
                .create(actualToken)
                .expectComplete()
                .verify();
    }

    @Test
    void testGetTokenWithAuthenticatedUser() {
        SecurityContext securityContext = createSecurityContextWithAuthentication("testUser");
        ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext))
                .stream().map(context -> {
                    Mono<String> result = WebReactiveUtils.getTokenUser();
                    return StepVerifier.create(result)
                            .expectNext("testUser")
                            .verifyComplete();
                });
    }

    @Test
    void testGetTokenWithoutAuthenticatedUser() {
        SecurityContext securityContext = createEmptySecurityContext();

        ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext))
                .stream().map(context -> {
                    Mono<String> result = WebReactiveUtils.getTokenUser();
                    return StepVerifier.create(result)
                            .verifyComplete();
                });
    }
    private SecurityContext createSecurityContextWithAuthentication(String username) {
        Authentication authentication = new TestingAuthenticationToken(username, null);
        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        return securityContext;
    }
    private SecurityContext createEmptySecurityContext() {
        return new SecurityContextImpl();
    }

    @Test
    void getInternalUser() {
        StepVerifier
                .create(WebReactiveUtils.getInternalUser())
                .expectComplete()
                .verify();

    }

    @Test
    void testGetInternalWithInstanceOf() {
        SecurityContext securityContext = createSecurityContextWithContextImpl();

        ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext))
                .stream().map(context -> {
                    Mono<String> result = WebReactiveUtils.getInternalUser();
                    return StepVerifier.create(result)
                            .expectNext("A000111")
                            .verifyComplete();
                });
    }

    @Test
    void testGetInternalWithoutInstanceOf() {
        SecurityContext securityContext = createEmptySecurityContextWithContextImpl();

        ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext))
                .stream().map(context -> {
                    Mono<String> result = WebReactiveUtils.getInternalUser();
                    return StepVerifier.create(result)
                            .verifyComplete();
                });
    }

    private SecurityContext createSecurityContextWithContextImpl() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
        return new CoreSecurityContextImpl(authentication, "EMP", "A000111");
    }

    private SecurityContext createEmptySecurityContextWithContextImpl() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
        return new CoreSecurityContextImpl(authentication, null, null);
    }

    @Test
    void channelUtilsCheck() {

        StepVerifier
                .create(WebReactiveUtils.isOfiChannel())
                .expectComplete()
                .verify();

        StepVerifier
                .create(WebReactiveUtils.isEmpChannel())
                .expectComplete()
                .verify();

        StepVerifier
                .create(WebReactiveUtils.isRmlChannel())
                .expectComplete()
                .verify();

        StepVerifier
                .create(WebReactiveUtils.isIntChannel())
                .expectComplete()
                .verify();

        StepVerifier
                .create(WebReactiveUtils.isIntChannel())
                .expectComplete()
                .verify();

    }

    @Test
    void testIsOfiChannel() {

        DarwinInfo darwinInfo = DarwinInfo.builder().channel("OFI").build();
        DarwinContext darwinContext = DarwinContext.builder().darwinInfo(darwinInfo).build();
        ReactiveDarwinContextHolder.withDarwinContext(darwinContext)
                .stream().map(context -> {
                    Mono<Boolean> result = WebReactiveUtils.isOfiChannel();
                    return StepVerifier.create(result)
                            .expectNext(true)
                            .verifyComplete();
                });
    }

    @Test
    void testIsEmpChannel() {

        DarwinInfo darwinInfo = DarwinInfo.builder().channel("EMP").build();
        DarwinContext darwinContext = DarwinContext.builder().darwinInfo(darwinInfo).build();
        ReactiveDarwinContextHolder.withDarwinContext(darwinContext)
                .stream().map(context -> {
                    Mono<Boolean> result = WebReactiveUtils.isEmpChannel();
                    return StepVerifier.create(result)
                            .expectNext(true)
                            .verifyComplete();
                });
    }

    @Test
    void testIsINTChannel() {

        DarwinInfo darwinInfo = DarwinInfo.builder().channel("EMP").build();
        DarwinContext darwinContext = DarwinContext.builder().darwinInfo(darwinInfo).build();
        ReactiveDarwinContextHolder.withDarwinContext(darwinContext)
                .stream().map(context -> {
                    Mono<Boolean> result = WebReactiveUtils.isOfiChannel();
                    return StepVerifier.create(result)
                            .expectNext(false)
                            .verifyComplete();
                });
    }

    @Test void checkInputChannel() {

        StepVerifier
                .create(WebReactiveUtils.isChannel(Mono.just("OFI")))
                .expectComplete()
                .verify();

    }
}