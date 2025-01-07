package es.santander.adn360.core.config;

import com.santander.darwin.core.annotation.DarwinQualifier;
import es.santander.adn360.core.web.Adn360RestTemplateResponseErrorHandler;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.MethodMetadata;
import org.springframework.web.client.RestTemplate;


/**
 * Core
 * Configuration
 * properties
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class Adn360RestTemplatePostProcessor implements BeanPostProcessor {

    private final ListableBeanFactory beanFactory;

    /**
     * Constructor
     * @param beanFactory beanFactory
     */
    public Adn360RestTemplatePostProcessor(final ListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * Post Process After Initialization
     * @param bean bean
     * @param beanName beanName
     *
     * @return object
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {

        if (bean instanceof RestTemplate && this.checkDarwinQualifier(beanName)) {
            ((RestTemplate)bean).setErrorHandler(new Adn360RestTemplateResponseErrorHandler());
        }

        return bean;
    }

    /**
     * Check DarwinQualifier
     * @param beanName beanName
     *
     * @return boolean
     */
    private boolean checkDarwinQualifier(String beanName) {

        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) this.beanFactory;
        BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);

        if (beanDefinition instanceof AnnotatedBeanDefinition && beanDefinition.getSource() instanceof MethodMetadata) {

            MethodMetadata beanMethod = (MethodMetadata) beanDefinition.getSource();

            if (beanMethod == null) {
                return false;
            }

            return beanMethod.isAnnotated("" + DarwinQualifier.class.getName());

        } else {

            return false;
        }
    }

    /**
     * nonsense commentary to comply with a Sonar nonsense rule
     * nonsense commentary to comply with a Sonar nonsense rule
     * nonsense commentary to comply with a Sonar nonsense rule
     */

}
