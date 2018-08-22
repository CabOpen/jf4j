package org.jf4j;

import org.slf4j.LoggerFactory;

public class Logger {
    private static final String PROJECT_NAME = "JF4J";
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PROJECT_NAME);

    public static Logger getLogger() {
        return LoggerHolder.INSTANCE;
    }

    public Logger() {
        super();
    }

    public void logInfo(String message) {
        LOGGER.info(message);
    }

    public void logDebug(String message) {
        LOGGER.debug(message);
    }

    public void logError(String message) {
        LOGGER.error(message);
    }

    /**
     * Logs an error.
     *
     * @param message
     *            error message.
     * @param exception
     *            exception causing the error.
     */
    public void logError(String message, Throwable exception) {
        final String exceptionString = exception == null ? "Unknown Reason"
                                                         : exception.getMessage() == null ? "An exception ["
                                                                                            + exception.toString()
                                                                                            + "] was raised"
                                                                                          : exception.getMessage()
                                                                                            + (exception.getCause() == null ? ""
                                                                                                                            : ". Cause: "
                                                                                                                              + exception.getCause());
        LOGGER.error(message + ". Reason: " + exceptionString);
    }

    public void logWarn(String message) {
        LOGGER.warn(message);
    }

    /**
     * Logs a warning.
     *
     * @param message
     *            warning message.
     * @param exception
     *            exception causing the warning.
     */
    public void logWarn(String message, Throwable exception) {
        final String exceptionString = exception == null ? "Unknown Reason"
                                                         : exception.getMessage() == null ? "An exception ["
                                                                                            + exception.toString()
                                                                                            + "] was raised"
                                                                                          : exception.getMessage()
                                                                                            + (exception.getCause() == null ? ""
                                                                                                                            : ". Cause: "
                                                                                                                              + exception.getCause());
        LOGGER.warn(message + ". Reason: " + exceptionString);
    }

    public void throwSdkException(Exception exception) throws JsonException {
        throwCloudException(exception instanceof JsonException ? (JsonException) exception
                                                               : new JsonException(exception));
    }

    public void throwSdkException(String message, Exception cause) throws JsonException {
        throwCloudException(new JsonException(message, cause));
    }

    public void throwSdkException(String message) throws JsonException {
        throwCloudException(new JsonException(message));
    }

    private void throwCloudException(JsonException exception) throws JsonException {
        logError(exception.getMessage(), exception.getCause());
        throw exception;
    }

    private static class LoggerHolder {
        public static final Logger INSTANCE = new Logger();
    }
}
