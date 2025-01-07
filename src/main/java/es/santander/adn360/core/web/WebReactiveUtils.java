package es.santander.adn360.core.web;

import com.santander.darwin.core.context.DarwinContext;
import com.santander.darwin.core.context.DarwinInfo;
import com.santander.darwin.core.context.ReactiveDarwinContextHolder;
import com.santander.darwin.core.exceptions.HttpBaseDarwinException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;

/**
 * Static util methods.
 */
@Slf4j
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public final class WebReactiveUtils {
    /**
     * Santander Channel EMP
     */
    public static final String SANTANDER_CHANNEL_EMP = "EMP";
    /**
     * Santander Channel OFI
     */
    public static final String SANTANDER_CHANNEL_OFI = "OFI";
    /**
     * Santander Channel RML
     */
    public static final String SANTANDER_CHANNEL_RML = "RML";
    /**
     * Santander Channel INT
     */
    public static final String SANTANDER_CHANNEL_INT = "INT";

    /**
     * Private constructor to avoid instantiation
     */
    private WebReactiveUtils() {
    }

    /**
     * Gets current request Santander Channel from SecurityContextHolder
     *
     * @return Mono Santander channel
     */
    public static Mono<String> getSantanderChannel() {

        return  ReactiveDarwinContextHolder.getContext().map(DarwinContext::getDarwinInfo)
            .map(DarwinInfo::getChannel)
            .switchIfEmpty(Mono.empty())
            .onErrorResume(e -> Mono.error(
                new HttpBaseDarwinException( "Channel is required", HttpStatus.BAD_REQUEST,
                        "Channel is required", "X-Santander-Channel is not informed")));

    }

    /**
     * Gets current the internal user of the current logged user
     *
     * @return Mono Internal user
     */
    public static Mono<String> getInternalUser() {
        return ReactiveSecurityContextHolder.getContext()
            .flatMap(context -> context instanceof CoreSecurityContextImpl ?
                Mono.just(((CoreSecurityContextImpl) context).getInternalUser()) : Mono.empty());
    }

    /**
     * Gets current logged user.
     *
     * @return Mono User id
     */
    public static Mono<String> getTokenUser() {
        // Get JWT token from SecurityContextHolder
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
                .map(Authentication::getName).switchIfEmpty(Mono.empty());

    }

    /**
     * Is the channel empresas?
     * @param channel channel
     *
     * @return true if channel is EMP
     */
    public static Mono<Boolean> isChannel(Mono<String> channel) {

        return Mono.zip(channel, getSantanderChannel()).map(tuple -> tuple.getT1().equals(tuple.getT2()));

    }

    /**
     * Is the channel empresas?
     *
     * @return true if channel is EMP
     */
    public static Mono<Boolean> isEmpChannel() {
        return getSantanderChannel().map(empChannel -> SANTANDER_CHANNEL_EMP.equalsIgnoreCase(empChannel));
    }

    /**
     * Is the channel oficina?
     *
     * @return true if channel is OFI
     */
    public static Mono<Boolean> isOfiChannel() {
        return getSantanderChannel().map(ofiChannel -> SANTANDER_CHANNEL_OFI.equalsIgnoreCase(ofiChannel));
    }

    /**
     * Is the channel RML?
     *
     * @return true if channel is RML
     */
    public static Mono<Boolean> isRmlChannel() {
        return getSantanderChannel().map(rmlChannel -> SANTANDER_CHANNEL_RML.equalsIgnoreCase(rmlChannel));

    }

    /**
     * Is the channel internet?
     *
     * @return true if channel is INT
     */
    public static Mono<Boolean> isIntChannel() {
        return getSantanderChannel().map(intChannel -> SANTANDER_CHANNEL_INT.equalsIgnoreCase(intChannel));
    }

}
