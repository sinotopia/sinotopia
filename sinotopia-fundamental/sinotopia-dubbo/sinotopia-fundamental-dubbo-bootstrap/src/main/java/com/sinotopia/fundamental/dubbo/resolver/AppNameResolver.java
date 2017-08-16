package com.sinotopia.fundamental.dubbo.resolver;

/**
 * <p>输入注释</p>
 *
 * @Author dzr
 * @Date 2016/6/6
 */
public interface AppNameResolver {

    /**
     * 解析应用的名称
     * @param resourceName
     * @return
     */
    public String resolver(String resourceName);
}
