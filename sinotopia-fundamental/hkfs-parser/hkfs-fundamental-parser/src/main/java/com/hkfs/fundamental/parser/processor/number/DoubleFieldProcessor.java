package com.hkfs.fundamental.parser.processor.number;

import com.hkfs.fundamental.common.utils.NumberUtils;

/**
 * Double类型的字段处理
 * Created by zhoubing on 2016/6/16.
 */
public class DoubleFieldProcessor extends NumberFieldProcessor<Double> {
    @Override
    protected Double parse(String value) {
        return NumberUtils.parseDouble(value);
    }
}
