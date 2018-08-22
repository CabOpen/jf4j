package org.jf4j;

import org.jf4j.spi.JsonServiceProviderHolder;

public class Json {
    private static final JsonFactory factory = JsonServiceProviderHolder.get();

}
