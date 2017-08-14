package com.hkfs.fundamental.common.convert;

import com.hkfs.fundamental.common.convert.serializer.NullNumberJsonSerializer;
import com.hkfs.fundamental.common.convert.serializer.NullStringJsonSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * 对空字符，空数字序列化时使用默认的空串或0
 * Created by zhoubing on 2016/8/5.
 */
public class DefaultValueObjectMapper extends FundamentalObjectMapper {
    public DefaultValueObjectMapper() {
        List<FundamentalJsonSerializer> defaultNullSerializers = new ArrayList<FundamentalJsonSerializer>(2);
        defaultNullSerializers.add(new NullNumberJsonSerializer());
        defaultNullSerializers.add(new NullStringJsonSerializer());
        setNullSerializers(defaultNullSerializers);
    }
}
