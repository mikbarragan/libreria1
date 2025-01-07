package es.santander.adn360.core.util.swagger;

/**
 * Swagger Sample Constants
 */
public class SwaggerSampleConstants {
    /**
     * INTERNAL ERROR
     */
    public static final String INTERNAL_ERROR = "{\"httpCode\": 500, " +
            "\"httpMessage\":\"Internal server error\", " +
            "\"moreInformation\":\"More information message\"}";
    /**
     * INVALID PARAMETERS
     */
    public static final String INVALID_PARAMETERS = "{\"httpCode\": 400, " +
            "\"httpMessage\":\"Invalid parameters\", " +
            "\"moreInformation\":\"More information message\"}";
    /**
     * FORBIDDEN
     */
    public static final String FORBIDDEN = "{\"httpCode\": 403, " +
            "\"httpMessage\":\"Call not authorized\", " +
            "\"moreInformation\":\"More information message\"}";
    /**
     * UNAUTHORIZED
     */
    public static final String UNAUTHORIZED =
            "{\"appName\": \"adn360-app\", " +
            "\"timeStamp\": 1624600059833, " +
            "\"errorName\": \"UNAUTHORIZED\", " +
            "\"status\": 401, " +
            "\"internalCode\": 401, " +
            "\"shortMessage\": \"UNAUTHORIZED\", " +
            "\"detailedMessage\": \"Invalid credentials\"}";

    private SwaggerSampleConstants(){}
}
