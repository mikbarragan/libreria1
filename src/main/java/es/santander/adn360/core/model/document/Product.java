package es.santander.adn360.core.model.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * The
 * Product
 * class
 */
@Data
@Builder
public class Product implements Serializable {

	/**
	 * Generated serial version UID
	 */
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Product type
	 */
	@Schema(description = "Product type", example = "506")
    private String type;

	/**
	 * Product subtype
	 */
	@Schema(description = "Product subtype", example = "610")
    private String subType;

	/**
	 * Attribute
	 */
	@Schema(description = "Attribute", example = "credit")
    private String attribute;

	/**
	 * Product description
	 */
	@Schema(description = "Product description")
    private String description;

}
