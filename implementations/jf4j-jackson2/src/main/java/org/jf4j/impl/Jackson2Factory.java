/**
 *
 */
package org.jf4j.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import org.jf4j.AbstractJsonFactory;
import org.jf4j.JsonException;
import org.jf4j.JsonGenerator;
import org.jf4j.JsonObjectReader;
import org.jf4j.JsonParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Jackson2Factory extends AbstractJsonFactory {

    public static final String FACTORY_DESCRIPTION = "Jackson 2 (https://github.com/FasterXML/jackson) JSON implementation";
    private static final String FACTORY_ID = "Jackson2";
    private final com.fasterxml.jackson.core.JsonFactory factory;
    private final ObjectMapper mapper;
    private final ObjectMapper prettyMapper;

    public Jackson2Factory() {
        super();
        factory = new com.fasterxml.jackson.core.JsonFactory();
        mapper = new ObjectMapper();
        prettyMapper = new ObjectMapper();
        // Non-standard JSON but we allow C style comments in our JSON
        mapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS, true);

        prettyMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS, true);
        prettyMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return FACTORY_ID;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jf4j.JsonFactory#description()
     */
    @Override
    public String description() {
        return FACTORY_DESCRIPTION;
    }

    @Override
    public JsonParser createReader(InputStream in) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JsonParser createReader(InputStream in, Charset charset) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JsonParser createReader(String value) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JsonParser createReader(Reader reader) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out, Charset encoding) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JsonGenerator createGenerator(Writer writer) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JsonObjectReader createObjectParser() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T fromInputStream(InputStream inputStream, Class<T> destinationClass) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T fromInputStream(InputStream inputStream, Charset charset,
                                 Class<T> destinationClass) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T fromReader(Reader reader, Class<T> destinationClass) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

}
