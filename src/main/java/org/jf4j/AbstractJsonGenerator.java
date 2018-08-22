/**
 *
 */
package org.jf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import javax.lang.model.util.Types;
import javax.xml.crypto.Data;

import org.jf4j.annotations.JsonString;
import org.jf4j.util.Utils;

public abstract class AbstractJsonGenerator implements JsonGenerator {

    private final Logger logger;

    /**
     * Constructor.
     */
    public AbstractJsonGenerator() {
        super();
        logger = Logger.getLogger();
    }

    @Override
    public final void serialize(Object value) throws JsonException {
        serialize(false, value);
    }

    private void serialize(boolean isJsonString, Object value) throws JsonException {
        if (value == null) {
            return;
        }
        Class<?> valueClass = value.getClass();
        if (Data.isNull(value)) {
            writeNull();
        } else if (value instanceof String) {
            writeString((String) value);
        } else if (value instanceof Number) {
            if (isJsonString) {
                writeString(value.toString());
            } else if (value instanceof BigDecimal) {
                writeNumber((BigDecimal) value);
            } else if (value instanceof BigInteger) {
                writeNumber((BigInteger) value);
            } else if (value instanceof Long) {
                writeNumber((Long) value);
            } else if (value instanceof Float) {
                float floatValue = ((Number) value).floatValue();
                Utils.checkArgument(logger, !Float.isInfinite(floatValue) && !Float.isNaN(floatValue),
                                    "Incorrect value for a float: " + floatValue);
                writeNumber(floatValue);
            } else if (value instanceof Integer || value instanceof Short || value instanceof Byte) {
                writeNumber(((Number) value).intValue());
            } else {
                double doubleValue = ((Number) value).doubleValue();
                Utils.checkArgument(logger, !Double.isInfinite(doubleValue) && !Double.isNaN(doubleValue),
                                    "Incorrect value for a double: " + doubleValue);
                writeNumber(doubleValue);
            }
        } else if (value instanceof Boolean) {
            writeBoolean((Boolean) value);
        } else if (value instanceof DateTime) {
            writeString(((DateTime) value).toStringRfc3339());
        } else if (value instanceof Iterable<?> || valueClass.isArray()) {
            writeStartArray();
            for (Object o : Types.iterableOf(value)) {
                serialize(isJsonString, o);
            }
            writeEndArray();
        } else if (valueClass.isEnum()) {
            String name = FieldInfo.of((Enum<?>) value).getName();
            if (name == null) {
                writeNull();
            } else {
                writeString(name);
            }
        } else {
            writeStartObject();
            // only inspect fields of POJO (possibly extends GenericData) but not generic Map
            boolean isMapNotGenericData = value instanceof Map<?, ?> && !(value instanceof GenericData);
            ClassInfo classInfo = isMapNotGenericData ? null : ClassInfo.of(valueClass);
            for (Map.Entry<String, Object> entry : Data.mapOf(value).entrySet()) {
                Object fieldValue = entry.getValue();
                if (fieldValue != null) {
                    String fieldName = entry.getKey();
                    boolean isJsonStringForField;
                    if (isMapNotGenericData) {
                        isJsonStringForField = isJsonString;
                    } else {
                        Field field = classInfo.getField(fieldName);
                        isJsonStringForField = field != null && field.getAnnotation(JsonString.class) != null;
                    }
                    writeFieldName(fieldName);
                    serialize(isJsonStringForField, fieldValue);
                }
            }
            writeEndObject();
        }
    }

}
