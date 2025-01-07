package es.santander.adn360.core;

import es.santander.adn360.core.config.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * SpringBootApplication for testing.
 *
 * @author Javier Moreno
 */
@SpringBootTest
@AutoConfigureObservability
class ApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TestConfiguration testConfiguration;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    void applicationTest() {
        assertDoesNotThrow(() -> ApplicationTestMock.main(new String[]{}));
    }

    @Test
    void configurationTest() {
        testConfiguration.init();
        assertNotNull(testConfiguration, "TestConfiguration should not be null after initialization");
    }
}
