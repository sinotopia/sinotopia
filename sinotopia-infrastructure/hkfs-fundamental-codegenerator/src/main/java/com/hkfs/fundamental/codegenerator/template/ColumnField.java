package com.hkfs.fundamental.codegenerator.template;

/**
 * 数据库字段对应java类成员变量
 * Created by zhoubing on 2016/5/3.
 */
public class ColumnField {
    public String columnType;//数据库字段类型
    public String columnName;//数据库字段名称
    public String fieldType;//java对象字段类型
    public String fieldName;//java对象字段名称

    public ColumnField(String columnType, String columnName, String fieldType, String fieldName) {
        this.columnType = columnType;
        this.columnName = columnName;
        this.fieldType = fieldType;
        this.fieldName = fieldName;
    }

    public String getColumnType() {
        return columnType;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }
}
