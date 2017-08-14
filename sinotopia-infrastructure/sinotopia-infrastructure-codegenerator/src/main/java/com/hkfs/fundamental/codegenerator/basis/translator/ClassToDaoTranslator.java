package com.hkfs.fundamental.codegenerator.basis.translator;

import com.hkfs.fundamental.codegenerator.basis.data.Class;
import com.hkfs.fundamental.codegenerator.basis.data.Interface;

/**
 * Created by zhoubing on 2016/5/3.
 */
public class ClassToDaoTranslator {
    /**
     * 生成的Dao接口的包名
     */
    private String packageName;

    public ClassToDaoTranslator(String packageName) {
        this.packageName = packageName;
    }

    /**
     * POJO对象转换成Dao接口
     * @param pojoClass 数据库表
     * @return 转换后的Dao接口
     */
    public Interface translate(Class pojoClass) {
        return new Interface(processDaoFullClassName(pojoClass));
    }

    protected String processDaoClassName(Class pojoClass) {
        StringBuilder sb = new StringBuilder();
        sb.append(pojoClass.getClassName()).append("Dao");
        return sb.toString();
    }
    protected String processDaoFullClassName(Class pojoClass) {
        StringBuilder sb = new StringBuilder();
        sb.append(packageName).append(".").append(processDaoClassName(pojoClass));
        return sb.toString();
    }
}
