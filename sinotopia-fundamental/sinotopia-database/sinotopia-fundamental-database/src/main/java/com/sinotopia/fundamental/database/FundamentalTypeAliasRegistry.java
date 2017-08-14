package com.sinotopia.fundamental.database;

import org.apache.ibatis.type.TypeAliasRegistry;

import java.util.Set;

/**
 * @author Clinton Begin
 */
public class FundamentalTypeAliasRegistry extends TypeAliasRegistry {
    
    //重新父类方法 使用FundamentalResolverUtil来处理别名
    @Override
    public void registerAliases(String packageName, Class<?> superType) {
        FundamentalResolverUtil<Class<?>> resolverUtil = new FundamentalResolverUtil<Class<?>>();
        resolverUtil.find(new FundamentalResolverUtil.IsA(superType), packageName);
        Set<Class<? extends Class<?>>> typeSet = resolverUtil.getClasses();
        for (Class<?> type : typeSet) {
            // Ignore inner classes and interfaces (including package-info.java)
            // Skip also inner classes. See issue #6
            if (!type.isAnonymousClass() && !type.isInterface() && !type.isMemberClass()) {
                registerAlias(type);
            }
        }
    }
}
