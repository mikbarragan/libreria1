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
 * Global
 * position
 * class
 * collection
 * name
 */
@Data
@Builder
@Document(collection = "#{mongoGlobalPositionCollectionName}")
public class GlobalPosition implements Serializable {
	@Serial
	private static final long serialVersionUID = 1;
	/**
	 * Position identifier
	 */
	@Schema(description = "Position identifier", required = true)
    @Id
    private String id;

	/**
	 * Position channel
	 */
	@Schema(description = "Position channel", example = "OFI", required = true)
    private String channel;

	/**
	 * Position application
	 */
	@Schema(description = "Position application", example = "ADN360")
    private String application;

	/**
	 * Segment
	 */
	@Schema(description = "Segment", example = "OFI")
    private String segment;

	/**
	 * List of product groups.
	 */
	@Schema(description = "List of product groups")
    private List<ProductGroup> productGroups;
}
