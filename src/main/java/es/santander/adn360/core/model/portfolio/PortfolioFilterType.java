package es.santander.adn360.core.model.portfolio;

/**
 * The PortfolioFilterType enum represents the different types of portfolio filters that can be used.
 * Each enum value corresponds to a specific type of portfolio filter.
 */
public enum PortfolioFilterType {
    MANAGED("MANAGED"),
    NOT_ADMINISTERED("NOT_ADMINISTERED"),
    ADVISED("ADVISED"),
    NOT_MANAGED("NOT_MANAGED"),
    ALL_MANAGED("ALL_MANAGED"),
    ALL_NOT_ADMINISTERED("ALL_NOT_ADMINISTERED"),
    ALL_ADVISED("ALL_ADVISED"),
    NO_PORTFOLIO("NO_PORTFOLIO"),
    REST("REST"),
    ALL_REST("ALL_REST");

    private final String value;

    /**
     * Constructs a new PortfolioFilterType with the specified value.
     *
     * @param value the value of the portfolio filter type
     */
    PortfolioFilterType(String value) {
        this.value = value;
    }

    /**
     * Returns the value of the portfolio filter type.
     *
     * @return the value of the portfolio filter type
     */
    public String getValue() {
        return value;
    }
}
