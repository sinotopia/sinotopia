package com.sinotopia.fundamental.parser.processor.number;

import com.sinotopia.fundamental.common.utils.NumberUtils;

/**
 * Float类型的字段处理
 * Created by zhoubing on 2016/6/16.
 */
public class FloatFieldProcessor extends NumberFieldProcessor<Float> {
    @Override
    protected Float parse(String value) {
        return NumberUtils.parseFloat(value);
    }
}
