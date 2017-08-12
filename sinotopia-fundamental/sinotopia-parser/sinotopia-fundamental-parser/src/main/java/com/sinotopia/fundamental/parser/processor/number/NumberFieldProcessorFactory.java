package com.hkfs.fundamental.parser.processor.number;

/**
 * 数字类处理器工厂
 * Created by zhoubing on 2016/6/16.
 */
public class NumberFieldProcessorFactory {
    /**
     * 创建字段处理器
     * @param cls
     * @return
     */
    public NumberFieldProcessor createFieldProcessor(Class<?> cls) {
        if (cls == Double.class) {
            return new DoubleFieldProcessor();
        }
        if (cls == Integer.class) {
            return new IntegerFieldProcessor();
        }
        if (cls == Long.class) {
            return new LongFieldProcessor();
        }
        if (cls == Float.class) {
            return new FloatFieldProcessor();
        }
        return null;
    }
}
