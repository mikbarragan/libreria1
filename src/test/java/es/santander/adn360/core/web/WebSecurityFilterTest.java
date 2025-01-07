package es.santander.adn360.core.web;

import com.santander.darwin.security.authentication.AuthenticationBearerToken;
import com.santander.darwin.security.authentication.service.TokenService;
import com.santander.darwin.security.authentication.token.DefaultToken;
import com.santander.darwin.security.authentication.token.Token;
import es.santander.adn360.core.config.CoreProperties;
import es.santander.adn360.core.model.document.repository.InternalUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.IOException;
import java.io.PrintWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureObservability
class WebSecurityFilterTest {

    private final HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
    private final HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
    private final FilterChain filterChain = Mockito.mock(FilterChain.class);

    @Autowired
    private WebSecurityFilter webSecurityFilter;

    @Autowired
    private CoreProperties coreProperties;

    @MockBean
    private InternalUserRepository internalUserRepository;

    private static Integer httpServletResponseStatus;

    @MockBean
    private TokenService<String, Token> tokenConverter;

    private static final String TOKEN_JWT_TEST = "eyJraWQiOiJTVFNTYW5CY2VDZXJ0VjEiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9" +
            ".eyJzdWIiOiJ1aWQ6bjExMTExIiwibmJmIjoxNjQ5MDg4MzE1LCJpc3MiOiJTVFNTYW5CY2VDZXJ0VjEiLCJleHAiOjE2NDkwOTIyMTUsImlhdCI6MTY0OTA4ODYxNSwianRpIjoiMThmZjllODItM2MxNy00YTI3LWI0YmEtMWEzNTc3ZTFkYzQ0In0.L02VCvKshCD44wdxLM_YX_sm_9FqSH70aPZgsyMJWoOyrPOjtnuJeDsSDGzclZVU9m8B6h9XLOyQRTcV0iBA2pFPAsLQZIdXuDyd2L_YBSM13OBMfqnxuYy_1cchrh62MJBhwjN1Pf1oxcYEfbYmLOPEEwOPNmKgaPkXyfhJ9J7mmFDeuPJb61FeB2FeYxUO63pQnfFOsM_bFBBHZ2FCRoknI1KCO7EasEn13OI4qxrv6RYwOR8GD5-wMh2nOuamaYlKEG1Gr_KxOc97QIvewonkV18r_n8sv0yypkqb6efzjQEHev35x9VtS_LGtB9Z1qEClmN-KtvPqrHArRJSGw";


    @BeforeEach
    void setUp() throws IOException {
        this.coreProperties.setChannelHeaderValidation(true);
        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            httpServletResponseStatus = invocationOnMock.getArgument(0);
            return null;
        }).when(this.httpServletResponse).setStatus(anyInt());
        when(this.httpServletResponse.getWriter()).thenReturn(Mockito.mock(PrintWriter.class));
        // Set test authentication
        Token jwtTokenTest = DefaultToken.builder().createBksOrJwtToken(TOKEN_JWT_TEST).build();
        AuthenticationBearerToken auth = new AuthenticationBearerToken(jwtTokenTest, AuthorityUtils.NO_AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @WithMockUser
    void testDoFilterInternalNoChannelHeader() throws ServletException, IOException {
        this.webSecurityFilter.doFilterInternal(this.httpServletRequest, this.httpServletResponse, this.filterChain);
        assertThat(httpServletResponseStatus).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void testDoFilterInternalOK() throws ServletException, IOException {
        when(this.httpServletRequest.getHeader(WebSecurityFilter.HEADER_CHANNEL))
                .thenReturn("testChannel");

        this.webSecurityFilter.doFilterInternal(this.httpServletRequest, this.httpServletResponse, this.filterChain);
        assertThat(httpServletResponseStatus).isNull();
    }

    @Test
    @WithAnonymousUser
    void testAnonymousUserEndpoint() throws ServletException, IOException {
        when(this.httpServletRequest.getHeader(WebSecurityFilter.HEADER_CHANNEL))
                .thenReturn("testChannel");

        this.webSecurityFilter.doFilterInternal(this.httpServletRequest, this.httpServletResponse, this.filterChain);
        assertThat(httpServletResponseStatus).isNull();
    }

    @Test
    @WithMockUser
    void testSecuredEndpoint() throws ServletException, IOException {

        when(this.httpServletRequest.getHeader(WebSecurityFilter.HEADER_CHANNEL))
                .thenReturn("testChannel");

        this.webSecurityFilter.doFilterInternal(this.httpServletRequest, this.httpServletResponse, this.filterChain);
        assertThat(httpServletResponseStatus).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    void testErrorOnSubjectRepo() throws ServletException, IOException {

        //throw exception on repository access
        when(this.internalUserRepository.findOneByIdUsuario(isA(String.class))).thenThrow(new RuntimeException("whatever"));

        when(this.httpServletRequest.getHeader(WebSecurityFilter.HEADER_CHANNEL))
                .thenReturn(WebUtils.SANTANDER_CHANNEL_EMP);

        this.webSecurityFilter.doFilterInternal(this.httpServletRequest, this.httpServletResponse, this.filterChain);

        assertThat(((CoreSecurityContextImpl)SecurityContextHolder.getContext()).getInternalUser()).isNullOrEmpty();
        assertThat(((CoreSecurityContextImpl)SecurityContextHolder.getContext()).getSantanderChannel()).isEqualTo(WebUtils.SANTANDER_CHANNEL_EMP);


    }


}
