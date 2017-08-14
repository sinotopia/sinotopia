package com.sinotopia.fundamental.database;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 具有转换特性的ArrayList
 */
public class ArrayListEx<T> extends ArrayList<T> {
    /**
     * 将当前列表转换成另外指定对象类型的列表，并拷贝对象字段
     *
     * @param clazz 新列表项的Class
     * @param <R>   新列表项的类型
     * @return 新列表
     */
    public <R> List<R> toList(Class<R> clazz) {
        return ArrayListEx.convertList(this, clazz);
    }

    /**
     * 将当前列表转换成另外指定对象类型的列表，并拷贝对象字段
     *
     * @param converter 列表转换器
     * @param <R>       新列表项的类型
     * @return 新列表
     */
    public <R> List<R> toList(Function<T, R> converter) {
        return ArrayListEx.convertList(this, converter);
    }

    /**
     * 根据类型构造对象
     *
     * @param clazz
     * @param <R>
     * @return
     */
    public static <R> R newInstance(Class<R> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 将当前列表转换成另外指定对象类型的列表，并拷贝对象字段
     *
     * @param sourceList 原列表
     * @param clazz      新列表项的Class
     * @param <K>        原列表项的类型
     * @param <R>        新列表项的类型
     * @return 新列表
     */
    public static <K, R> List<R> convertList(List<K> sourceList, Class<R> clazz) {
        int size = sourceList.size();
        List<R> targetList = new ArrayList<R>(size);
        R target = null;
        Map<String, Object> sourceMethodMap = new HashMap<String, Object>();
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(clazz);
        for (int i = 0; i < size; i++) {
            target = newInstance(clazz);
            copyProperties(sourceList.get(i), target, sourceMethodMap, targetPds);
            targetList.add(target);
        }
        return targetList;
    }

    /**
     * 将当前列表转换成另外指定对象类型的列表，并拷贝对象字段
     *
     * @param sourceList 原列表
     * @param converter  列表转换器
     * @param <K>        原列表项的类型
     * @param <R>        新列表项的类型
     * @return 新列表
     */
    public static <K, R> List<R> convertList(List<K> sourceList, Function<K, R> converter) {
        int size = sourceList.size();
        List<R> targetList = new ArrayList<R>(size);
        for (int i = 0; i < size; i++) {
            targetList.add(converter.apply(sourceList.get(i)));
        }
        return targetList;
    }

    /**
     * 重写BeanUtils.copyProperties，采用缓存减少多次反射同一个类的方法
     *
     * @param source          来源对象
     * @param target          目标对象
     * @param sourceMethodMap 来源对象读取属性方法缓存
     * @param targetPds       目标对象的属性描述器数组
     */
    private static void copyProperties(Object source, Object target,
                                       Map<String, Object> sourceMethodMap,
                                       PropertyDescriptor[] targetPds) {
        //维护目标对象属性读取方法映射，如果有映射则返回Method，否则返回1
        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null) {
                Method readMethod = null;
                Object cachedObject = sourceMethodMap.get(targetPd.getName());
                if (cachedObject == null) {
                    PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                    if (sourcePd == null) {
                        sourceMethodMap.put(targetPd.getName(), 1);
                        continue;
                    }
                    readMethod = sourcePd.getReadMethod();
                    if (readMethod == null ||
                            !ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        sourceMethodMap.put(targetPd.getName(), 1);
                        continue;
                    }

                    //缓存
                    sourceMethodMap.put(targetPd.getName(), readMethod);
                } else if (cachedObject instanceof Method) {
                    readMethod = (Method) cachedObject;
                }

                if (readMethod != null) {
                    try {
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source);
                        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                            writeMethod.setAccessible(true);
                        }
                        writeMethod.invoke(target, value);
                    } catch (Throwable ex) {
                        throw new FatalBeanException(
                                "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                    }
                }
            }
        }
    }
}
