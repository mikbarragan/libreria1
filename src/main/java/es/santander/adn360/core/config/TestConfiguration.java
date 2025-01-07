package es.santander.adn360.core.config;

import com.santander.darwin.security.authentication.AuthenticationBearerToken;
import com.santander.darwin.security.authentication.token.DefaultToken;
import com.santander.darwin.security.authentication.token.Token;
import es.santander.adn360.core.web.CoreSecurityContextImpl;
import jakarta.annotation.PostConstruct;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Tests configuration. Includes mock methods for security context.
 * Test profile should be included in test configuration if you want to use this configuration:
 *      spring.profiles.active: test
 */
@Configuration
@EnableWebSecurity
@ConditionalOnProperty(value = "darwin.security.enabled", havingValue = "false")
public class TestConfiguration {

    /**
     * Mock implementation of the CoreSecurityContext interface.
     */
    static final CoreSecurityContextImpl CORE_SECURITY_CONTEXT = Mockito.mock(CoreSecurityContextImpl.class);
    /**
     * Mock implementation of the AuthenticationBearerToken interface.
     */
    static final AuthenticationBearerToken JWT_AUTHENTICATION_TOKEN = Mockito.mock(AuthenticationBearerToken.class);


    /**
     * Customizer for the web security configuration.
     * @return WebSecurityCustomizer
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.debug(false)
                .ignoring()
                .requestMatchers(new AntPathRequestMatcher("/**"));
    }


    /**
     * The init context
     */
    @PostConstruct
    public void init() {
        // Mock Santander channel and JWTDetails by default for tests.
        Mockito.when(CORE_SECURITY_CONTEXT.getAuthentication()).thenReturn(JWT_AUTHENTICATION_TOKEN);
        mockSantanderChannel("OFI");
        mockInternalUser("TestInternalUser");
        Token jwtTokenTest = DefaultToken.builder().createBksOrJwtToken("eyJraWQiOiJTVFNTYW5CY2VDZXJ0VjEiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9" +
                ".eyJzdWIiOiJ1aWQ6bjExMTExIiwibmJmIjoxNjQ5MDg4MzE1LCJpc3MiOiJTVFNTYW5CY2VDZXJ0VjEiLCJleHAiOjE2NDkwOTIyMTUsImlhdCI6MTY0OTA4ODYxNSwianRpIjoiMThmZjllODItM2MxNy00YTI3LWI0YmEtMWEzNTc3ZTFkYzQ0In0.L02VCvKshCD44wdxLM_YX_sm_9FqSH70aPZgsyMJWoOyrPOjtnuJeDsSDGzclZVU9m8B6h9XLOyQRTcV0iBA2pFPAsLQZIdXuDyd2L_YBSM13OBMfqnxuYy_1cchrh62MJBhwjN1Pf1oxcYEfbYmLOPEEwOPNmKgaPkXyfhJ9J7mmFDeuPJb61FeB2FeYxUO63pQnfFOsM_bFBBHZ2FCRoknI1KCO7EasEn13OI4qxrv6RYwOR8GD5-wMh2nOuamaYlKEG1Gr_KxOc97QIvewonkV18r_n8sv0yypkqb6efzjQEHev35x9VtS_LGtB9Z1qEClmN-KtvPqrHArRJSGw").build();
        AuthenticationBearerToken jwtAuthentcationToken = new AuthenticationBearerToken(jwtTokenTest, AuthorityUtils.NO_AUTHORITIES);
        mockJWTDetails(jwtAuthentcationToken);
        SecurityContextHolder.setContext(CORE_SECURITY_CONTEXT);

    }

    /**
     * Mocks Santander channel in security context.
     *
     * @param santanderChannel Santander channel
     */
    public static void mockSantanderChannel(String santanderChannel) {
        Mockito.when(CORE_SECURITY_CONTEXT.getSantanderChannel()).thenReturn(santanderChannel);
    }

    /**
     * Mocks JWT details in security context.
     *
     * @param jwtDetails DarwinUserDetails or AuthenticationParameters subclass
     */
    public static void mockJWTDetails(AuthenticationBearerToken jwtDetails) {
        Mockito.when(CORE_SECURITY_CONTEXT.getAuthentication().getDetails()).thenReturn(jwtDetails);
    }

    /**
     * Mocks Internal user in security context.
     *
     * @param internalUser Internal User
     */
    public static void mockInternalUser(String internalUser) {
        Mockito.when(CORE_SECURITY_CONTEXT.getInternalUser()).thenReturn(internalUser);
    }

}
