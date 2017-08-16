package com.sinotopia.fundamental.jackson.convert.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.sinotopia.fundamental.jackson.convert.FundamentalJsonSerializer;

import java.io.IOException;

/**
 * Created by zhoubing on 2016/8/5.
 */
public class NullNumberJsonSerializer extends FundamentalJsonSerializer {
    public NullNumberJsonSerializer() {
        super(Number.class);
    }

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        if (value == null) {
            jgen.writeNumber(0);
        } else {
            jgen.writeObject(value);
        }
    }
}
