package org.jf4j;

import java.io.Closeable;
import java.util.Collection;

public interface JsonObjectReader extends Closeable {
    /** Returns the JSON factory from which this generator was created. */
    JsonFactory getFactory();

    <T> void parseArray(Collection<? super T> destinationCollection,
                        Class<T> destinationItemClass) throws JsonException;
}
