package com.hkfs.fundamental.common.convert.serializer;

/**
 * Double类型字段值的序列化
 * Created by zhoubing on 2016/8/5.
 */
public class DoubleJsonSerializer extends BigDecimalJsonSerializer {
    public DoubleJsonSerializer() {
        addClazz(Double.class);
        addClazz(double.class);
    }
}
