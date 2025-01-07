package es.santander.adn360.core.config;

import org.junit.jupiter.api.Test;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;

import static org.wildfly.common.Assert.assertNotNull;
import static org.wildfly.common.Assert.assertTrue;

@SpringBootTest
@AutoConfigureObservability
class AopConfigurationTest {

    @Autowired
    private AopConfiguration aopConfiguration;

    @Test
    void performanceMonitorInterceptor() {
        CorePerformanceMonitorInterceptor interceptor = aopConfiguration.performanceMonitorInterceptor();
        assertNotNull(interceptor);
    }

    @Test
    void performanceMonitorAdvisor() {
        Advisor advisor = aopConfiguration.performanceMonitorAdvisor();
        assertNotNull(advisor);
        assertTrue(advisor.getAdvice() instanceof CorePerformanceMonitorInterceptor);
    }
}