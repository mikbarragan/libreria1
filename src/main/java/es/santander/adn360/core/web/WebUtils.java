package es.santander.adn360.core.web;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

/**
 * Static util methods.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public final class WebUtils {
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
     * Gets current request Santander Channel from SecurityContextHolder
     *
     * @return Santander channel
     */
    public static String getSantanderChannel() {
        return SecurityContextHolder.getContext() instanceof CoreSecurityContextImpl
                ? ((CoreSecurityContextImpl) SecurityContextHolder.getContext()).getSantanderChannel()
                : null;
    }

    /**
     * Gets current the internal user of the current logged user
     *
     * @return Internal user
     */
    public static String getInternalUser() {
        return SecurityContextHolder.getContext() instanceof CoreSecurityContextImpl
                ? ((CoreSecurityContextImpl) SecurityContextHolder.getContext()).getInternalUser()
                : null;
    }

    /**
     * Gets current logged user.
     *
     * @return User id
     */
    public static String getTokenUser() {
        // Get JWT token from SecurityContextHolder
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
    }

    /**
     * Is the channel empresas?
     * @param channel channel
     *
     * @return true if channel is EMP
     */
    public static boolean isChannel(final String channel) {

        Objects.requireNonNull(channel, "Channel is null.");

        return channel.equalsIgnoreCase(getSantanderChannel());
    }

    /**
     * Is the channel empresas?
     *
     * @return true if channel is EMP
     */
    public static boolean isEmpChannel() {
        return SANTANDER_CHANNEL_EMP.equalsIgnoreCase(getSantanderChannel());
    }

    /**
     * Is the channel oficina?
     *
     * @return true if channel is OFI
     */
    public static boolean isOfiChannel() {
        return SANTANDER_CHANNEL_OFI.equalsIgnoreCase(getSantanderChannel());
    }

    /**
     * Is the channel RML?
     *
     * @return true if channel is RML
     */
    public static boolean isRmlChannel() {
        return SANTANDER_CHANNEL_RML.equalsIgnoreCase(getSantanderChannel());
    }

    /**
     * Is the channel internet?
     *
     * @return true if channel is INT
     */
    public static boolean isIntChannel() {
        return SANTANDER_CHANNEL_INT.equalsIgnoreCase(getSantanderChannel());
    }

}
