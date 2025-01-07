package es.santander.adn360.core.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class MongoConfigurationTest {

    @Mock
    private MongoProperties mongoProperties;

    private MongoConfiguration mongoConfiguration;

    @BeforeEach
    void setUp() {
        mongoConfiguration = new MongoConfiguration(mongoProperties);
    }

    @Test
    void testMongoDBDefaultSettings() {
        assertNotNull(mongoConfiguration.mongoDBDefaultSettings());
    }

    @Test
    void testCustomConversions() {
        MongoCustomConversions conversions = mongoConfiguration.customConversions();
        assertNotNull(conversions);
    }
}
