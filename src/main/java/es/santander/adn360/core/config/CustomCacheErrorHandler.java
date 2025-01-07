package es.santander.adn360.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

/**
 * The
 * Custom
 * Cache
 * Error
 * Handler
 * class
 * with
 * log
 */
@Slf4j
public class CustomCacheErrorHandler implements CacheErrorHandler {

    /**
     * Debug message.
     */
    private static final String DEBUG_INFO = "Debug information cache exception";

	@Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        log.error("Error getting data from cache " + exception.getMessage(),exception);
        log.debug(DEBUG_INFO, exception);
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        log.error("Error putting data into cache " + exception.getMessage());
        log.debug(DEBUG_INFO, exception);
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        log.error("Error evicting data from cache " + exception.getMessage());
        log.debug(DEBUG_INFO, exception);
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.error("Error clearing data from cache " + exception.getMessage());
        log.debug(DEBUG_INFO, exception);
    }

}
