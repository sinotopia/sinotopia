package com.sinotopia.fundamental.parser.processor.number;

import com.sinotopia.fundamental.common.utils.StrUtils;
import com.sinotopia.fundamental.parser.processor.BaseFieldProcessor;

/**
 * 数字类型的字段处理器
 * Created by zhoubing on 2016/6/16.
 */
public abstract class NumberFieldProcessor<T> extends BaseFieldProcessor<T> {
    @Override
    protected String format(String value) {
        return StrUtils.getFirstNumberFromText(value, false);
    }
}
