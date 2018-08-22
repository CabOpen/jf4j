package org.jf4j;

import java.io.Closeable;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Set;

import dagger.internal.Beta;

/**
 * Provides forward, read-only access to JSON data in a streaming way.
 * <p>
 * This is the most efficient way for reading JSON data. This is the only way to parse and process JSON data that are
 * too big to be loaded in memory.
 */
public interface JsonParser extends Closeable {

    /**
     * Returns the JSON factory from which this generator was created.
     */
    public JsonFactory getFactory();

    /**
     * Parse a JSON object, array, or value into a new instance of the given destination class.
     *
     * <p>
     * If it parses an object, after this method ends, the current token will be the object's ending
     * {@link JsonToken#END_OBJECT}. If it parses an array, after this method ends, the current token will be the
     * array's ending {@link JsonToken#END_ARRAY}.
     * </p>
     *
     * @param <T>
     *            destination class
     * @param destinationClass
     *            destination class that has a public default constructor to use to create a new instance
     * @return new instance of the parsed destination class
     */
    <T> T parse(Class<T> destinationClass) throws JsonException;

    /**
     * Returns the next token from the stream or {@code null} to indicate end of input.
     */
    JsonToken nextToken() throws JsonException;

    /**
     * Returns the token the parser currently points to or {@code null} for none (at start of input or after end of
     * input).
     */
    JsonToken getCurrentToken();

    /**
     * Returns the most recent field name or {@code null} for array values or for root-level values.
     */
    String getCurrentName() throws JsonException;

    /**
     * Skips to the matching {@link JsonToken#END_ARRAY} if current token is {@link JsonToken#START_ARRAY}, the matching
     * {@link JsonToken#END_OBJECT} if the current token is {@link JsonToken#START_OBJECT}, else does nothing.
     */
    JsonParser skipChildren() throws JsonException;

    /**
     * Returns a textual representation of the current token or {@code null} if {@link #getCurrentToken()} is
     * {@code null}.
     */
    String getText() throws JsonException;

    /**
     * Returns the byte value of the current token.
     */
    byte getByteValue() throws JsonException;

    /**
     * Returns the short value of the current token.
     */
    short getShortValue() throws JsonException;

    /**
     * Returns the int value of the current token.
     */
    int getIntValue() throws JsonException;

    /**
     * Returns the float value of the current token.
     */
    float getFloatValue() throws JsonException;

    /**
     * Returns the long value of the current token.
     */
    long getLongValue() throws JsonException;

    /**
     * Returns the double value of the current token.
     */
    double getDoubleValue() throws JsonException;

    /**
     * Returns the {@link BigInteger} value of the current token.
     */
    BigInteger getBigIntegerValue() throws JsonException;

    /**
     * Returns the {@link BigDecimal} value of the current token.
     */
    BigDecimal getDecimalValue() throws JsonException;

    /**
     * Skips the values of all keys in the current object until it finds the given key.
     *
     * <p>
     * Before this method is called, the parser must either point to the start or end of a JSON object or to a field
     * name. After this method ends, the current token will either be the {@link JsonToken#END_OBJECT} of the current
     * object if the key is not found, or the value of the key that was found.
     * </p>
     *
     * @param keyToFind
     *            key to find
     */
    void skipToKey(String keyToFind) throws JsonException;

    /**
     * Skips the values of all keys in the current object until it finds one of the given keys.
     *
     * <p>
     * Before this method is called, the parser must either point to the start or end of a JSON object or to a field
     * name. After this method ends, the current token will either be the {@link JsonToken#END_OBJECT} of the current
     * object if no matching key is found, or the value of the key that was found.
     * </p>
     *
     * @param keysToFind
     *            set of keys to look for
     * @return name of the first matching key found or {@code null} if no match was found
     * @since 1.10
     */
    String skipToKey(Set<String> keysToFind) throws JsonException;

    /**
     * {@link Beta} <br/>
     * Parse a JSON object, array, or value into a new instance of the given destination class, optionally using the
     * given parser customizer.
     *
     * <p>
     * If it parses an object, after this method ends, the current token will be the object's ending
     * {@link JsonToken#END_OBJECT}. If it parses an array, after this method ends, the current token will be the
     * array's ending {@link JsonToken#END_ARRAY}.
     * </p>
     *
     * @param <T>
     *            destination class
     * @param destinationClass
     *            destination class that has a public default constructor to use to create a new instance
     * @param customizeParser
     *            optional parser customizer or {@code null} for none
     * @return new instance of the parsed destination class
     */
    <T> T parse(Class<T> destinationClass, CustomizeJsonParser customizeParser) throws JsonException;

    /**
     * Parse a JSON object, array, or value into a new instance of the given destination class.
     *
     * <p>
     * If it parses an object, after this method ends, the current token will be the object's ending
     * {@link JsonToken#END_OBJECT}. If it parses an array, after this method ends, the current token will be the
     * array's ending {@link JsonToken#END_ARRAY}.
     * </p>
     *
     * @param dataType
     *            Type into which the JSON should be parsed
     * @param close
     *            {@code true} if {@link #close()} should be called after parsing
     * @return new instance of the parsed dataType
     * @since 1.15
     */
    Object parse(Type dataType, boolean close) throws JsonException;

    /**
     * {@link Beta} <br/>
     * Parse a JSON object, array, or value into a new instance of the given destination class, optionally using the
     * given parser customizer.
     *
     * <p>
     * If it parses an object, after this method ends, the current token will be the object's ending
     * {@link JsonToken#END_OBJECT}. If it parses an array, after this method ends, the current token will be the
     * array's ending {@link JsonToken#END_ARRAY}.
     * </p>
     *
     * @param dataType
     *            Type into which the JSON should be parsed
     * @param close
     *            {@code true} if {@link #close()} should be called after parsing
     * @param customizeParser
     *            optional parser customizer or {@code null} for none
     * @return new instance of the parsed dataType
     * @since 1.10
     */
    Object parse(Type dataType, boolean close, CustomizeJsonParser customizeParser) throws JsonException;

    /**
     * Parse a JSON object from the given JSON parser into the given destination object.
     *
     * <p>
     * Before this method is called, the parser must either point to the start or end of a JSON object or to a field
     * name. After this method ends, the current token will be the {@link JsonToken#END_OBJECT} of the current object.
     * </p>
     *
     * @param destination
     *            destination object
     * @since 1.15
     */
    void parse(Object destination) throws JsonException;

    /**
     * {@link Beta} <br/>
     * Parse a JSON object from the given JSON parser into the given destination object, optionally using the given
     * parser customizer.
     *
     * <p>
     * Before this method is called, the parser must either point to the start or end of a JSON object or to a field
     * name. After this method ends, the current token will be the {@link JsonToken#END_OBJECT} of the current object.
     * </p>
     *
     * @param destination
     *            destination object
     * @param customizeParser
     *            optional parser customizer or {@code null} for none
     */
    void parse(Object destination, CustomizeJsonParser customizeParser) throws JsonException;

    /**
     * Parse a JSON Array from the given JSON parser into the given destination collection.
     *
     * @param destinationCollectionClass
     *            class of destination collection (must have a public default constructor)
     * @param destinationItemClass
     *            class of destination collection item (must have a public default constructor)
     * @since 1.15
     */
    <T> Collection<T> parseArray(Class<?> destinationCollectionClass,
                                 Class<T> destinationItemClass) throws JsonException;

    /**
     * {@link Beta} <br/>
     * Parse a JSON Array from the given JSON parser into the given destination collection, optionally using the given
     * parser customizer.
     *
     * @param destinationCollectionClass
     *            class of destination collection (must have a public default constructor)
     * @param destinationItemClass
     *            class of destination collection item (must have a public default constructor)
     * @param customizeParser
     *            optional parser customizer or {@code null} for none
     */

    <T> Collection<T> parseArray(Class<?> destinationCollectionClass, Class<T> destinationItemClass,
                                 CustomizeJsonParser customizeParser) throws JsonException;

    /**
     * Parse a JSON Array from the given JSON parser into the given destination collection.
     *
     * @param destinationCollection
     *            destination collection
     * @param destinationItemClass
     *            class of destination collection item (must have a public default constructor)
     * @since 1.15
     */
    <T> void parseArray(Collection<? super T> destinationCollection,
                        Class<T> destinationItemClass) throws JsonException;

    /**
     * {@link Beta} <br/>
     * Parse a JSON Array from the given JSON parser into the given destination collection, optionally using the given
     * parser customizer.
     *
     * @param destinationCollection
     *            destination collection
     * @param destinationItemClass
     *            class of destination collection item (must have a public default constructor)
     * @param customizeParser
     *            optional parser customizer or {@code null} for none
     */

    <T> void parseArray(Collection<? super T> destinationCollection, Class<T> destinationItemClass,
                        CustomizeJsonParser customizeParser) throws JsonException;

}
