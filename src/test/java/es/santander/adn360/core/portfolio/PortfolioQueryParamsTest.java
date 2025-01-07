package es.santander.adn360.core.portfolio;

import es.santander.adn360.core.model.portfolio.PortfolioFilterType;
import es.santander.adn360.core.model.portfolio.PortfolioQueryParams;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PortfolioQueryParamsTest {

    private PortfolioQueryParams queryParams;

    @Test
    void testSetDefaultValues() {
        queryParams = PortfolioQueryParams.builder().build();
        queryParams.setDefaultValues();

        assertTrue(queryParams.getRelated_to_portfolio_types().isEmpty());
        assertEquals(Set.of(PortfolioFilterType.MANAGED), queryParams.getUnrelated_to_portfolio_types());

        queryParams = PortfolioQueryParams.builder()
                .related_to_portfolio_types(Set.of(PortfolioFilterType.ADVISED))
                .unrelated_to_portfolio_types(Set.of(PortfolioFilterType.NOT_ADMINISTERED))
                .build();
        queryParams.setDefaultValues();

        assertEquals(Set.of(PortfolioFilterType.ADVISED), queryParams.getRelated_to_portfolio_types());
        assertEquals(Set.of(PortfolioFilterType.NOT_ADMINISTERED), queryParams.getUnrelated_to_portfolio_types());
    }

    @Test
    void testIsEmpty() {
        queryParams = PortfolioQueryParams.builder().build();
        assertTrue(queryParams.isEmpty());

        queryParams = PortfolioQueryParams.builder()
                .related_to_portfolio_types(Set.of(PortfolioFilterType.ADVISED))
                .unrelated_to_portfolio_types(Set.of(PortfolioFilterType.NOT_ADMINISTERED))
                .build();
        assertFalse(queryParams.isEmpty());
    }

    @Test
    void testToMap() {
        var emptyQueryParams = PortfolioQueryParams.builder().build();
        MultiValueMap<String, PortfolioFilterType> emptyMap = new LinkedMultiValueMap<>();
        assertTrue(mapsAreEqual(emptyMap, emptyQueryParams.toMap()));

        MultiValueMap<String, PortfolioFilterType> mapWithRelated = new LinkedMultiValueMap<>();
        mapWithRelated.addAll("related_to_portfolio_types",
                List.of(PortfolioFilterType.ADVISED, PortfolioFilterType.MANAGED));
        var queryParamsWithRelated = PortfolioQueryParams.builder()
                .related_to_portfolio_types(Set.of(PortfolioFilterType.ADVISED, PortfolioFilterType.MANAGED))
                .build();
        assertTrue(mapsAreEqual(mapWithRelated, queryParamsWithRelated.toMap()));

        MultiValueMap<String, PortfolioFilterType> mapWithUnrelated = new LinkedMultiValueMap<>();
        mapWithUnrelated.addAll("unrelated_to_portfolio_types",
                List.of(PortfolioFilterType.ADVISED, PortfolioFilterType.MANAGED));
        var queryParamsWithUnrelated = PortfolioQueryParams.builder()
                .unrelated_to_portfolio_types(Set.of(PortfolioFilterType.ADVISED, PortfolioFilterType.MANAGED))
                .build();
        assertTrue(mapsAreEqual(mapWithUnrelated, queryParamsWithUnrelated.toMap()));

        MultiValueMap<String, PortfolioFilterType> mapWithBothFilled = new LinkedMultiValueMap<>();
        mapWithBothFilled.addAll("related_to_portfolio_types",
                List.of(PortfolioFilterType.NO_PORTFOLIO));
        mapWithBothFilled.addAll("unrelated_to_portfolio_types",
                List.of(PortfolioFilterType.ADVISED, PortfolioFilterType.MANAGED));
        var queryParamsWithBothFilled = PortfolioQueryParams.builder()
                .related_to_portfolio_types(Set.of(PortfolioFilterType.NO_PORTFOLIO))
                .unrelated_to_portfolio_types(Set.of(PortfolioFilterType.ADVISED, PortfolioFilterType.MANAGED))
                .build();
        assertTrue(mapsAreEqual(mapWithBothFilled, queryParamsWithBothFilled.toMap()));

    }

    private boolean mapsAreEqual(MultiValueMap<String, PortfolioFilterType> map1, MultiValueMap<String, PortfolioFilterType> map2) {
        if (map1.size() != map2.size()) {
            return false;
        }
        for (String key : map1.keySet()) {
            if (!map1.get(key).containsAll(map2.get(key))) {
                return false;
            }
        }
        return true;
    }
}
