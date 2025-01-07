package es.santander.adn360.core.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The
 * Cache
 * Config
 * Class
 * support
 */
@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {

    /**
     * The Core properties
     */
    private final CoreProperties coreProperties;


    /**
     * Configs internal user cache manager
     *
     * @return internal user cache manager
     */
    @Bean
    CacheManagerCustomizer<CaffeineCacheManager> internalUserCacheManager() {
        return cacheManager -> {
            cacheManager.registerCustomCache("internal_users",
                    Caffeine.from(CaffeineSpec.parse(this.coreProperties.getInternalUsers().getCacheSpec())).build());
            cacheManager.registerCustomCache("reactive_internal_users",
                    Caffeine.from(CaffeineSpec.parse(this.coreProperties.getInternalUsers().getCacheSpec())).build());
        };
    }
}
