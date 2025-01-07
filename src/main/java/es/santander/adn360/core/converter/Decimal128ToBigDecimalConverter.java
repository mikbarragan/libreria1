package es.santander.adn360.core.converter;

import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * MongoDB converter used to map
 * Decimal128 fields to BigDecimal.
 * There are problems when serializing
 * and deserializing Decimal128 fields.
 */
public class Decimal128ToBigDecimalConverter implements Converter<Decimal128, BigDecimal> {

    @Override
    public BigDecimal convert(final Decimal128 decimal128) {

        return Optional.ofNullable(decimal128)
                .map(Decimal128::toString)
                .map(Double::parseDouble)
                .map(BigDecimal::valueOf)
                .orElse(null);
    }
}
