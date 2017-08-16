package com.sinotopia.fundamental.parser.processor.date;

import com.sinotopia.fundamental.common.utils.StrUtils;
import com.sinotopia.fundamental.parser.processor.BaseFieldProcessor;

/**
 *
 * Created by zhoubing on 2016/6/16.
 */
public abstract class BaseDateFieldProcessor<T> extends BaseFieldProcessor<T> {
    //字段日期的格式
    private String format;

    public BaseDateFieldProcessor(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    protected String format(String value) {
        return StrUtils.trim(value);
    }
}
