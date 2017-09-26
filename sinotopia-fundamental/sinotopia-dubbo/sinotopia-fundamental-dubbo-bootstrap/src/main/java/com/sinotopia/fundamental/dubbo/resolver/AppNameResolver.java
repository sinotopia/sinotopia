package com.sinotopia.fundamental.dubbo.resolver;

/**
 * <p>输入注释</p>
 */
public interface AppNameResolver {

    /**
     * 解析应用的名称
     *
     * @param resourceName
     * @return
     */
    String resolver(String resourceName);
}
