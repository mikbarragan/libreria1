package es.santander.adn360.core.model.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * The field class
 */
@Data
@Builder
public class Field implements Serializable {

    /**
     * Generated serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

	/**
     * Field name
     */
    @Schema(description = "Field name")
    private String name;

    /**
     * nameToFullAggregate: optional model field create for full aggregation (ADN client)
     */
    @Schema(description = "Optional model field create for full aggregation (ADN client)")
    private String[] nameToFullAggregate;

    /**
     * Operation
     */
    @Schema(description = "Operation")
    private String operation;

    /**
     * Field order
     */
    @Schema(description = "Field order")
    private Integer order;

    /**
     * Title
     */
    @Schema(description = "Title")
    private String title;

}
