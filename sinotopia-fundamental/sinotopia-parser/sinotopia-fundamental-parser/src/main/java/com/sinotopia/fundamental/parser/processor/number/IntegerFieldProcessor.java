package com.sinotopia.fundamental.parser.processor.number;

import com.sinotopia.fundamental.common.utils.NumberUtils;

/**
 * Integer类型的字段处理
 * Created by zhoubing on 2016/6/16.
 */
public class IntegerFieldProcessor extends NumberFieldProcessor<Integer> {
    @Override
    protected Integer parse(String value) {
        return NumberUtils.parseInt(value);
    }
}
