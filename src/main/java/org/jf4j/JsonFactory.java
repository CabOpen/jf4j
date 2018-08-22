package org.jf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * https://www.oracle.com/technetwork/articles/java/json-1973242.html
 *
 */
public interface JsonFactory {

    /**
     * Creates a JSON parser from a byte stream. The character encoding of the stream is determined as specified in
     * <a href="http://tools.ietf.org/rfc/rfc7159.txt">RFC 7159</a>.
     *
     * @param in
     *            input stream
     * @return a JSON parser
     */
    JsonParser createReader(InputStream in) throws JsonException;

    /**
     * Creates a JSON parser from a byte stream.
     *
     * @param in
     *            input stream
     * @param charset
     *            charset in which the input stream is encoded or {@code null} to let the parser detect the charset. In
     *            this case, it behaves like {@link #createReader(InputStream)}.
     * @return a JSON parser
     */
    JsonParser createReader(InputStream in, Charset charset) throws JsonException;

    /**
     * Creates a JSON parser for the given string value.
     *
     * @param value
     *            string value
     * @return a JSON parser
     */
    JsonParser createReader(String value) throws JsonException;

    /**
     * Creates a JSON parser from a character stream.
     *
     * @param reader
     *            reader
     * @return a JSON parser
     */
    JsonParser createReader(Reader reader) throws JsonException;

    /**
     * Creates a JSON generator for writing JSON to a byte stream.
     *
     * @param out
     *            i/o stream to which JSON is written
     * @return a JSON generator
     */
    JsonGenerator createGenerator(OutputStream out);

    /**
     * Creates a JSON generator for writing JSON to the given output stream and encoding.
     *
     * @param out
     *            output stream
     * @param encoding
     *            encoding
     * @return a JSON generator
     */
    JsonGenerator createGenerator(OutputStream out, Charset encoding) throws JsonException;

    /**
     * Creates a JSON generator for writing JSON to a character stream..
     *
     * @param writer
     *            a i/o writer to which JSON is written
     * @return a JSON generator
     */
    JsonGenerator createGenerator(Writer writer) throws JsonException;

    /**
     * Creates an object parser which uses this factory to parse JSON data.
     *
     */
    JsonObjectReader createObjectParser();

    /**
     * Returns a serialized JSON string representation for the given item using {@link JsonGenerator#serialize(Object)}.
     *
     * @param item
     *            data key/value pairs
     * @return serialized JSON string representation
     */
    String toString(Object item) throws JsonException;

    /**
     * Returns a pretty-printed serialized JSON string representation for the given item using
     * {@link JsonGenerator#serialize(Object)} with {@link JsonGenerator#enablePrettyPrint()}.
     *
     * <p>
     * The specifics of how the JSON representation is made pretty is implementation dependent, and should not be relied
     * on. However, it is assumed to be legal, and in fact differs from {@link #toString(Object)} only by adding
     * whitespace that does not change its meaning.
     * </p>
     *
     * @param item
     *            data key/value pairs
     * @return serialized JSON string representation
     */
    String toPrettyString(Object item) throws JsonException;

    /**
     * Returns a UTF-8 encoded byte array of the serialized JSON representation for the given item using
     * {@link JsonGenerator#serialize(Object)}.
     *
     * @param item
     *            data key/value pairs
     * @return byte array of the serialized JSON representation
     *
     */
    byte[] toByteArray(Object item) throws JsonException;

    /**
     * Parses a string value as a JSON object, array, or value into a new instance of the given destination class using
     * {@link JsonParser#parse(Class)}.
     *
     * @param value
     *            JSON string value
     * @param destinationClass
     *            destination class that has an accessible default constructor to use to create a new instance
     * @return new instance of the parsed destination class
     */
    <T> T fromString(String value, Class<T> destinationClass) throws JsonException;

    /**
     * Parse and close an input stream as a JSON object, array, or value into a new instance of the given destination
     * class using {@link JsonParser#parseAndClose(Class)}.
     *
     * <p>
     * Tries to detect the charset of the input stream automatically.
     * </p>
     *
     * @param inputStream
     *            JSON value in an input stream
     * @param destinationClass
     *            destination class that has an accessible default constructor to use to create a new instance
     * @return new instance of the parsed destination class
     *
     */
    <T> T fromInputStream(InputStream inputStream, Class<T> destinationClass) throws JsonException;

    /**
     * Parse and close an input stream as a JSON object, array, or value into a new instance of the given destination
     * class using {@link JsonParser#parseAndClose(Class)}.
     *
     * @param inputStream
     *            JSON value in an input stream
     * @param charset
     *            Charset in which the stream is encoded
     * @param destinationClass
     *            destination class that has an accessible default constructor to use to create a new instance
     * @return new instance of the parsed destination class
     */
    <T> T fromInputStream(InputStream inputStream, Charset charset, Class<T> destinationClass) throws JsonException;

    /**
     * Parse and close a reader as a JSON object, array, or value into a new instance of the given destination class
     * using {@link JsonParser#parseAndClose(Class)}.
     *
     * @param reader
     *            JSON value in a reader
     * @param destinationClass
     *            destination class that has an accessible default constructor to use to create a new instance
     * @return new instance of the parsed destination class
     */
    <T> T fromReader(Reader reader, Class<T> destinationClass) throws JsonException;

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    String toString();

    String description();

}
