package com.hkfs.fundamental.jackson.convert.serializer;

/**
 * Float类型字段值的序列化
 * Created by zhoubing on 2016/8/5.
 */
public class FloatJsonSerializer extends BigDecimalJsonSerializer {
    public FloatJsonSerializer() {
        addClazz(Float.class);
        addClazz(float.class);
    }
}
