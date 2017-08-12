package com.hkfs.fundamental.test;

import com.hkfs.fundamental.codegenerator.lazy.LazyCodeGenerator;

/**
 * Created by brucezee on 2017/1/17.
 */
public class LazyCodeGeneratorTest {
    public static void main(String[] args) {
        //基本配置
        LazyCodeGenerator lazyCodeGenerator = LazyCodeGenerator.create()
                .setRoot("../crawler/")//相对路径
                .setPojoRoot("crawler-bean/src/main/java")
                .setDaoRoot("crawler-dao/src/main/java")
                .setMapperRoot("crawler-dao/src/main/resources/mappers")
                .setPojoPackageName("com.hakim.crawler.bean")
                .setDaoPackageName("com.hakim.crawler.dao")
                .setConnection("192.168.7.58", 3306, "crawler", "root", "123456");

        //其他
        lazyCodeGenerator.setColumnTypeReflection("DECIMAL", "Double");
//        lazyCodeGenerator.setTablePrefix("tab_");
//        lazyCodeGenerator.setIncludeTables("tab_user", "tab_user_account");
//        lazyCodeGenerator.setExcludeTables("tab_customer", "tab_nothing");
//        lazyCodeGenerator.setPrimaryKeyColumnName("id");
//        ...

        lazyCodeGenerator.generate();
    }
}
