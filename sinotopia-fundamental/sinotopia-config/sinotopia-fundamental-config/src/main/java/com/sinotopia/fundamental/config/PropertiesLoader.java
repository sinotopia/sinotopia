package com.sinotopia.fundamental.config;

import java.util.Properties;

/**
 * 配置加载器
 */
public interface PropertiesLoader {
    /**
     * 加载配置参数
     *
     * @param properties
     */
    public void load(Properties properties);
}
