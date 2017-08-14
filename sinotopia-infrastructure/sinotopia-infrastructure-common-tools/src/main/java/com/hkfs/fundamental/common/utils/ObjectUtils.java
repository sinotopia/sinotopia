package com.hkfs.fundamental.common.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoubing on 2016/4/18.
 */
public class ObjectUtils {
    /**
     * 判断对象是否为空（空列表，空字符串，空map）
     * @param object
     * @return
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        else if (object instanceof String) {
            return StrUtils.isEmpty((String)object);
        }
        else if (object instanceof Collection) {
            return ((List)object).size() == 0;
        }
        else if (object instanceof Map) {
            return ((Map)object).size() == 0;
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


}
