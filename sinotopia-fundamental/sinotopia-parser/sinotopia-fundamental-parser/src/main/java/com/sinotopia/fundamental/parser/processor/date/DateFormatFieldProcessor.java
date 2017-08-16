package com.sinotopia.fundamental.parser.processor.date;

import com.sinotopia.fundamental.common.utils.TimeUtils;

/**
 * 将一种格式的日期字符串转换成另外一种格式的日期字符串
 * Created by zhoubing on 2016/6/16.
 */
public class DateFormatFieldProcessor extends BaseDateFieldProcessor<String> {
    //新日期格式
    private String newFormat;

    public DateFormatFieldProcessor(String format, String newFormat) {
        super(format);
        this.newFormat = newFormat;
    }
    public DateFormatFieldProcessor(String newFormat) {
        super(null);
        this.newFormat = newFormat;
    }

    public String getNewFormat() {
        return newFormat;
    }

    public void setNewFormat(String newFormat) {
        this.newFormat = newFormat;
    }

    @Override
    protected String parse(String value) {
        return TimeUtils.parseDateString(value, getFormat(), getNewFormat());
    }
}
