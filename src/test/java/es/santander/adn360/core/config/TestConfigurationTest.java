package es.santander.adn360.core.config;

import com.santander.darwin.security.authentication.AuthenticationBearerToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestConfigurationTest {

    @Test
    void testMockSantanderChannel() {
        String expectedChannel = "OFI";
        TestConfiguration.mockSantanderChannel(expectedChannel);
        assertEquals(expectedChannel, TestConfiguration.CORE_SECURITY_CONTEXT.getSantanderChannel());
    }

    @Test
    void testMockJWTDetails() {
        // Create a mock AuthenticationBearerToken
        AuthenticationBearerToken jwtDetails = mock(AuthenticationBearerToken.class);

        // Mock the getAuthentication method to return an Authentication object
        Authentication auth = mock(Authentication.class);
        when(TestConfiguration.CORE_SECURITY_CONTEXT.getAuthentication()).thenReturn(auth);

        // Mock the getDetails method to return jwtDetails
        when(auth.getDetails()).thenReturn(jwtDetails);

        // Call the method under test
        TestConfiguration.mockJWTDetails(jwtDetails);

        // Assert that the getDetails method returns the expected value
        assertEquals(jwtDetails, auth.getDetails());
    }

    @Test
    void testMockInternalUser() {
        String expectedUser = "TestInternalUser";

        // Mock the getInternalUser method to return expectedUser
        when(TestConfiguration.CORE_SECURITY_CONTEXT.getInternalUser()).thenReturn(expectedUser);

        // Call the method under test
        TestConfiguration.mockInternalUser(expectedUser);

        // Assert that the getInternalUser method returns the expected value
        assertEquals(expectedUser, TestConfiguration.CORE_SECURITY_CONTEXT.getInternalUser());
    }
}
