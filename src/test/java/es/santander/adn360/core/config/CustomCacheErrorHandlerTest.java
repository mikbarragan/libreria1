package es.santander.adn360.core.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class CustomCacheErrorHandlerTest {

    @Mock
    private Cache cache;

    private CustomCacheErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new CustomCacheErrorHandler();
    }

    @Test
    void testHandleCacheGetError() {
        RuntimeException exception = new RuntimeException("Test exception");
        assertDoesNotThrow(() -> errorHandler.handleCacheGetError(exception, cache, "testKey"));
    }

    @Test
    void testHandleCachePutError() {
        RuntimeException exception = new RuntimeException("Test exception");
        assertDoesNotThrow(() -> errorHandler.handleCachePutError(exception, cache, "testKey", "testValue"));
    }

    @Test
    void testHandleCacheEvictError() {
        RuntimeException exception = new RuntimeException("Test exception");
        assertDoesNotThrow(() -> errorHandler.handleCacheEvictError(exception, cache, "testKey"));
    }

    @Test
    void testHandleCacheClearError() {
        RuntimeException exception = new RuntimeException("Test exception");
        assertDoesNotThrow(() -> errorHandler.handleCacheClearError(exception, cache));
    }
}
