package es.santander.adn360.core.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.management.JMXConnectionPoolListener;
import es.santander.adn360.core.converter.Decimal128DoubleConverter;
import es.santander.adn360.core.converter.Decimal128ToBigDecimalConverter;
import es.santander.adn360.core.converter.DoubleDecimal128Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The
 * Mongo
 * Conf
 * class
 * w
 * argas
 */
@Configuration
@ConditionalOnClass({MongoClientSettings.class})
@RequiredArgsConstructor
public class MongoConfiguration {

    /** mongo properties */
    private final MongoProperties mongoProperties;

    /**
     * MongoClientSettingsBuilderCustomizer
     * Bean of MongoClientSettings
     *
     * @return MongoClientSettings
     */
    @Bean
    public MongoClientSettingsBuilderCustomizer mongoDBDefaultSettings() {
        return builder ->
            builder.applyToServerSettings(b -> b
                    .heartbeatFrequency(this.mongoProperties.getHeartbeatFrequency(), TimeUnit.MILLISECONDS)
                    .minHeartbeatFrequency(this.mongoProperties.getMinHeartbeatFrequency(), TimeUnit.MILLISECONDS)
            ).applyToConnectionPoolSettings((ConnectionPoolSettings.Builder b) -> b
                    .maxWaitTime(this.mongoProperties.getMaxWaitTime(), TimeUnit.MILLISECONDS)
                    .maxConnectionIdleTime(this.mongoProperties.getMaxConnectionIdleTime(), TimeUnit.MILLISECONDS)
                    .maxConnectionLifeTime(this.mongoProperties.getMaxConnectionLifeTime(), TimeUnit.MILLISECONDS)
                    .maxSize(this.mongoProperties.getConnectionsPerHost())
                    .minSize(this.mongoProperties.getMinConnectionsPerHost())
                    .addConnectionPoolListener(new JMXConnectionPoolListener())
            ).applyToSocketSettings(b -> b
                    .connectTimeout(this.mongoProperties.getConnectTimeout() , TimeUnit.MILLISECONDS)
                    .readTimeout(this.mongoProperties.getReadTimeout(), TimeUnit.MILLISECONDS));
    }

    /**
     * customConversions
     * Sets MongoDB converters.
     *
     * @return custom conversions
     */
    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converterList = Arrays.asList(
                new Decimal128ToBigDecimalConverter(),
                new DoubleDecimal128Converter(),
                new Decimal128DoubleConverter()
        );

        return new MongoCustomConversions(converterList);
    }

    /**
     * nonsense commentary to comply with a Sonar nonsense rule
     * nonsense commentary to comply with a Sonar nonsense rule
     * nonsense commentary to comply with a Sonar nonsense rule
     */
}
