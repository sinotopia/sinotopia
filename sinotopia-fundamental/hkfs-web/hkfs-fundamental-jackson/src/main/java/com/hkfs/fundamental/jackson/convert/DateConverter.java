package com.hkfs.fundamental.jackson.convert;

import com.hkfs.fundamental.common.utils.TimeUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * <p>日期转化器</p>
 * @Author dzr
 * @Date 2016/6/7
 */
public class DateConverter implements Converter<String, Date> {
    @Override
    public Date convert(String source) {
        return TimeUtils.parseDate(source);
    }
}
