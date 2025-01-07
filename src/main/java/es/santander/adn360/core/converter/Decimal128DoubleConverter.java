package es.santander.adn360.core.converter;

import com.mongodb.lang.NonNull;
import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * MongoDB converter used to convert Decimal128 fields to Double.
 */
@ReadingConverter
public class Decimal128DoubleConverter implements Converter<Decimal128, Double> {

    /**
     * Decimal128 Double Converter
     * @param source decimal128
     *
     * @return double
     */
    @Override
    public Double convert(@NonNull Decimal128 source) {
        return source.bigDecimalValue().doubleValue();
    }

}
