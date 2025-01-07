package es.santander.adn360.core.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The
 * Exchange
 * class
 * with
 * id
 * divisa
 * origen
 * destino
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "#{mongoExchangeCollectionName}")
public class Exchange implements Serializable {

    /**
     * Generated serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * Id
     */
    @Id
    private String id;
    /**
     * Divisa Origen
     */
    private String divisaOrigen;
    /**
     * Divisa Destino
     */
    private String divisaDestino;
    /**
     * Cambio Fijo
     */
    private BigDecimal cambioFijo;
    /**
     * Fecha Cambio
     */
    private Date fechaCambio;
}
