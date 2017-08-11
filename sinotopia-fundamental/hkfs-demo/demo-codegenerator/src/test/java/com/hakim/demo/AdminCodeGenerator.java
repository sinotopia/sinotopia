package com.hakim.demo;

import com.hkfs.fundamental.codegenerator.lazy.LazyCodeGenerator;

/**
 * 对账系统数据库对象自动生成
 * Created by brucezee on 2017/2/21.
 */
public class AdminCodeGenerator {
    public static void main(String[] args) {
        LazyCodeGenerator lazyCodeGenerator = LazyCodeGenerator.create()
                .setRoot("../hkfs-demo/demo-business/demo-admin")               //全局根路径
                .setPojoRoot("demo-admin-bean/src/main/java")                   //数据库对象的根路径
                .setDaoRoot("demo-admin-dao/src/main/java")                     //Dao的根路径
                .setMapperRoot("demo-admin-dao/src/main/resources/mappers")     //数据库Mapper的根路径
                .setPojoPackageName("com.hakim.demo.admin.bean")                //数据库对象的包名
                .setDaoPackageName("com.hakim.demo.admin.dao")                  //Dao的包名
                .setConnection("192.168.7.187", 3306, "admin", "root", "123456")//数据库连接配置
                .usePageDaoBase()                           //使用分页PageDaoBase作为Dao的基类，生成分页的Dao和Mapper

//                .setIncludeTables("tb_product", "tb_borrow")              //只单独处理的表
//                .setExcludeTables("tb_company", "tb_school")              //排除处理的表
//                .putTableNameConfig("tb_account_file", "DycAccountFile")  //将某张表对应到指定的类名
//                .setColumnTypeReflection("DECIMAL", "Double")             //将某种数据库字段类型隐射到Java的数据类型
                ;
        lazyCodeGenerator.generate();
    }
}
