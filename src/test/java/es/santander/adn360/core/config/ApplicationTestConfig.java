package es.santander.adn360.core.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


/**
 * SpringBootApplication for testing.
 *
 * @author Javier Moreno
 */
@Configuration
@EnableAspectJAutoProxy
@EnableCaching
class ApplicationTestConfig {


    /**
     *  Bean of CacheManager
     *
     * @return CacheManager
     */
    @Bean
    CacheManagerCustomizer<CaffeineCacheManager> exampleCacheCustomization(Ticker ticker) {
        return cacheManager -> cacheManager.registerCustomCache("test",
                Caffeine.newBuilder()
                        .maximumSize(100)
                        .ticker(ticker)
                        .build());
    }

    /**
     *  Bean of Ticker
     *
     * @return Ticker
     */
    @Bean
    Ticker ticker() {
        return Ticker.systemTicker();
    }

    /**
     *  Bean of String
     *
     * @return String
     */
    @Bean
    String mongoExchangeCollectionName() {
        return "currencies";
    }

    /**
     *  Bean of String
     *
     * @return String
     */
    @Bean
    String mongoGlobalPositionCollectionName() {
        return "global_positions";
    }

    /**
     *  Bean of String
     *
     * @return String
     */
    @Bean
    String mongoProductConfigurationCollectionName() {
        return "product_configurations";
    }



}
