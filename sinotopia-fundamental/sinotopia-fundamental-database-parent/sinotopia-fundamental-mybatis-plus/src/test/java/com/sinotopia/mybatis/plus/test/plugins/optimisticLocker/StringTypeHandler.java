package com.sinotopia.mybatis.plus.test.plugins.optimisticLocker;

import java.lang.reflect.Field;

import com.sinotopia.mybatis.plus.plugins.VersionHandler;

public class StringTypeHandler implements VersionHandler<String> {

    public void plusVersion(Object paramObj, Field field, String versionValue) throws Exception {
        field.set(paramObj, String.valueOf(Long.parseLong(versionValue) + 1));
    }

}
