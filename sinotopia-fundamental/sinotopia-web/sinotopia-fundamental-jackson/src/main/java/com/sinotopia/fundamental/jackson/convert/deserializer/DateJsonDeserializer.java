package com.hkfs.fundamental.jackson.convert.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.hkfs.fundamental.common.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by lvjx on 2017/3/28 0028.
 */
public class DateJsonDeserializer extends JsonDeserializer<Date> {

    public static final Logger log = LoggerFactory.getLogger(DateJsonDeserializer.class);
    /**
     * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser,
     *      com.fasterxml.jackson.databind.DeserializationContext)
     */
    @Override
    public Date deserialize(JsonParser parser, DeserializationContext context)
             {
        try {
            return TimeUtils.parseDate(parser.getValueAsString());
        } catch (Exception e) {
            log.error("parse json error",e);
        }
        return null;
    }
}
