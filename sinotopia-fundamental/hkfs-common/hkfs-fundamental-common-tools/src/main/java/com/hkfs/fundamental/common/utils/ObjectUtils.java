package com.hkfs.fundamental.common.utils;

import com.hkfs.fundamental.common.assist.CodeMocker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by zhoubing on 2016/4/18.
 */
public class ObjectUtils {
    private static Logger logger = LoggerFactory.getLogger(CodeMocker.class);
    /**
     * 判断对象是否为空（空列表，空字符串，空map）
     * @param object
     * @return
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            return StrUtils.isEmpty((String)object);
        }
        if (object instanceof CharSequence) {
            return ((CharSequence) object).length() == 0;
        }
        if (object instanceof Collection) {
            return ((Collection)object).isEmpty();
        }
        if (object instanceof Map) {
            return ((Map)object).isEmpty();
        }
        if (object.getClass().isArray()) {
            return Array.getLength(object) == 0;
        }
        return false;
    }

    /**
     * 判断对象是否不为空（空列表，空字符串，空map）
     * @param object
     * @return
     */
    public static boolean notEmpty(Object object) {
        return !isEmpty(object);
    }

    /**
     * 将对象转换成字符串
     * @param object
     * @return
     */
    public static String toString(Object object) {
        return object != null ? object.toString() : null;
    }


    /**
     * 判断所有参数都是空
     * @param objects
     * @return
     */
    public static boolean isAllEmpty(Object...objects) {
        for (Object object : objects) {
            if (!isEmpty(object)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将对象转换成map
     * @param parameterObject 对象
     * @return
     */
    public static Map<String, Object> objectToMap(Object parameterObject) {
        return objectToMap(parameterObject, false);
    }

    /**
     * 将对象转换成map
     * @param parameterObject 对象
     * @param ignoreNullValue 是否忽略空值
     * @return
     */
    public static Map<String, Object> objectToMap(Object parameterObject, boolean ignoreNullValue) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        if (parameterObject == null) {
            return map;
        }
        Class<?> cls = parameterObject.getClass();
        while (cls != Object.class) {
            processMapFields(parameterObject, map, cls, ignoreNullValue);
            cls = cls.getSuperclass();
        }
        return map;
    }

    private static void processMapFields(Object parameterObject, Map<String, Object> map, Class<?> cls, boolean ignoreNullValue) {
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (modifiers != Modifier.PUBLIC && modifiers != Modifier.PRIVATE && modifiers != Modifier.PROTECTED) {
                //过滤掉private static final int serialVersionUID = 1L;
                continue;
            }
            try {
                field.setAccessible(true);
                if (field.isAccessible()) {
                    Object value = field.get(parameterObject);

                    if (ignoreNullValue) {
                        if (value != null) {
                            map.put(field.getName(), value);
                        }
                    }
                    else {
                        map.put(field.getName(), value);
                    }
                }
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
