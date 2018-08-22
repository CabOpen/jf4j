package org.jf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.crypto.Data;

import org.jf4j.util.Utils;

/**
 * Abstract JSON parser.
 * <P>
 * Any implementation of a JSON parser should implement this class.
 */
public abstract class AbstractJsonParser implements JsonParser {

    /**
     * Maps a polymorphic {@link Class} to its {@link Field} with the {@link JsonPolymorphicTypeMap} annotation, or
     * {@code null} if there is no field with that annotation.
     */
    private static WeakHashMap<Class<?>, Field> cachedTypemapFields = new WeakHashMap<>();

    /** Lock on the {@code cachedTypemapFields}. */
    private static final Lock lock = new ReentrantLock();

    protected final Logger logger;

    /**
     * Constructor.
     */
    public AbstractJsonParser() {
        super();
        logger = Logger.getLogger();
    }

    @Override
    public void skipToKey(String keyToFind) throws JsonException {
        skipToKey(Collections.singleton(keyToFind));
    }

    @Override
    public final String skipToKey(Set<String> keysToFind) throws JsonException {
        JsonToken curToken = startParsingObjectOrArray();
        while (curToken == JsonToken.FIELD_NAME) {
            String key = getText();
            nextToken();
            if (keysToFind.contains(key)) {
                return key;
            }
            skipChildren();
            curToken = nextToken();
        }
        return null;
    }

    /** Starts parsing that handles start of input by calling {@link #nextToken()}. */
    private JsonToken startParsing() throws JsonException {
        JsonToken currentToken = getCurrentToken();
        // token is null at start, so get next token
        if (currentToken == null) {
            currentToken = nextToken();
        }
        Utils.checkArgument(logger, currentToken != null, "no JSON input found");
        return currentToken;
    }

    /**
     * Starts parsing an object or array by making sure the parser points to an object field name, first array value or
     * end of object or array.
     *
     * <p>
     * If the parser is at the start of input, {@link #nextToken()} is called. The current token must then be
     * {@link JsonToken#START_OBJECT}, {@link JsonToken#END_OBJECT}, {@link JsonToken#START_ARRAY},
     * {@link JsonToken#END_ARRAY}, or {@link JsonToken#FIELD_NAME}. For an object only, after the method is called, the
     * current token must be either {@link JsonToken#FIELD_NAME} or {@link JsonToken#END_OBJECT}.
     * </p>
     */
    private JsonToken startParsingObjectOrArray() throws JsonException {
        JsonToken currentToken = startParsing();
        switch (currentToken) {
            case START_OBJECT:
                currentToken = nextToken();
                Utils.checkArgument(logger,
                                    currentToken == JsonToken.FIELD_NAME || currentToken == JsonToken.END_OBJECT,
                                    String.valueOf(currentToken));
                break;
            case START_ARRAY:
                currentToken = nextToken();
                break;
            default:
                break;
        }
        return currentToken;
    }

    @Override
    public <T> T parse(Class<T> destinationClass) throws JsonException {
        return parse(destinationClass, null);
    }

    @Override
    public final <T> T parse(Class<T> destinationClass, CustomizeJsonParser customizeParser) throws JsonException {
        @SuppressWarnings("unchecked")
        T result = (T) parse(destinationClass, false, customizeParser);
        return result;
    }

    @Override
    public Object parse(Type dataType, boolean close) throws JsonException {
        return parse(dataType, close, null);
    }

    @Override
    public Object parse(Type dataType, boolean close, CustomizeJsonParser customizeParser) throws JsonException {
        try {
            if (!Void.class.equals(dataType)) {
                startParsing();
            }
            return parseValue(null, dataType, new ArrayList<Type>(), null, customizeParser, true);
        } finally {
            if (close) {
                try {
                    close();
                } catch (IOException exception) {
                    throw new JsonException(exception);
                }
            }
        }
    }

    @Override
    public final void parse(Object destination) throws JsonException {
        parse(destination, null);
    }

    @Override
    public final void parse(Object destination, CustomizeJsonParser customizeParser) throws JsonException {
        ArrayList<Type> context = new ArrayList<>();
        context.add(destination.getClass());
        parse(context, destination, customizeParser);
    }

    /**
     * Parses the next field from the given JSON parser into the given destination object.
     *
     * @param context
     *            destination context stack (possibly empty)
     * @param destination
     *            destination object instance or {@code null} for none (for example empty context stack)
     * @param customizeParser
     *            optional parser customizer or {@code null} for none
     */
    private void parse(ArrayList<Type> context, Object destination,
                       CustomizeJsonParser customizeParser) throws JsonException {
        if (destination instanceof GenericJson) {
            ((GenericJson) destination).setFactory(getFactory());
        }
        JsonToken curToken = startParsingObjectOrArray();
        Class<?> destinationClass = destination.getClass();
        ClassInfo classInfo = ClassInfo.of(destinationClass);
        boolean isGenericData = GenericData.class.isAssignableFrom(destinationClass);
        if (!isGenericData && Map.class.isAssignableFrom(destinationClass)) {
            // The destination class is not a sub-class of GenericData but is of Map, so parse data
            // using parseMap.
            @SuppressWarnings("unchecked")
            Map<String, Object> destinationMap = (Map<String, Object>) destination;
            parseMap(null, destinationMap, Types.getMapValueParameter(destinationClass), context, customizeParser);
            return;
        }
        while (curToken == JsonToken.FIELD_NAME) {
            String key = getText();
            nextToken();
            // stop at items for feeds
            if (customizeParser != null && customizeParser.stopAt(destination, key)) {
                return;
            }
            // get the field from the type information
            FieldInfo fieldInfo = classInfo.getFieldInfo(key);
            if (fieldInfo != null) {
                // skip final fields
                if (fieldInfo.isFinal() && !fieldInfo.isPrimitive()) {
                    throw new IllegalArgumentException("final array/object fields are not supported");
                }
                Field field = fieldInfo.getField();
                int contextSize = context.size();
                context.add(field.getGenericType());
                Object fieldValue = parseValue(field, fieldInfo.getGenericType(), context, destination, customizeParser,
                                               true);
                context.remove(contextSize);
                fieldInfo.setValue(destination, fieldValue);
            } else if (isGenericData) {
                // store unknown field in generic JSON
                GenericData object = (GenericData) destination;
                object.set(key, parseValue(null, null, context, destination, customizeParser, true));
            } else {
                // unrecognized field, skip value.
                if (customizeParser != null) {
                    customizeParser.handleUnrecognizedKey(destination, key);
                }
                skipChildren();
            }
            curToken = nextToken();
        }
    }

    @Override
    public final <T> Collection<T> parseArray(Class<?> destinationCollectionClass,
                                              Class<T> destinationItemClass) throws JsonException {
        return parseArray(destinationCollectionClass, destinationItemClass, null);
    }

    @Override
    public final <T> Collection<T> parseArray(Class<?> destinationCollectionClass, Class<T> destinationItemClass,
                                              CustomizeJsonParser customizeParser) throws JsonException {
        @SuppressWarnings("unchecked")
        Collection<T> destinationCollection = (Collection<T>) Data.newCollectionInstance(destinationCollectionClass);
        parseArray(destinationCollection, destinationItemClass, customizeParser);
        return destinationCollection;
    }

    @Override
    public final <T> void parseArray(Collection<? super T> destinationCollection,
                                     Class<T> destinationItemClass) throws JsonException {
        parseArray(destinationCollection, destinationItemClass, null);
    }

    @Override
    public final <T> void parseArray(Collection<? super T> destinationCollection, Class<T> destinationItemClass,
                                     CustomizeJsonParser customizeParser) throws JsonException {
        parseArray(null, destinationCollection, destinationItemClass, new ArrayList<Type>(), customizeParser);
    }

    /**
     * Parses a JSON Array from the given JSON parser into the given destination collection, optionally using the given
     * parser customiser.
     *
     * @param fieldContext
     *            field context or {@code null} for none
     * @param destinationCollection
     *            destination collection
     * @param destinationItemType
     *            type of destination collection item
     * @param context
     *            destination context stack (possibly empty)
     * @param customizeParser
     *            optional parser customizer or {@code null} for none
     */
    private <T> void parseArray(Field fieldContext, Collection<T> destinationCollection, Type destinationItemType,
                                ArrayList<Type> context, CustomizeJsonParser customizeParser) throws JsonException {
        JsonToken curToken = startParsingObjectOrArray();
        while (curToken != JsonToken.END_ARRAY) {
            @SuppressWarnings("unchecked")
            T parsedValue = (T) parseValue(fieldContext, destinationItemType, context, destinationCollection,
                                           customizeParser, true);
            destinationCollection.add(parsedValue);
            curToken = nextToken();
        }
    }

    /**
     * Parses a JSON Object from the given JSON parser into the given destination map, optionally using the given parser
     * customiser.
     *
     * @param fieldContext
     *            field context or {@code null} for none
     * @param destinationMap
     *            destination map
     * @param valueType
     *            valueType of the map value type parameter
     * @param context
     *            destination context stack (possibly empty)
     * @param customizeParser
     *            optional parser customizer or {@code null} for none
     */
    private void parseMap(Field fieldContext, Map<String, Object> destinationMap, Type valueType,
                          ArrayList<Type> context, CustomizeJsonParser customizeParser) throws JsonException {
        JsonToken curToken = startParsingObjectOrArray();
        while (curToken == JsonToken.FIELD_NAME) {
            String key = getText();
            nextToken();
            // stop at items for feeds
            if (customizeParser != null && customizeParser.stopAt(destinationMap, key)) {
                return;
            }
            Object value = parseValue(fieldContext, valueType, context, destinationMap, customizeParser, true);
            destinationMap.put(key, value);
            curToken = nextToken();
        }
    }

    /**
     * Parses a value.
     *
     * @param fieldContext
     *            field context or {@code null} for none (for example into a map)
     * @param valueType
     *            value type or {@code null} if not known (for example into a map)
     * @param context
     *            destination context stack (possibly empty)
     * @param destination
     *            destination object instance or {@code null} for none (for example empty context stack)
     * @param customizeParser
     *            customize parser or {@code null} for none
     * @param handlePolymorphic
     *            whether or not to check for polymorphic schema
     * @return parsed value
     */
    private Object parseValue(Field fieldContext, Type valueType, ArrayList<Type> context, Object destination,
                              CustomizeJsonParser customizeParser, boolean handlePolymorphic) throws JsonException {

        valueType = Data.resolveWildcardTypeOrTypeVariable(context, valueType);
        // resolve a parameterized type to a class
        Class<?> valueClass = valueType instanceof Class<?> ? (Class<?>) valueType : null;
        if (valueType instanceof ParameterizedType) {
            valueClass = Types.getRawClass((ParameterizedType) valueType);
        }
        // Void means skip
        if (valueClass == Void.class) {
            skipChildren();
            return null;
        }
        // value type is now null, class, parameterized type, or generic array type
        JsonToken token = getCurrentToken();
        try {
            switch (getCurrentToken()) {
                case START_ARRAY:
                case END_ARRAY:
                    boolean isArray = Types.isArray(valueType);
                    Utils.checkArgument(logger,
                                        valueType == null || isArray
                                                || valueClass != null
                                                   && Types.isAssignableToOrFrom(valueClass, Collection.class),
                                        "expected collection or array type but got %s", valueType);
                    Collection<Object> collectionValue = null;
                    if (customizeParser != null && fieldContext != null) {
                        collectionValue = customizeParser.newInstanceForArray(destination, fieldContext);
                    }
                    if (collectionValue == null) {
                        collectionValue = Data.newCollectionInstance(valueType);
                    }
                    Type subType = null;
                    if (isArray) {
                        subType = Types.getArrayComponentType(valueType);
                    } else if (valueClass != null && Iterable.class.isAssignableFrom(valueClass)) {
                        subType = Types.getIterableParameter(valueType);
                    }
                    subType = Data.resolveWildcardTypeOrTypeVariable(context, subType);
                    parseArray(fieldContext, collectionValue, subType, context, customizeParser);
                    if (isArray) {
                        return Types.toArray(collectionValue, Types.getRawArrayComponentType(context, subType));
                    }
                    return collectionValue;
                case FIELD_NAME:
                case START_OBJECT:
                case END_OBJECT:
                    Utils.checkArgument(logger, !Types.isArray(valueType), "expected object or map type but got %s",
                                        valueType);
                    // Check if we're parsing into a polymorphic datatype.
                    Field typemapField = handlePolymorphic ? getCachedTypemapFieldFor(valueClass) : null;
                    Object newInstance = null;
                    if (valueClass != null && customizeParser != null) {
                        newInstance = customizeParser.newInstanceForObject(destination, valueClass);
                    }
                    boolean isMap = valueClass != null && Types.isAssignableToOrFrom(valueClass, Map.class);
                    if (typemapField != null) {
                        newInstance = new GenericJson();
                    } else if (newInstance == null) {
                        // check if it is a map to avoid ClassCastException to Map
                        if (isMap || valueClass == null) {
                            newInstance = Data.newMapInstance(valueClass);
                        } else {
                            newInstance = Types.newInstance(valueClass);
                        }
                    }
                    int contextSize = context.size();
                    if (valueType != null) {
                        context.add(valueType);
                    }
                    if (isMap && !GenericData.class.isAssignableFrom(valueClass)) {
                        Type subValueType = Map.class.isAssignableFrom(valueClass) ? Types.getMapValueParameter(valueType)
                                                                                   : null;
                        if (subValueType != null) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> destinationMap = (Map<String, Object>) newInstance;
                            parseMap(fieldContext, destinationMap, subValueType, context, customizeParser);
                            return newInstance;
                        }
                    }
                    parse(context, newInstance, customizeParser);
                    if (valueType != null) {
                        context.remove(contextSize);
                    }
                    if (typemapField == null) {
                        return newInstance;
                    }

                    // Get the correct type out of the naively parsed data.
                    Object typeValueObject = ((GenericJson) newInstance).get(typemapField.getName());
                    Utils.checkArgument(logger, typeValueObject != null,
                                        "No value specified for @JsonPolymorphicTypeMap field");
                    String typeValue = typeValueObject.toString();
                    JsonPolymorphicTypeMap typeMap = typemapField.getAnnotation(JsonPolymorphicTypeMap.class);
                    Class<?> typeClass = null;
                    for (TypeDef typeDefinition : typeMap.typeDefinitions()) {
                        if (typeDefinition.key().equals(typeValue)) {
                            typeClass = typeDefinition.ref();
                            break;
                        }
                    }
                    Utils.checkArgument(logger, typeClass != null,
                                        "No TypeDef annotation found with key: " + typeValue);
                    JsonFactory factory = getFactory();
                    // TODO(ngmiceli): Avoid having to parse JSON content twice. Optimize when type is first.
                    JsonParser parser = factory.createJsonParser(factory.toString(newInstance));
                    parser.startParsing();
                    return parser.parseValue(fieldContext, typeClass, context, null, null, false);
                case VALUE_TRUE:
                case VALUE_FALSE:
                    Utils.checkArgument(logger,
                                        valueType == null || valueClass == boolean.class
                                                || valueClass != null && valueClass.isAssignableFrom(Boolean.class),
                                        "expected type Boolean or boolean but got %s", valueType);
                    return token == JsonToken.VALUE_TRUE ? Boolean.TRUE : Boolean.FALSE;
                case VALUE_NUMBER_FLOAT:
                case VALUE_NUMBER_INT:
                    Utils.checkArgument(logger,
                                        fieldContext == null || fieldContext.getAnnotation(JsonString.class) == null,
                                        "number type formatted as a JSON number cannot use @JsonString annotation");
                    if (valueClass == null || valueClass.isAssignableFrom(BigDecimal.class)) {
                        return getDecimalValue();
                    }
                    if (valueClass == BigInteger.class) {
                        return getBigIntegerValue();
                    }
                    if (valueClass == Double.class || valueClass == double.class) {
                        return getDoubleValue();
                    }
                    if (valueClass == Long.class || valueClass == long.class) {
                        return getLongValue();
                    }
                    if (valueClass == Float.class || valueClass == float.class) {
                        return getFloatValue();
                    }
                    if (valueClass == Integer.class || valueClass == int.class) {
                        return getIntValue();
                    }
                    if (valueClass == Short.class || valueClass == short.class) {
                        return getShortValue();
                    }
                    if (valueClass == Byte.class || valueClass == byte.class) {
                        return getByteValue();
                    }
                    throw new IllegalArgumentException("expected numeric type but got " + valueType);
                case VALUE_STRING:
                    // TODO(user): Maybe refactor this method in multiple mini-methods for readability?
                    String text = getText().trim().toLowerCase(Locale.US);
                    // If we are expecting a Float / Double and the Text is NaN (case insensitive)
                    // Then: Accept, even if the Annotation is JsonString.
                    // Otherwise: Check that the Annotation is not JsonString.
                    if (!(((valueClass == float.class || valueClass == Float.class)
                           || (valueClass == double.class || valueClass == Double.class))
                          && (text.equals("nan") || text.equals("infinity") || text.equals("-infinity")))) {
                        Utils.checkArgument(logger,
                                            valueClass == null || !Number.class.isAssignableFrom(valueClass)
                                                    || fieldContext != null
                                                       && fieldContext.getAnnotation(JsonString.class) != null,
                                            "number field formatted as a JSON string must use the @JsonString annotation");
                    }
                    return Data.parsePrimitiveValue(valueType, getText());
                case VALUE_NULL:
                    Utils.checkArgument(logger, valueClass == null || !valueClass.isPrimitive(),
                                        "primitive number field but found a JSON null");
                    if (valueClass != null
                        && 0 != (valueClass.getModifiers() & (Modifier.ABSTRACT | Modifier.INTERFACE))) {
                        if (Types.isAssignableToOrFrom(valueClass, Collection.class)) {
                            return Data.nullOf(Data.newCollectionInstance(valueType).getClass());
                        }
                        if (Types.isAssignableToOrFrom(valueClass, Map.class)) {
                            return Data.nullOf(Data.newMapInstance(valueClass).getClass());
                        }
                    }
                    return Data.nullOf(Types.getRawArrayComponentType(context, valueType));
                default:
                    throw new IllegalArgumentException("unexpected JSON node type: " + token);
            }
        } catch (IllegalArgumentException e) {
            // build context string
            StringBuilder contextStringBuilder = new StringBuilder();
            String currentName = getCurrentName();
            if (currentName != null) {
                contextStringBuilder.append("key ").append(currentName);
            }
            if (fieldContext != null) {
                if (currentName != null) {
                    contextStringBuilder.append(", ");
                }
                contextStringBuilder.append("field ").append(fieldContext);
            }
            throw new IllegalArgumentException(contextStringBuilder.toString(), e);
        }
    }

    /**
     * Finds the {@link Field} on the given {@link Class} that has the {@link JsonPolymorphicTypeMap} annotation, or
     * {@code null} if there is none.
     *
     * <p>
     * The class must contain exactly zero or one {@link JsonPolymorphicTypeMap} annotation.
     * </p>
     *
     * @param key
     *            The {@link Class} to search in, or {@code null}
     * @return The {@link Field} with the {@link JsonPolymorphicTypeMap} annotation, or {@code null} either if there is
     *         none or if the key is {@code null}
     */
    private static Field getCachedTypemapFieldFor(Class<?> key) {
        if (key == null) {
            return null;
        }
        lock.lock();
        try {
            // Must use containsKey because we do store null values for when the class has no
            // JsonPolymorphicTypeMap field.
            if (cachedTypemapFields.containsKey(key)) {
                return cachedTypemapFields.get(key);
            }
            // Find the field that determines the type and cache it.
            Field value = null;
            Collection<FieldInfo> fieldInfos = ClassInfo.of(key).getFieldInfos();
            for (FieldInfo fieldInfo : fieldInfos) {
                Field field = fieldInfo.getField();
                JsonPolymorphicTypeMap typemapAnnotation = field.getAnnotation(JsonPolymorphicTypeMap.class);
                if (typemapAnnotation != null) {
                    Utils.checkArgument(logger, value == null,
                                        "Class contains more than one field with @JsonPolymorphicTypeMap annotation: %s",
                                        key);
                    Utils.checkArgument(logger, Data.isPrimitive(field.getType()),
                                        "Field which has the @JsonPolymorphicTypeMap, %s, is not a supported type: %s",
                                        key, field.getType());
                    value = field;
                    // Check for duplicate typeDef keys
                    TypeDef[] typeDefs = typemapAnnotation.typeDefinitions();
                    HashSet<String> typeDefKeys = new HashSet<>();
                    Utils.checkArgument(logger, typeDefs.length > 0,
                                        "@JsonPolymorphicTypeMap must have at least one @TypeDef");
                    for (TypeDef typeDef : typeDefs) {
                        Utils.checkArgument(logger, typeDefKeys.add(typeDef.key()),
                                            "Class contains two @TypeDef annotations with identical key: %s",
                                            typeDef.key());
                    }
                }
            }
            cachedTypemapFields.put(key, value);
            return value;
        } finally {
            lock.unlock();
        }
    }

}
