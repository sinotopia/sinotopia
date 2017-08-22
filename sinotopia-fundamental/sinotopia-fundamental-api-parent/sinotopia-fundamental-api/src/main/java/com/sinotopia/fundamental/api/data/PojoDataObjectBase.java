package com.sinotopia.fundamental.api.data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 */
public class PojoDataObjectBase extends PageDataObjectBase {

	private static final long serialVersionUID = 1L;

	public Object extendedParameter;

	public Object getExtendedParameter() {
		return extendedParameter;
	}

	public void setExtendedParameter(Object extendedParameter) {
		this.extendedParameter = extendedParameter;
	}

	public void putExtendedParameterValue(String key, Object value) {

		if (extendedParameter == null) {
			extendedParameter = new LinkedHashMap<String, Object>();
		}
		if (!(extendedParameter instanceof Map)) {
			throw new IllegalArgumentException("extendedParameter不是Map类型，不能用当前方法添加扩展参数");
		}
		((Map) extendedParameter).put(key, value);
	}

	public Object getExtendedParameterValue(String key) {
		if (!(extendedParameter instanceof Map)) {
			throw new IllegalArgumentException("extendedParameter不是Map类型，不能用当前方法获取扩展参数");
		}
		return ((Map) extendedParameter).get(key);
	}

	public void orderBy(OrderBy orderBy) {
		putExtendedParameterValue("orderBy", orderBy);
	}
}
