package org.jf4j;

import java.io.Closeable;
import java.io.Flushable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Object in charge of writing JSON data to an output source in a streaming way.
 * <p>
 * The generated JSON text must strictly conform to the grammar defined in
 * <a href="http://www.ietf.org/rfc/rfc7159.txt">RFC 7159</a>.
 */
public interface JsonGenerator extends Flushable, Closeable {
    /**
     * Returns the JSON factory from which this generator was created.
     */
    JsonFactory getFactory();

    /**
     * Serializes the given JSON value object, or if {@code value} is {@code null} it does no serialization.
     * 
     * @throws JsonException
     */
    void serialize(Object item) throws JsonException;

    /**
     * Requests that the output be pretty printed (by default it is not).
     */
    void enablePrettyPrint() throws JsonException;

    /**
     * Writes a JSON start array character '['.
     * <p>
     * It starts a new child array context within which JSON values can be written to the array. This method is valid
     * only in an array context, field context or in no context (when a context is not yet started). This method can
     * only be called once in no context.
     */
    void writeStartArray() throws JsonException;

    /**
     * Writes a JSON end array character ']'.
     */
    void writeEndArray() throws JsonException;

    /**
     * Writes a JSON start object character '{'.
     * <p>
     * It starts a new child object context within which JSON name/value pairs can be written to the object. This method
     * is valid only in an array context, field context or in no context (when a context is not yet started). This
     * method can only be called once in no context.
     */
    void writeStartObject() throws JsonException;

    /**
     * Writes a JSON end object character '}'.
     */
    void writeEndObject() throws JsonException;

    /**
     * Writes a JSON quoted field name.
     */
    void writeFieldName(String name) throws JsonException;

    /**
     * Writes a JSON null value within the current array, field or root context.
     */
    void writeNull() throws JsonException;

    /**
     * Writes a JSON quoted string value.
     */
    void writeString(String value) throws JsonException;

    /**
     * Writes a literal JSON boolean value ('true' or 'false').
     */
    void writeBoolean(boolean state) throws JsonException;

    /**
     * Writes a JSON int value.
     */
    void writeNumber(int v) throws JsonException;

    /**
     * Writes a JSON long value.
     */
    void writeNumber(long v) throws JsonException;

    /**
     * Writes a JSON big integer value.
     */
    void writeNumber(BigInteger v) throws JsonException;

    /**
     * Writes a JSON float value.
     */
    void writeNumber(float v) throws JsonException;

    /**
     * Writes a JSON double value.
     */
    void writeNumber(double v) throws JsonException;

    /**
     * Writes a JSON big decimal value.
     */
    void writeNumber(BigDecimal v) throws JsonException;

    /**
     * Writes a JSON numeric value that has already been encoded properly.
     */
    void writeNumber(String encodedValue) throws JsonException;

}
