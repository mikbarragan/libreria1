package es.santander.adn360.core.web;

import com.santander.darwin.security.authentication.AuthenticationBearerToken;
import com.santander.darwin.security.authentication.service.TokenService;
import com.santander.darwin.security.authentication.token.DefaultToken;
import com.santander.darwin.security.authentication.token.Token;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;



class WebUtilsTest {

    @Autowired
    private TokenService<String, Token> tokenConverter;

    private static final String TOKEN_JWT_TEST = "eyJraWQiOiJTVFNTYW5CY2VDZXJ0VjEiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9" +
            ".eyJzdWIiOiJ1aWQ6bjExMTExIiwibmJmIjoxNjQ5MDg4MzE1LCJpc3MiOiJTVFNTYW5CY2VDZXJ0VjEiLCJleHAiOjE2NDkwOTIyMTUsImlhdCI6MTY0OTA4ODYxNSwianRpIjoiMThmZjllODItM2MxNy00YTI3LWI0YmEtMWEzNTc3ZTFkYzQ0In0.L02VCvKshCD44wdxLM_YX_sm_9FqSH70aPZgsyMJWoOyrPOjtnuJeDsSDGzclZVU9m8B6h9XLOyQRTcV0iBA2pFPAsLQZIdXuDyd2L_YBSM13OBMfqnxuYy_1cchrh62MJBhwjN1Pf1oxcYEfbYmLOPEEwOPNmKgaPkXyfhJ9J7mmFDeuPJb61FeB2FeYxUO63pQnfFOsM_bFBBHZ2FCRoknI1KCO7EasEn13OI4qxrv6RYwOR8GD5-wMh2nOuamaYlKEG1Gr_KxOc97QIvewonkV18r_n8sv0yypkqb6efzjQEHev35x9VtS_LGtB9Z1qEClmN-KtvPqrHArRJSGw";
    private static final String TOKEN_JWT_NOUSERTID_TEST ="eyJraWQiOiJTVFNTYW5CY2VDZXJ0VjEiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9" +
            ".eyJuYmYiOjE2NDkwODgzMTUsImlzcyI6IlNUU1NhbkJjZUNlcnRWMSIsImV4cCI6MTY0OTA5MjIxNSwiaWF0IjoxNjQ5MDg4NjE1LCJqdGkiOiIxOGZmOWU4Mi0zYzE3LTRhMjctYjRiYS0xYTM1NzdlMWRjNDQifQ.L02VCvKshCD44wdxLM_YX_sm_9FqSH70aPZgsyMJWoOyrPOjtnuJeDsSDGzclZVU9m8B6h9XLOyQRTcV0iBA2pFPAsLQZIdXuDyd2L_YBSM13OBMfqnxuYy_1cchrh62MJBhwjN1Pf1oxcYEfbYmLOPEEwOPNmKgaPkXyfhJ9J7mmFDeuPJb61FeB2FeYxUO63pQnfFOsM_bFBBHZ2FCRoknI1KCO7EasEn13OI4qxrv6RYwOR8GD5-wMh2nOuamaYlKEG1Gr_KxOc97QIvewonkV18r_n8sv0yypkqb6efzjQEHev35x9VtS_LGtB9Z1qEClmN-KtvPqrHArRJSGw";
    @BeforeEach
    void setUp()  {
        SecurityContextHolder.setContext(new CoreSecurityContextImpl(SecurityContextHolder
                .getContext().getAuthentication(), "OFI", "A000111"));
    }

    @Test
    void getSantanderChannel() {

        Assertions.assertThat(WebUtils.getSantanderChannel()).isEqualTo("OFI");
    }

    @Test
    void getInternalUser() {
        Assertions.assertThat(WebUtils.getInternalUser()).isEqualTo("A000111");
    }

    @Test
    void getTokenUser() {
        Assertions.assertThat(WebUtils.getTokenUser()).isNullOrEmpty();
    }

    @Test
    void nullpointerControl() {
        // Set test authentication
        Token jwtTokenTest = DefaultToken.builder().createBksOrJwtToken(TOKEN_JWT_NOUSERTID_TEST).build();
        AuthenticationBearerToken auth = new AuthenticationBearerToken(jwtTokenTest, AuthorityUtils.NO_AUTHORITIES);
        auth.setDetails(null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Assertions.assertThat(WebUtils.getTokenUser()).isNull();

    }

    @Test
    void incorrectUserId() {
        // Set test authentication
        Token jwtTokenTest = DefaultToken.builder().createBksOrJwtToken(TOKEN_JWT_TEST).build();
        AuthenticationBearerToken auth = new AuthenticationBearerToken(jwtTokenTest, AuthorityUtils.NO_AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Assertions.assertThat(WebUtils.getTokenUser()).isEqualTo("n11111");

    }

    @Test void channelUtilsCheck() {

        SecurityContextHolder.setContext(new CoreSecurityContextImpl(SecurityContextHolder
                .getContext().getAuthentication(), "OFI", "A000111"));

        Assertions.assertThat(WebUtils.isOfiChannel()).isTrue();
        Assertions.assertThat(WebUtils.isEmpChannel()).isFalse();
        Assertions.assertThat(WebUtils.isRmlChannel()).isFalse();

        SecurityContextHolder.setContext(new CoreSecurityContextImpl(SecurityContextHolder
                .getContext().getAuthentication(), "EMP", "A000111"));

        Assertions.assertThat(WebUtils.isEmpChannel()).isTrue();
        Assertions.assertThat(WebUtils.isOfiChannel()).isFalse();
        Assertions.assertThat(WebUtils.isRmlChannel()).isFalse();

        SecurityContextHolder.setContext(new CoreSecurityContextImpl(SecurityContextHolder
                .getContext().getAuthentication(), "RML", "A000111"));

        Assertions.assertThat(WebUtils.isRmlChannel()).isTrue();
        Assertions.assertThat(WebUtils.isEmpChannel()).isFalse();
        Assertions.assertThat(WebUtils.isOfiChannel()).isFalse();

        SecurityContextHolder.setContext(new CoreSecurityContextImpl(SecurityContextHolder
                .getContext().getAuthentication(), "OTR", "A000111"));

        Assertions.assertThat(WebUtils.isEmpChannel()).isFalse();
        Assertions.assertThat(WebUtils.isOfiChannel()).isFalse();
        Assertions.assertThat(WebUtils.isRmlChannel()).isFalse();

        SecurityContextHolder.setContext(new CoreSecurityContextImpl(SecurityContextHolder
                .getContext().getAuthentication(), "INT", "A000111"));

        Assertions.assertThat(WebUtils.isIntChannel()).isTrue();
        Assertions.assertThat(WebUtils.isOfiChannel()).isFalse();
        Assertions.assertThat(WebUtils.isRmlChannel()).isFalse();
    }

    @Test void checkInputChannel() {
        SecurityContextHolder.setContext(new CoreSecurityContextImpl(SecurityContextHolder
                .getContext().getAuthentication(), "OFI", "A000111"));

        Assertions.assertThat(WebUtils.isChannel("OFI")).isTrue();
        Assertions.assertThat(WebUtils.isChannel("EMP")).isFalse();
    }
}