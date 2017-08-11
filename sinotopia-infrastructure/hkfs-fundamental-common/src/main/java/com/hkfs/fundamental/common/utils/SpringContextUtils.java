package com.hkfs.fundamental.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

/**
 * 获取applicationContext对象
 * @author pc
 *
 */
public class SpringContextUtils {

	private static final Logger log = LoggerFactory.getLogger(SpringContextUtils.class);
	
	private static ApplicationContext applicationContext;

	public static ApplicationContext getSpringContext() {
		if (null == applicationContext) {
			throw new RuntimeException(
					"Spring ApplicationContext is null, Please config this Class as a spring bean.");
		}
		return applicationContext;
	}

	public static void setApplicationContext(ApplicationContext applicationContext) {
		SpringContextUtils.applicationContext = applicationContext;
		log.info("set applicaiton context success!");
	}
	
	/**
	 * 发布事件
	 * @param event
	 */
	public static void publishEvent(ApplicationEvent event){
		if(SpringContextUtils.getSpringContext()!=null){
			SpringContextUtils.getSpringContext().publishEvent(event);
		}
	}
}
