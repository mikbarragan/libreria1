package es.santander.adn360.core.config;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CorePerformanceMonitorInterceptorTest {

    @Mock
    private MethodInvocation methodInvocation;

    private CorePerformanceMonitorInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new CorePerformanceMonitorInterceptor(false);
    }

    @Test
    void testInvokeUnderTrace() throws Throwable {
        when(methodInvocation.proceed()).thenReturn(null);
        when(methodInvocation.getMethod()).thenReturn(this.getClass().getDeclaredMethod("setUp"));

        interceptor.invokeUnderTrace(methodInvocation, null);

        verify(methodInvocation, times(1)).proceed();
    }
}
