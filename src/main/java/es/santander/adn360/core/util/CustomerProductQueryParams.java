package es.santander.adn360.core.util;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * Class for query params related to customers.
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerProductQueryParams extends ProductQueryParams {

    /**
     * Generated serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The customer identifier
     */
    @Parameter(description = "The customer identifier", example = "F087293443")
    @Pattern(regexp = "^(F|J)[0-9]{9}$")
    private String customer_id;

    /**
     * When given parameter is set, contract will not be from this customer
     */
    @Parameter(description = "When given parameter is set, contract will not be from this customer",
			example = "F087293443")
    @Pattern(regexp = "^(F|J)[0-9]{9}$")
    private String excluded_customer_id;

    /**
     * The Interviner type
     */
    @Parameter(description = "The Intervinier type, Available values: OWN,ALL", example = "OWN")
    @Pattern(regexp = "(OWN|ALL)")
    private String participant_type;

    /**
     * The situations indicator contracts
     */
    @Parameter(description = "The situations indicator contracts. Available values: ACTIVES,CANCELED,ALL", example = "ACTIVES")
    @Pattern(regexp = "^(ACTIVES|CANCELED|ALL|PRECANCELED)$")
    private String situations_indicator = "ACTIVES";

    /**
     * The Intervinier Order
     */
    @Parameter(description = "The Intervinier Order, possible values are ALL or a number", example = "ALL")
    @Pattern(regexp = "(ALL|[0-9])")
    private String participant_order;

    /**
     * Get customer product query params builder.
     * @param application application name
     * @param segment segmen
     * @param customer_id customer identifier
     * @param excluded_customer_id excluded customer identifier
     * @param participant_type participant type
     * @param participant_order participant order
     * @param situations_indicator situations indicator
     */
    @Builder(builderMethodName = "getBuilder")
    public CustomerProductQueryParams(
            String application,
            String segment,
            String customer_id,
            String excluded_customer_id,
            String participant_type,
            String participant_order,
            String situations_indicator
    ) {
        super(application, segment);
        this.customer_id = customer_id;
        this.excluded_customer_id = excluded_customer_id;
        this.participant_type = participant_type;
        this.participant_order = participant_order;
        this.situations_indicator = situations_indicator == null ? "ACTIVES" : situations_indicator;
    }
}
