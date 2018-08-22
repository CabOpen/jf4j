package org.jf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.jf4j.util.Utils;

/**
 * Abstract Json Factory.
 * <p>
 * Any Json Factory implementation should extend this class.
 *
 */
public abstract class AbstractJsonFactory implements JsonFactory {
    private static final String TAG_DESTINATION_CLASS = "destinationClass";
    protected final Logger logger;

    /**
     *
     */
    public AbstractJsonFactory() {
        super();
        logger = Logger.getLogger();
    }

    @Override
    public String toString(Object item) throws JsonException {
        return toString(item, false);
    }

    @Override
    public String toPrettyString(Object item) throws JsonException {
        return toString(item, true);
    }

    @Override
    public byte[] toByteArray(Object item) throws JsonException {
        return toByteStream(item, false).toByteArray();
    }

    @Override
    public <T> T fromString(String value, Class<T> destinationClass) throws JsonException {
        Utils.checkNotNull(logger, destinationClass, TAG_DESTINATION_CLASS);
        try (final JsonParser reader = createReader(value)) {
            return reader.parse(destinationClass);
        } catch (IOException exception) {
            throw new JsonException(exception);
        }
    }

    /**
     * Returns a serialised JSON string representation for the given item using {@link JsonGenerator#serialize(Object)}.
     *
     * @param item
     *            data key/value pairs
     * @param pretty
     *            whether to return a pretty representation
     * @return serialised JSON string representation
     */
    private String toString(Object item, boolean pretty) throws JsonException {
        try {
            return toByteStream(item, pretty).toString(StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException exception) {
            throw new JsonException(exception);
        }
    }

    /**
     * Returns a UTF-8 byte array output stream of the serialized JSON representation for the given item using
     * {@link JsonGenerator#serialize(Object)}.
     *
     * @param item
     *            data key/value pairs
     * @param pretty
     *            whether to return a pretty representation
     * @return serialized JSON string representation
     */
    @SuppressWarnings("resource")
    private ByteArrayOutputStream toByteStream(Object item, boolean pretty) throws JsonException {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            final JsonGenerator generator = createGenerator(byteStream, StandardCharsets.UTF_8);
            if (pretty) {
                generator.enablePrettyPrint();
            }
            generator.serialize(item);
            generator.flush();
            return byteStream;
        } catch (IOException exception) {
            throw new JsonException(exception);
        }
    }

}
