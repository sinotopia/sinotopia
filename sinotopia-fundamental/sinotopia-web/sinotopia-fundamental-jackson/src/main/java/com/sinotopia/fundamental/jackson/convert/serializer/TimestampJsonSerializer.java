package com.sinotopia.fundamental.jackson.convert.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.sinotopia.fundamental.jackson.convert.FundamentalJsonSerializer;

import java.io.IOException;
import java.util.Date;

/**
 * 日期类型字段值的序列化(序列化成Long)
 * Created by zhoubing on 2016/8/5.
 */
public class TimestampJsonSerializer extends FundamentalJsonSerializer {
    public TimestampJsonSerializer() {
        super(Date.class);
    }

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        if (value == null) {
            jgen.writeNull();
        }
        else {
            jgen.writeNumber(((Date) value).getTime());
        }
    }
}
