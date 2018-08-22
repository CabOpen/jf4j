package org.jf4j.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import org.jf4j.JsonException;
import org.jf4j.JsonFactory;
import org.jf4j.JsonGenerator;
import org.jf4j.JsonObjectReader;
import org.jf4j.JsonParser;

public class NoOpFactory implements JsonFactory {

    public static final String FACTORY_ID = "NoOp";
    public static final String FACTORY_DESCRITION = "No operation (" + FACTORY_ID + ") JSON implementation";

    public NoOpFactory() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jf4j.JsonFactory#createParser(java.io.InputStream)
     */
    @Override
    public JsonParser createReader(InputStream in) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jf4j.JsonFactory#createParser(java.io.InputStream, java.nio.charset.Charset)
     */
    @Override
    public JsonParser createReader(InputStream in, Charset charset) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jf4j.JsonFactory#createParser(java.lang.String)
     */
    @Override
    public JsonParser createReader(String value) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jf4j.JsonFactory#createParser(java.io.Reader)
     */
    @Override
    public JsonParser createReader(Reader reader) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jf4j.JsonFactory#createGenerator(java.io.OutputStream)
     */
    @Override
    public JsonGenerator createGenerator(OutputStream out) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jf4j.JsonFactory#createGenerator(java.io.OutputStream, java.nio.charset.Charset)
     */
    @Override
    public JsonGenerator createGenerator(OutputStream out, Charset encoding) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jf4j.JsonFactory#createGenerator(java.io.Writer)
     */
    @Override
    public JsonGenerator createGenerator(Writer writer) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jf4j.JsonFactory#createObjectParser()
     */
    @Override
    public JsonObjectReader createObjectParser() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jf4j.JsonFactory#toString(java.lang.Object)
     */
    @Override
    public String toString(Object item) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jf4j.JsonFactory#toPrettyString(java.lang.Object)
     */
    @Override
    public String toPrettyString(Object item) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jf4j.JsonFactory#toByteArray(java.lang.Object)
     */
    @Override
    public byte[] toByteArray(Object item) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jf4j.JsonFactory#fromString(java.lang.String, java.lang.Class)
     */
    @Override
    public <T> T fromString(String value, Class<T> destinationClass) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jf4j.JsonFactory#fromInputStream(java.io.InputStream, java.lang.Class)
     */
    @Override
    public <T> T fromInputStream(InputStream inputStream, Class<T> destinationClass) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jf4j.JsonFactory#fromInputStream(java.io.InputStream, java.nio.charset.Charset, java.lang.Class)
     */
    @Override
    public <T> T fromInputStream(InputStream inputStream, Charset charset,
                                 Class<T> destinationClass) throws JsonException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jf4j.JsonFactory#fromReader(java.io.Reader, java.lang.Class)
     */
    @Override
    public <T> T fromReader(Reader reader, Class<T> destinationClass) throws JsonException {
        // TODO Auto-generated method stub
        return null;
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

    public static boolean isNoOp(JsonFactory factory) {
        return factory == null ? false : factory.toString() == FACTORY_ID;
    }

    @Override
    public String description() {
        return FACTORY_DESCRITION;
    }

}
