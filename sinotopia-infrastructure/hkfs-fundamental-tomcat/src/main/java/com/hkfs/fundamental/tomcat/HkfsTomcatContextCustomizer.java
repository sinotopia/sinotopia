package com.hkfs.fundamental.tomcat;

import org.apache.catalina.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;

/**
 * <p>TomcatContext自定义器</p>
 *
 * @Author dzr
 * @Date 2016/7/8
 */
public class HkfsTomcatContextCustomizer implements TomcatContextCustomizer {
    private final static Logger LOGGER = LoggerFactory.getLogger(HkfsTomcatContextCustomizer.class);
    @Override
    public void customize(Context context) {

        LOGGER.info("**********************Hkfs Tomcat Context config begin ......");

        LOGGER.info("**********************Hkfs Tomcat Context config finished!");
    }
}
