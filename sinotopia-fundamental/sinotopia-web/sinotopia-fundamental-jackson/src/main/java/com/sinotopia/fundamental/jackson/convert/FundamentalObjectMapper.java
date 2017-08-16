package com.sinotopia.fundamental.jackson.convert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sinotopia.fundamental.api.data.ListResultEx;
import com.sinotopia.fundamental.api.data.Result;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由于默认springmvc序列化结果对象时会将额外的以get开头的方法也会序列化成json，这里将Result及子类中额外的参数过滤掉
 */
public class FundamentalObjectMapper extends ObjectMapper {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private List<FundamentalJsonSerializer> valueSerializers;//自定义value序列化列表
    private List<FundamentalJsonSerializer> nullSerializers;//自定义空null序列化列表

    public FundamentalObjectMapper(){
        super();
        initParameters();//定义部分参数
        addSerializers();//添加字段处理器
    }

    private void initParameters() {
        setSerializationInclusion(JsonInclude.Include.NON_NULL);//对于map list无效
        setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));
        disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);//当对象没有任何字段时 不抛异常
    }

    private void addSerializers() {
        SimpleModule module = new SimpleModule();

        addDefaultSerializers(module);

        registerModule(module);
    }

    //对Result的子类进行特殊处理，ResultEx,ListResultEx,ObjectResultEx
    //特殊处理是由于子类中有非成员变量的get方法，这些get方法会导致重复序列化同一个列表或对象
    protected void addDefaultSerializers(SimpleModule module) {
        module.addSerializer(Result.class, new JsonSerializer<Result>() {
            @Override
            public void serialize(Result value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
                Map<String, Object> map = new HashMap<String, Object>(4);
                Integer retCode = value.getRetCode();
                String retMsg = value.getRetMsg();
                Object data = value.getData();
                if (retCode != null) {
                    map.put("retCode", retCode);
                }
                if (retMsg != null) {
                    map.put("retMsg", retMsg);
                }
                if (data != null) {
                    map.put("data", data);
                }
                if (value instanceof ListResultEx) {
                    ListResultEx listResultEx = (ListResultEx) value;
                    Integer totalCount = listResultEx.getTotalCount();
                    if (totalCount != null) {
                        map.put("totalCount", totalCount);
                    }
                    Integer pageNo = listResultEx.getPageNo();
                    if (pageNo != null) {
                        map.put("pageNo", pageNo);
                    }
                    Integer pageSize = listResultEx.getPageSize();
                    if (pageSize != null) {
                        map.put("pageSize", pageSize);
                    }
                }
                jgen.writeObject(map);
            }
        });
    }

    public List<FundamentalJsonSerializer> getValueSerializers() {
        return valueSerializers;
    }

    public void setValueSerializers(List<FundamentalJsonSerializer> valueSerializers) {
        this.valueSerializers = valueSerializers;

        if (valueSerializers != null) {
            SimpleModule module = new SimpleModule();

            for (FundamentalJsonSerializer serializer : valueSerializers) {
                List<Class<?>> clazzList = serializer.getClazzList();
                if (clazzList != null && clazzList.size() > 0) {
                    //同一个处理器可以处理多个类型
                    for (Class<?> clazz : clazzList) {
                        module.addSerializer(clazz, serializer);
                    }
                }
                else {
                    module.addSerializer(serializer);
                }
            }

            registerModule(module);
        }
    }

    public List<FundamentalJsonSerializer> getNullSerializers() {
        return nullSerializers;
    }

    public void setNullSerializers(List<FundamentalJsonSerializer> nullSerializers) {
        this.nullSerializers = nullSerializers;

        if (nullSerializers != null) {
            HashMap<Class<?>, JsonSerializer<Object>> nullSerializersMap = new HashMap<Class<?>, JsonSerializer<Object>>(nullSerializers.size());
            for (FundamentalJsonSerializer nullSerializer : nullSerializers) {
                nullSerializersMap.put(nullSerializer.getClazz(), nullSerializer);
            }

            NullBeanSerializerModifier nullBeanSerializerModifier = new NullBeanSerializerModifier(nullSerializersMap);
            setSerializerFactory(getSerializerFactory().withSerializerModifier(nullBeanSerializerModifier));
        }
    }


}
