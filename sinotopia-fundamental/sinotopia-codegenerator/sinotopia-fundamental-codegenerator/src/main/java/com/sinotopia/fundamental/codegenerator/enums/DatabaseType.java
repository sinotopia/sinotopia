package com.sinotopia.fundamental.codegenerator.enums;

/**
 * 数据库类型枚举
 * Created by zhoubing on 2016/5/13.
 */
public enum DatabaseType {
    MYSQL("mysql", "com.mysql.jdbc.Driver"),
    POSTGRESQL("postgresql", "org.postgresql.Driver"),

    ;



    private String type;
    private String driverClassName;

    private DatabaseType(String type, String driverClassName) {
        this.type = type;
        this.driverClassName = driverClassName;
    }

    public String getType() {
        return type;
    }

    public String getDriverClassName() {
        return driverClassName;
    }
}
