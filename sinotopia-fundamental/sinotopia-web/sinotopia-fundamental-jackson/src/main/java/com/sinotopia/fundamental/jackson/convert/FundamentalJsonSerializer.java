package com.sinotopia.fundamental.jackson.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhoubing on 2016/8/5.
 */
public abstract class FundamentalJsonSerializer extends JsonSerializer<Object> {
    private String className;
    private List<Class<?>> clazzList;
    protected ObjectMapper objectMapper = new ObjectMapper();//用于单独序列化

    public FundamentalJsonSerializer(String className) throws ClassNotFoundException {
        this.className = className;
        parseClass(className);
    }
    public FundamentalJsonSerializer(Class<?> clazz) {
        setClazz(clazz);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) throws ClassNotFoundException {
        this.className = className;
        parseClass(className);
    }

    public Class<?> getClazz() {
        if (clazzList != null && clazzList.size() > 0) {
            return clazzList.get(0);
        }
        return null;
    }
    public List<Class<?>> getClazzList() {
        return this.clazzList;
    }

    public void setClazz(Class<?> clazz) {
        this.clazzList = new LinkedList<Class<?>>();
        this.clazzList.add(clazz);
    }
    public void addClazz(Class<?> clazz) {
        if (clazzList == null) {
            clazzList = new LinkedList<Class<?>>();
        }
        this.clazzList.add(clazz);
    }

    private void parseClass(String name) throws ClassNotFoundException {
        setClazz(Class.forName(name).getClass());
    }

    //使用额外的ObjectMapper进行序列化，防止死循环
    public void writeValue(JsonGenerator jgen, Object value) throws IOException {
        objectMapper.writeValue(jgen, value);
    }

}
