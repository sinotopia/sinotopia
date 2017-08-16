package com.sinotopia.fundamental.jackson.convert.serializer;

import java.math.BigDecimal;

/**
 * 小数点类型字段值的序列化
 * Created by zhoubing on 2016/8/5.
 */
public class DecimalJsonSerializer extends BigDecimalJsonSerializer {
    public DecimalJsonSerializer() {
        addClazz(Double.class);
        addClazz(double.class);
        addClazz(Float.class);
        addClazz(float.class);
        addClazz(BigDecimal.class);
    }
}
