package com.sinotopia.fundamental.pay.spdb.bean;

import java.util.Map;

import com.sinotopia.fundamental.pay.spdb.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CommonBeanRequest {

	protected final Logger logger = LoggerFactory
			.getLogger(CommonBeanRequest.class);

	public Map<String, Object> toMap() {
		return ReflectUtil.getObjFieldAndValueToMap(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getName() + this.toMap();
	}

}
