package es.santander.adn360.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * The
 * Mongo
 * prop
 * class
 * with
 * properties
 * source
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
@PropertySource("classpath:/core.properties")
@RefreshScope
public class MongoProperties {

	/**
	 * Max connection idle time
	 */
	private Integer maxConnectionIdleTime;

	/**
	 * Max connection life time
	 */
	private Integer maxConnectionLifeTime;

	/**
	 * Max wait time
	 */
	private Integer maxWaitTime;

	/**
	 * Socket keep alive
	 */
	private Boolean socketKeepAlive;

	/**
	 * Connections per host
	 */
	private Integer connectionsPerHost;

	/**
	 * Min connections per host
	 */
	private Integer minConnectionsPerHost;

	/**
	 * Internal user collection
	 */
	private String internalUserCollection;

	/**
	 * Heartbeat connect timeout
	 */
	private Integer heartbeatConnectTimeout;

	/**
	 * Heartbeat frequency
	 */
	private Integer heartbeatFrequency;

	/**
	 * Heartbeat socket timeout
	 */
	private Integer heartbeatSocketTimeout;

	/**
	 * Min heart beat frequency
	 */
	private Integer minHeartbeatFrequency;

	/**
	 * Database name
	 */
	private String name;

	/**
	 * Connect Timeout
	 */
	private Integer connectTimeout;

	/**
	 * Read Timeout
	 */
	private Integer readTimeout;


}
