package es.santander.adn360.core.model.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * The
 * Internal
 * user
 * class
 */
@Data
@Builder
@Document(collection = "#{@mongoProperties.getInternalUserCollection()}")
public class InternalUser implements Serializable {
    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1;
	/**
	 * Identifier
	 */
    @Id
    private String id;

	/**
	 * Internal user identifier
	 */
    private String idUsuario;

    /**
     * Internal user
     */
    private String usuarioInterno;

    /**
     * Logical deletion date
     */
    private Date fechaBajaLogica;
}
