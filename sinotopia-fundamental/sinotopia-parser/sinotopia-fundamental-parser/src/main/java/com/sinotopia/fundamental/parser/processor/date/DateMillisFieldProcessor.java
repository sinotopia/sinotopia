package com.sinotopia.fundamental.parser.processor.date;

import com.sinotopia.fundamental.common.utils.TimeUtils;

/**
 * 日期字符串转时间毫秒数
 * Created by zhoubing on 2016/6/16.
 */
public class DateMillisFieldProcessor extends BaseDateFieldProcessor<Long> {
    public DateMillisFieldProcessor(String format) {
        super(format);
    }

    @Override
    protected Long parse(String value) {
        return TimeUtils.parseTime(value, getFormat());
    }
}
