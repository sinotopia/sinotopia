package com.hkfs.fundamental.jackson.convert.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hkfs.fundamental.jackson.convert.FundamentalJsonSerializer;
import com.hkfs.fundamental.jackson.convert.FundamentalObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期类型字段值的序列化
 * Created by zhoubing on 2016/8/5.
 */
public class DateJsonSerializer extends FundamentalJsonSerializer {
    private SimpleDateFormat simpleDateFormat;
    private String dateFormat;

    public DateJsonSerializer() {
        super(Date.class);
    }

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        if (value == null) {
            jgen.writeNull();
        }
        else {
            jgen.writeString(getFormat().format((Date) value));
        }
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        this.simpleDateFormat = new SimpleDateFormat(dateFormat);
    }

    private SimpleDateFormat getFormat() {
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat(FundamentalObjectMapper.DEFAULT_DATE_FORMAT);
        }
        return simpleDateFormat;
    }
}
