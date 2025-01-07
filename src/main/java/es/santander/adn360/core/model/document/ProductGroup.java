package es.santander.adn360.core.model.document;

import com.fasterxml.jackson.annotation.JsonView;
import es.santander.adn360.core.util.ProductGroupViews;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The
 * product
 * group
 * class
 * The
 * product
 * group
 * class
 *
 */
@Data
@Builder(toBuilder = true)
public class ProductGroup implements Serializable {

    /**
     * Generated serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Included contracts in filter
     */
    public static final String FILTER_OB_INCLUDED_CONTRACTS = "obIncludedContracts";

    /**
     * Excluded contracts in filter
     */
    public static final String FILTER_OB_EXCLUDED_CONTRACTS = "obExcludedContracts";

    /**
     * Included or excluded and intervener filter
     */
    public static final String FILTER_OB_INCLUDED_OR_EXCLUDED_AND_INTERVENER = "obIncludeOrExcludedAndIntervener";

    /**
     * Product group identifier
     */
    @Schema(description = "Product group identifier", example = "TARJETAS")
    @JsonView(ProductGroupViews.Basic.class)
    private String id;

    /**
     * Title
     */
    @Schema(description = "Title")
    @JsonView(ProductGroupViews.Basic.class)
    private String title;

    /**
     * List of product groups
     */
    @Schema(description = "List of product groups")
    @JsonView(ProductGroupViews.Basic.class)
    private List<ProductGroup> productGroups;

    /**
     * Links
     */
    @Schema(description = "List of links")
    @JsonView(ProductGroupViews.Basic.class)
    private List<Link> links;

    /**
     * Product group agreggation
     */
    @Schema(description = "Product group agreggation")
    @JsonView(ProductGroupViews.Full.class)
    private Aggregation aggregation;

    /**
     * List of filters
     */
    @Schema(description = "List of filters")
    @JsonView(ProductGroupViews.Full.class)
    private List<String> filters;

    /**
     * List of excluded status
     */
    @Schema(description = "List of excluded status")
    @JsonView(ProductGroupViews.Full.class)
    private List<String> excludedStatus;

    /**
     * List of valid status
     */
    @Schema(description = "List of valid status")
    @JsonView(ProductGroupViews.Full.class)
    private List<String> validStatus;

    /**
     * If applies 25 months
     */
    @Schema(description = "If applies 25 months", allowableValues = {"true","false"})
    @JsonView(ProductGroupViews.Basic.class)
    private Boolean apply25Months;

    /**
     * Shown in contingency
     */
    @Schema(description = "Shown in contingency", allowableValues = {"true","false"})
    @JsonView(ProductGroupViews.Basic.class)
    private Boolean shownInContingency;

    /**
     * If it is contingency
     */
    @Schema(description = "If it is contingency", allowableValues = {"true","false"})
    @JsonView(ProductGroupViews.Basic.class)
    private Boolean contingency;

    /**
     * If it is last RTU update
     */
    @Schema(description = "If it is last RTU update", allowableValues = {"true","false"})
    @JsonView(ProductGroupViews.Basic.class)
    private String lastRTUpdate;


    /**
     * Gets an stream of
     * product groups from
     * all children product
     * groups
     *
     * @return stream prodcut groups
     */
    public Stream<ProductGroup> flattenedProductGroups() {

        Stream<ProductGroup> spg = Optional.ofNullable(productGroups)
                .map(pg -> productGroups.stream().flatMap(ProductGroup::flattenedProductGroups))
                .orElseGet(Stream::empty);

        return Stream.concat(
                Stream.of(this),
                spg
        );
    }

}
