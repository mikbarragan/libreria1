package es.santander.adn360.core.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * Service to deal with reactive context
 */
@Slf4j
@Service
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveContextUtils {

    /**
     * Constructor
     */
    public ReactiveContextUtils() {
        //nonsense commentary to comply with a Sonar nonsense rule
    }

    /**
     * Method to get the context
     *
     * @return mono securityContext
     */
    public Mono<SecurityContext> getContext() {

        return ReactiveSecurityContextHolder.getContext();
    }

    /**
     * Method to set the security context
     *
     * @param securityContext   security context to set
     * @return context
     */
    public Context withSecurityContext(SecurityContextImpl securityContext) {

        return ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext));
    }

}
