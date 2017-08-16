package com.sinotopia.fundamental.parser.processor;

/**
 * 字段处理器
 * Created by zhoubing on 2016/6/16.
 */
public interface FieldProcessor<T> {
    /**
     * 处理字段值
     * @param value 字段值
     * @return
     */
    public T process(String value);
}
