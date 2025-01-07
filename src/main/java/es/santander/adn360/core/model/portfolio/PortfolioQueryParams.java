package es.santander.adn360.core.model.portfolio;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Set;

/**
 * PortfolioQueryParams class description
 * Contains portfolio types filters
 */
@Data
@Builder
public class PortfolioQueryParams {

    /** related to portfolio types **/
    @Parameter(
            description = """
                    List of portfolio types that all retrieved contracts must be related to. It only applies to active
                    portfolios. This filter is not compatible with include_contracts_with_portfolio. When
                    unrelated_to_portfolio_types and this are empty, it defaults to empty.
                    The meaning of each portfolio type is:
                    - MANAGED: contracts having at least a managed portfolio.
                    - NOT_ADMINISTERED: contracts having at least a not administered portfolio.
                    - ADVISED: contracts having at least an advised portfolio.
                    - ALL_MANAGED: contracts having all of its portfolios of type managed.
                    - ALL_NOT_ADMINISTERED: contracts having all of its portfolios of type not administered.
                    - ALL_ADVISED: contracts having all of its portfolios of type advised.
                    - NOT_MANAGED: contracts having portfolios whose type is not managed.
                    - NO_PORTFOLIO: contracts having no portfolios.
                    - REST: contracts with at least one portfolio that is not: managed, not administered or advised.
                    - ALL_REST: contracts with all portfolios that are not: managed, not administered or advised.""",
            example = "MANAGED")
    private Set<PortfolioFilterType> related_to_portfolio_types;

    /** unrelated to portfolio types **/
    @Parameter(
            description = """
                    List of portfolio types that all retrieved contracts must not be related to. It only applies to
                    active portfolios. This filter is not compatible with include_contracts_with_portfolio. When
                    related_to_portfolio_types and this are empty, it defaults to MANAGED. The meaning of each
                    portfolio type is:
                    - MANAGED: contracts having at least a managed portfolio.
                    - NOT_ADMINISTERED: contracts having at least a not administered portfolio.
                    - ADVISED: contracts having at least an advised portfolio.
                    - ALL_MANAGED: contracts having all of its portfolios of type managed.
                    - ALL_NOT_ADMINISTERED: contracts having all of its portfolios of type not administered.
                    - ALL_ADVISED: contracts having all of its portfolios of type advised.
                    - NOT_MANAGED: contracts having portfolios whose type is not managed.
                    - NO_PORTFOLIO: contracts having no portfolios.
                    - REST: contracts with at least one portfolio that is not: managed, not administered or advised.
                    - ALL_REST: contracts with all portfolios that are not: managed, not administered or advised.""",
            example = "MANAGED")
    private Set<PortfolioFilterType> unrelated_to_portfolio_types;

    /**
     * Constructor
     * @param relatedToPortfolioTypes    include contracts with these portfolio types
     * @param unrelatedToPortfolioTypes  exclude contracts with these portfolio types
     */
    public PortfolioQueryParams(Set<PortfolioFilterType> relatedToPortfolioTypes,
                                Set<PortfolioFilterType> unrelatedToPortfolioTypes) {
        this.related_to_portfolio_types = relatedToPortfolioTypes;
        this.unrelated_to_portfolio_types = unrelatedToPortfolioTypes;
    }

    /**
     * Sets default values when includePortfolioTypes and excludePortfolioTypes are empty
     */
    public void setDefaultValues() {
        if (this.isEmpty()) {
            this.related_to_portfolio_types = Set.of();
            this.unrelated_to_portfolio_types = Set.of(PortfolioFilterType.MANAGED);
        }
    }

    /**
     * Checks if related_to_portfolio_types and unrelated_to_portfolio_types are empty
     * @return true if both are empty, false otherwise
     */
    public boolean isEmpty() {
        return CollectionUtils.isEmpty(this.related_to_portfolio_types) &&
                CollectionUtils.isEmpty(this.unrelated_to_portfolio_types);
    }

    /**
     * Converts PortfolioQueryParams to MultiValueMap
     * @return MultiValueMap with related_to_portfolio_types and unrelated_to_portfolio_types
     */
    public MultiValueMap<String, PortfolioFilterType> toMap() {
        MultiValueMap<String, PortfolioFilterType> map = new LinkedMultiValueMap<>();
        if (!CollectionUtils.isEmpty(related_to_portfolio_types)) {
            map.put("related_to_portfolio_types", new ArrayList<>(this.related_to_portfolio_types));
        }
        if (!CollectionUtils.isEmpty(unrelated_to_portfolio_types)) {
            map.put("unrelated_to_portfolio_types", new ArrayList<>(this.unrelated_to_portfolio_types));
        }
        return map;
    }

}
