package es.santander.adn360.core.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * AOP core configuration. It includes performance monitor
 * interceptor to get time traces in microservices.
 */
@Configuration
@EnableAspectJAutoProxy
@Aspect
public class AopConfiguration {

    /**
     * Este método es para cargar el point cut
     */
    @Pointcut(
            "execution(* (@org.springframework.web.bind.annotation.RestController *).*(..)) || " +
            "execution(* (@org.springframework.stereotype.Service *).*(..)) || " +
            "execution(* (@org.springframework.stereotype.Repository *).*(..))"
    )
    public void monitor() {
        // Do nothing because of X and Y.
    }

    /**
     * Get the performance interceptor with dynamic logger.
     * @return performance interceptor
     */
    @Bean
    public CorePerformanceMonitorInterceptor performanceMonitorInterceptor() {
        return new CorePerformanceMonitorInterceptor(true);
    }


    /**
     * Get the performance monitor advisor.
     * @return default pointcut advisor
     */
    @Bean
    public Advisor performanceMonitorAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("es.santander.adn360.core.config.AopConfiguration.monitor()");
        return new DefaultPointcutAdvisor(pointcut, this.performanceMonitorInterceptor());
    }
}
