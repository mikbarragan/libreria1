package es.santander.adn360.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


/**
 * The
 * Core
 * Properties
 * data
 * class
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "core")
@PropertySource("classpath:/core.properties")
@RefreshScope
public class CoreProperties {

    private final Services services = new Services();
    private final InternalUsers internalUsers = new InternalUsers();
    private Boolean channelHeaderValidation;

    /**
     * The static service
     */
    @Data
    public static class Services {

        private final StsService stsService = new StsService();

    }

    /**
     * The static sts
     */
    @Data
    public static class StsService {
        private Integer cacheMaximumSize;
        private Integer cacheExpireAfterMinutes;
    }

    /**
     * The statis users
     */
    @Data
    public static class InternalUsers {
        private String cacheSpec;
    }

}
