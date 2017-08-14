package com.sinotopia.fundamental.database.sqlanalysis;

/**
 */
public class Parameter {

    private String type;
    private String value;

    public Parameter(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getSqlValue() {
        if ("Long".equals(type)
                || "Integer".equals(type)
                || "Double".equals(type)
                || "Float".equals(type)) {
            return value;
        }

        //String Timestamp
        return "'" + value + "'";
    }
}
