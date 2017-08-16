package com.sinotopia.fundamental.jackson.convert.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.sinotopia.fundamental.jackson.convert.FundamentalJsonSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BigDecimal类型字段值的序列化
 * Created by zhoubing on 2016/8/5.
 */
public class BigDecimalJsonSerializer extends FundamentalJsonSerializer {
    private Integer scale;
    private RoundingMode roundingMode;
    private Integer roundingModeValue;

    public BigDecimalJsonSerializer() {
        super(BigDecimal.class);
    }

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        if (value == null) {
            jgen.writeNull();
        } else {
            if (scale != null) {
                BigDecimal bigDecimal = new BigDecimal(value.toString());
                if (roundingMode != null) {
                    bigDecimal = bigDecimal.setScale(scale, roundingMode);
                }
                else if (roundingModeValue != null) {
                    bigDecimal = bigDecimal.setScale(scale, roundingModeValue);
                }
                else {
                    bigDecimal = bigDecimal.setScale(scale, RoundingMode.HALF_UP);
                }

                writeValue(jgen, bigDecimal);
            }
            else {
                jgen.writeObject(value);
            }
        }
    }

    public Integer getRoundingModeValue() {
        return roundingModeValue;
    }

    public void setRoundingModeValue(Integer roundingModeValue) {
        this.roundingModeValue = roundingModeValue;
    }

    public RoundingMode getRoundingMode() {
        return roundingMode;
    }

    public void setRoundingMode(RoundingMode roundingMode) {
        this.roundingMode = roundingMode;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }
}
