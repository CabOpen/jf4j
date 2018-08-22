package org.jf4j;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Customises the behaviour of a JSON parser.
 *
 */
public interface CustomizeJsonParser {
    /**
     * Returns whether to stop parsing at the given key of the given context object.
     */
    boolean stopAt(Object context, String key);

    /**
     * Called when the given unrecognised key is encountered in the given context object.
     */
    void handleUnrecognizedKey(Object context, String key);

    /**
     * Returns a new instance value for the given field in the given context object for a JSON array.
     */
    Collection<Object> newInstanceForArray(Object context, Field field);

    /**
     * Returns a new instance value for the given field class in the given context object for JSON Object.
     */
    Object newInstanceForObject(Object context, Class<?> fieldClass);
}
