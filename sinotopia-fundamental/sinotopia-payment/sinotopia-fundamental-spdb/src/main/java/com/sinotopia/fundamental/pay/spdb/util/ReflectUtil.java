package com.sinotopia.fundamental.pay.spdb.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 *
 */
public class ReflectUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(ReflectUtil.class);

	public static List<Field> getAllFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<Field>();
		while (clazz != Object.class) {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}
		List<Field> filterFields = new ArrayList<Field>();
		for (Field field : fields) {
			field.setAccessible(true);
			filterFields.add(field);
		}
		return fields;
	}

	/**
	 * 将object中所有的field及其非空的值加入到map中去。
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> getObjFieldAndValueToMap(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (Field field : ReflectUtil.getAllFields(obj.getClass())) {
			try {
				Object fieldValue = field.get(obj);
				Class<?> type = field.getType();
				if (fieldValue != null
						&& (type == String.class || type == int.class
								|| type == long.class || Enum.class
									.isAssignableFrom(type))) {
					map.put(field.getName(), fieldValue.toString());
				}
			} catch (Exception e) {
				logger.warn("设置属性失败" + e);
			}
		}
		return map;
	}
}
