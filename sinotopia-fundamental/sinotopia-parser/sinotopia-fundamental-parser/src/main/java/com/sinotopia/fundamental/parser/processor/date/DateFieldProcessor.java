package com.sinotopia.fundamental.parser.processor.date;

import com.sinotopia.fundamental.common.utils.TimeUtils;

import java.util.Date;

/**
 * 日期字符串转Date
 * Created by zhoubing on 2016/6/16.
 */
public class DateFieldProcessor extends BaseDateFieldProcessor<Date> {
    public DateFieldProcessor() {
        this(null);
    }
    public DateFieldProcessor(String format) {
        super(format);
    }

    @Override
    protected Date parse(String value) {
        return TimeUtils.parseDate(value, getFormat());
    }
}
