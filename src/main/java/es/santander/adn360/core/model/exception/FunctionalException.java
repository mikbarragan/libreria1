package es.santander.adn360.core.model.exception;

import es.santander.adn360.core.util.ExceptionEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

/**
 * RuntimeException: This exception will be throw in case of this is a
 * functional error
 *
 * @author Javier Moreno
 */
@ToString
public class FunctionalException extends RuntimeException {

    /**
     * Generated Serial Version ID
     */
    private static final long serialVersionUID = -4303575365387571184L;

    /**
     * Status and message info
     */
    @Schema(description = "Status and message info")
    @Getter
    private final ExceptionEnum info;

    /**
     * Error details
     */
    @Schema(description = "Error details")
    @Getter
    private final String moreInformation;

    /**
     * Functional exception
     * @param info enum information
     */
    public FunctionalException(ExceptionEnum info) {
        super();
        this.info = info;
        this.moreInformation = null;
    }

    /**
     * Functional exception
     * @param info  enum information
     * @param moreInformation   text with information
     */
    public FunctionalException(ExceptionEnum info, String moreInformation) {
        super();
        this.info = info;
        this.moreInformation = moreInformation;
    }

    /**
     * Functional exception
     * @param message   text with information
     * @param info  enum information
     */
    public FunctionalException(String message, ExceptionEnum info) {
        super(message);
        this.info = info;
        this.moreInformation = null;
    }

    /**
     * Functional exception
     * @param message text with information
     * @param info  enum information
     * @param moreInformation  extra text information
     */
    public FunctionalException(String message, ExceptionEnum info, String moreInformation) {
        super(message);
        this.info = info;
        this.moreInformation = moreInformation;
    }

    /**
     * Functional exception
     * @param cause cause of exception
     * @param info  enum information
     */
    public FunctionalException(Throwable cause, ExceptionEnum info) {
        super(cause);
        this.info = info;
        this.moreInformation = null;
    }

    /**
     * Function exception
     * @param cause cause of exception
     * @param info  enum information
     * @param moreInformation   extra text information
     */
    public FunctionalException(Throwable cause, ExceptionEnum info, String moreInformation) {
        super(cause);
        this.info = info;
        this.moreInformation = moreInformation;
    }

    /**
     * Functional exception
     * @param message   text information
     * @param cause     cause of exception
     * @param info      enum exception
     */
    public FunctionalException(String message, Throwable cause, ExceptionEnum info) {
        super(message, cause);
        this.info = info;
        this.moreInformation = null;
    }

    /**
     * Functional exception
     * @param message text information
     * @param cause     cause of exception
     * @param info      enum information
     * @param moreInformation   extra text information
     */
    public FunctionalException(String message, Throwable cause, ExceptionEnum info, String moreInformation) {
        super(message, cause);
        this.info = info;
        this.moreInformation = moreInformation;
    }

}
