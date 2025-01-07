package es.santander.adn360.core.model.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * The
 * product
 * conf
 * class
 * collection
 * name
 */
@Data
@Builder
@Document(collection = "#{mongoProductConfigurationCollectionName}")
public class ProductConfiguration implements Serializable {
	@Serial
	private static final long serialVersionUID = 1;
	/**
	 * Product identifier
	 */
	@Schema(description = "Product identifier")
    @Id
    private String id;


	/**
	 * Product channel
	 */
	@Schema(description = "Product channel")
    private String channel;

	/**
	 * Product application
	 */
	@Schema(description = "Product application")
    private String application;

	/**
	 * Product segment
	 */
	@Schema(description = "Product segment")
    private String segment;

	/**
	 * Product group identifier
	 */
	@Schema(description = "Product group identifier")
    private String productGroupId;

	/**
	 * List of products
	 */
	@Schema(description = "List of products")
    private List<Product> products;
}
