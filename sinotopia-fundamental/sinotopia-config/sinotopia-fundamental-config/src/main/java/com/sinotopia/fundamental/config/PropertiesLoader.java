package com.hkfs.fundamental.config;

import java.util.Properties;

/**
 * 配置加载器
 * Created by brucezee on 2017/2/5.
 */
public interface PropertiesLoader {
    /**
     * 加载配置参数
     * @param properties
     */
    public void load(Properties properties);
}
