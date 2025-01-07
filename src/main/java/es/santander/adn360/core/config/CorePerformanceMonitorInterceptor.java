package es.santander.adn360.core.config;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RestController;

/**
 * Performance
 * monitor
 * interceptor
 * used
 * to
 * log
 * repositories,
 * services
 * and
 * controllers
 * running
 * times.
 */
@Slf4j
public class CorePerformanceMonitorInterceptor extends PerformanceMonitorInterceptor {

	/**
	 * Generated serial version UID
	 */
	private static final long serialVersionUID = 6527958119579556440L;

	/**
	 * Trace message template
	 */
	private static final String TRACE_MESSAGE = "Monitoring %s method %s, running time = %s ms, %s s.";

	/**
	 * Class type for repositories
	 */
    private static final String CLASS_TYPE_REPOSITORY = "Repository";

	/**
	 * Class type for services
	 */
    private static final String CLASS_TYPE_SERVICE = "Service";


	/**
	 * Class type for controllers
	 */
    private static final String CLASS_TYPE_CONTROLLER = "Controller";

    /**
     * Create a new PerformanceMonitorInterceptor with a dynamic or static logger, according to the given flag.
	 * @param useDynamicLogger whether to use a dynamic logger or a static logger
     */
    CorePerformanceMonitorInterceptor(boolean useDynamicLogger) {
        super(useDynamicLogger);
    }

    @Override
    protected Object invokeUnderTrace(
            final MethodInvocation invocation,
            final Log logger
    ) throws Throwable {


        final String name = this.createInvocationTraceName(invocation);
        final StopWatch stopWatch = new StopWatch(name);
        stopWatch.start(name);

        Object result;
        try {
            result = invocation.proceed();
        } finally {
            stopWatch.stop();
            // Do not log method calls with 0 time (for example cached methods)
            if (stopWatch.getTotalTimeMillis() > 0) {

                String classType;
                // Get class type

                if (invocation.getMethod().getDeclaringClass().getAnnotation(Repository.class) != null) {

                    classType = CLASS_TYPE_REPOSITORY;

                } else if (invocation.getMethod().getDeclaringClass().getAnnotation(Service.class) != null) {
                    classType = CLASS_TYPE_SERVICE;

                } else if (invocation.getMethod().getDeclaringClass().getAnnotation(Controller.class) != null
                        || invocation.getMethod().getDeclaringClass().getAnnotation(RestController.class) != null) {

                    classType = CLASS_TYPE_CONTROLLER;
                } else {
                    classType = null;
                }

                if (classType != null) {
                    log.trace(String.format(TRACE_MESSAGE, classType, stopWatch.getId(), stopWatch.getTotalTimeMillis(),
                            stopWatch.getTotalTimeSeconds()));
                }
            }
        }

        return result;
    }
}
