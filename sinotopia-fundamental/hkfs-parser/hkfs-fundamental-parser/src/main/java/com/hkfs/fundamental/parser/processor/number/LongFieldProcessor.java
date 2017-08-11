package com.hkfs.fundamental.parser.processor.number;

import com.hkfs.fundamental.common.utils.NumberUtils;

/**
 * Long类型的字段处理
 * Created by zhoubing on 2016/6/16.
 */
public class LongFieldProcessor extends NumberFieldProcessor<Long> {
    @Override
    protected Long parse(String value) {
        return NumberUtils.parseLong(value);
    }
}
