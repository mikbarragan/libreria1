package es.santander.adn360.core.portfolio;

import es.santander.adn360.core.model.portfolio.PortfolioFilterType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PortfolioFilterTypeTest {


    @Test
    void testGetValue() {
        assertEquals(10, PortfolioFilterType.values().length);

        assertEquals("MANAGED", PortfolioFilterType.MANAGED.getValue());
        assertEquals("NOT_ADMINISTERED", PortfolioFilterType.NOT_ADMINISTERED.getValue());
        assertEquals("ADVISED", PortfolioFilterType.ADVISED.getValue());
        assertEquals("NOT_MANAGED", PortfolioFilterType.NOT_MANAGED.getValue());
        assertEquals("ALL_MANAGED", PortfolioFilterType.ALL_MANAGED.getValue());
        assertEquals("ALL_NOT_ADMINISTERED", PortfolioFilterType.ALL_NOT_ADMINISTERED.getValue());
        assertEquals("ALL_ADVISED", PortfolioFilterType.ALL_ADVISED.getValue());
        assertEquals("NO_PORTFOLIO", PortfolioFilterType.NO_PORTFOLIO.getValue());
        assertEquals("REST", PortfolioFilterType.REST.getValue());
        assertEquals("ALL_REST", PortfolioFilterType.ALL_REST.getValue());
    }
}
