package com.sinotopia.fundamental.jackson.convert;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.HashMap;
import java.util.List;

/**
 * 空值序列化修改容器
 * Created by zhoubing on 2016/8/5.
 */
public class NullBeanSerializerModifier extends BeanSerializerModifier {
    private HashMap<Class<?>, JsonSerializer<Object>> nullSerializerMap = null;
    public NullBeanSerializerModifier(HashMap<Class<?>, JsonSerializer<Object>> nullSerializerMap) {
        this.nullSerializerMap = nullSerializerMap;
    }

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {
        if (nullSerializerMap != null && nullSerializerMap.size() > 0) {
            // 循环所有的beanPropertyWriter
            int size = beanProperties.size();
            for (int i = 0; i < size; i++) {
                BeanPropertyWriter writer = beanProperties.get(i);
                Class<?> clazz = writer.getPropertyType();
                // 判断字段的类型，注册nullSerializer
                // 给writer注册一个自己的nullSerializer

                //数字类型以父类来处理
                if (nullSerializerMap.containsKey(Number.class)) {
                    if (Number.class.isAssignableFrom(clazz)) {
                        writer.assignNullSerializer(nullSerializerMap.get(Number.class));
                        continue;
                    }
                }

                JsonSerializer<Object> nullSerializer = nullSerializerMap.get(clazz);
                if (nullSerializer != null) {
                    writer.assignNullSerializer(nullSerializer);
                }
            }
        }
        return beanProperties;
    }
}
