package com.hkfs.fundamental.jackson.convert.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hkfs.fundamental.jackson.convert.FundamentalJsonSerializer;

import java.io.IOException;

/**
 * Created by zhoubing on 2016/8/5.
 */
public class NullStringJsonSerializer extends FundamentalJsonSerializer {
    public NullStringJsonSerializer() {
        super(String.class);
    }
    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        if (value == null) {
            jgen.writeString("");
        } else {
            jgen.writeObject(value);
        }
    }
}
