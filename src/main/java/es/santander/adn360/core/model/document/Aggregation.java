package es.santander.adn360.core.model.document;

import java.io.Serializable;
import java.io.Serial;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * The
 * Aggregation
 * class
 */
@Data
@Builder
public class Aggregation implements Serializable {
	/**
	 * Generated serial version UID
	 */
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Service
	 */
	@Schema(description = "Aggregation service")
    private String service;

	/**
	 * Field list
	 */
	@Schema(description = "Field list")
    private List<Field> fields;
}
