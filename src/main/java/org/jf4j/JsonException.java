package org.jf4j;

public class JsonException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -5481308898281835303L;

    public JsonException() {
        super();
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public JsonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * @param message
     * @param cause
     */
    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public JsonException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public JsonException(Throwable cause) {
        super(cause);
    }

}
