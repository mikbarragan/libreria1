package es.santander.adn360.core.web;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;

/**
 * Security context that stores the Santander Channel
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CoreSecurityContextImpl extends SecurityContextImpl {
    /**
	 * Generated serial version UID
	 */
	private static final long serialVersionUID = 1763899208903496312L;
    /**
     * Santander Channel
     */
	private final String santanderChannel;
    /**
     * Internal User
     */
    private final String internalUser;

    /**
     * Constructor
     *
     * @param authentication   Authentication
     * @param santanderChannel Santander channel
     * @param internalUser     Internal user associated to user token
     */
    CoreSecurityContextImpl(Authentication authentication, String santanderChannel, String internalUser) {

        super(authentication);

        this.santanderChannel = santanderChannel;
        this.internalUser = internalUser;
    }
}
