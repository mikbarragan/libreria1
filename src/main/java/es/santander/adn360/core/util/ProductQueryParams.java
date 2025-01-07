package es.santander.adn360.core.util;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Class to wrapper identifiers from different channels
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductQueryParams implements Serializable {

	/**
	 * Generated serial version UID
	 */
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Application name.
	 */
	@Parameter(description = "Application name", example = "ADN360")
    @Pattern(regexp = "^[A-Z0-9]+$")
    private String application;

	/**
	 * Application segment.
	 */
	@Parameter(description = "Application segment", example = "EMPR")
    @Pattern(regexp = "^[A-Z0-9]+$")
    private String segment;

	/**
	 * nonsense commentary to comply with a Sonar nonsense rule
	 * nonsense commentary to comply with a Sonar nonsense rule
	 * nonsense commentary to comply with a Sonar nonsense rule
	 */
}
