package es.santander.adn360.core.converter;

import com.mongodb.lang.NonNull;
import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.math.BigDecimal;

/**
 * MongoDB converter used to convert Double fields to Decimal128.
 */
@WritingConverter
public class DoubleDecimal128Converter implements Converter<Double, Decimal128> {

    /**
     * Double Decimal128 Converter
     * @param source double
     *
     * @return decimal128
     */
    @Override
    public Decimal128 convert(@NonNull Double source) {
        return new Decimal128(BigDecimal.valueOf(source));
    }
}
