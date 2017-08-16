package com.sinotopia.fundamental.parser.processor;

/**
 * 字段处理器基类
 * Created by zhoubing on 2016/6/16.
 */
public abstract class BaseFieldProcessor<T> implements FieldProcessor<T> {
    /**
     * 将已经格式化好的对象转换成最终结果对象
     * @param value
     * @return
     */
    protected abstract T parse(String value);

    /**
     * 格式化对象，将对象转换成最终需要的字段
     * @param value
     * @return
     */
    protected abstract String format(String value);

    @Override
    public T process(String value) {
        if (value == null) {
            return null;
        }

        String formattedValue = format(value);
        if (formattedValue == null) {
            return null;
        }

        return parse(formattedValue);
    }
}
