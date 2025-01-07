package es.santander.adn360.core.model.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * The
 * Link
 * Class
 */
@Data
@Builder
public class Link implements Serializable {

	/**
	 * Generated serial version UID
	 */
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Relationship between the current resource and the linked one
	 */
	@Schema(description = "Relationship between the current resource and the linked one")
    private String rel;

	/**
	 * The referenced resource
	 */
	@Schema(description = "The referenced resource")
    private String ref;

}
