package org.jf4j;

/**
 * JSON token.
 */
public enum JsonToken {

    /** Start of a JSON array ('['). */
    START_ARRAY("Start of a JSON array ('[')"),

    /** End of a JSON array (']'). */
    END_ARRAY("End of a JSON array (']')"),

    /** Start of a JSON object ('{'). */
    START_OBJECT("Start of a JSON object ('{')"),

    /** End of a JSON object ('}'). */
    END_OBJECT("End of a JSON object ('}')"),

    /** JSON field name. */
    FIELD_NAME("JSON field name"),

    /** JSON field string value. */
    VALUE_STRING("JSON field string value"),

    /**
     * JSON field number value of an integer with an arbitrary number of digits and no fractional part.
     */
    VALUE_NUMBER_INT("JSON field number value of an integer"),

    /** JSON field number value of an arbitrary-precision decimal number. */
    VALUE_NUMBER_FLOAT("JSON field number value of a float"),

    /** JSON field {@code true} value. */
    VALUE_TRUE("JSON field true value"),

    /** JSON field {@code false} value. */
    VALUE_FALSE("JSON field false value"),

    /** JSON {@code null}. */
    VALUE_NULL("JSON null"),

    /** Some other token. */
    NOT_AVAILABLE("other token");

    private final String description;

    /**
     * Constructor.
     *
     * @param description
     *            description
     */
    private JsonToken(String description) {
        this.description = description;
    }

    /**
     * Gets string representation.
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return description;
    }

}
