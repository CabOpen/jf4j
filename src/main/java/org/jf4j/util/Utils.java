/**
 *
 */
package org.jf4j.util;

import org.jf4j.JsonException;
import org.jf4j.Logger;

public class Utils {
    /**
     * Ensures that a field is not null.
     *
     * @param logger
     *            logger to report an error if any
     * @param arg
     *            value of the argument
     * @param argName
     *            name of the argument
     * @throws JsonException
     *             if the field is null
     */
    public static void checkNotNull(Logger logger, Object arg, String argName) throws JsonException {
        if (arg == null) {
            logger.throwSdkException(new IllegalArgumentException("Argument [" + argName + "] cannot be Null"));
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression
     *            a boolean expression
     * @param errorMessage
     *            error message to include in the exception when the check fails
     * @throws JsonException
     *             if the check fails
     */
    public static void checkArgument(Logger logger, boolean expression, String errorMessage) throws JsonException {
        if (!expression) {
            logger.throwSdkException(new IllegalArgumentException(errorMessage));
        }
    }
}
