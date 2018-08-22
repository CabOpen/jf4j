package org.jf4j;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Default custom parser.
 * <p>
 * Any custom parser should extend this class and override the methods that need customisation.
 *
 */
public class AbstractCustomizeJsonParser implements CustomizeJsonParser {

    /*
     * (non-Javadoc)
     *
     * @see org.jf4j.CustomizeJsonParser#stopAt(java.lang.Object, java.lang.String)
     */
    @Override
    public boolean stopAt(Object context, String key) {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jf4j.CustomizeJsonParser#handleUnrecognizedKey(java.lang.Object, java.lang.String)
     */
    @Override
    public void handleUnrecognizedKey(Object context, String key) {
        // Nothing to do
    }

    /**
     * newInstanceForArray.
     *
     * @see org.jf4j.CustomizeJsonParser#newInstanceForArray(java.lang.Object, java.lang.reflect.Field)
     * @return by default {@code null}
     */
    @Override
    public Collection<Object> newInstanceForArray(Object context, Field field) {
        return null;
    }

    /**
     * newInstanceForObject.
     *
     * @see org.jf4j.CustomizeJsonParser#newInstanceForObject(java.lang.Object, java.lang.Class)
     * @return by default {@code null}
     */
    @Override
    public Object newInstanceForObject(Object context, Class<?> fieldClass) {
        return null;
    }

}
