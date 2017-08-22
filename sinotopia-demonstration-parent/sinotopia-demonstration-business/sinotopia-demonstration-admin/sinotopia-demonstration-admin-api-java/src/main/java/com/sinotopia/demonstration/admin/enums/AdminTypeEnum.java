package com.sinotopia.demonstration.admin.enums;

/**
 * 管理员类型 1,administrator,超级管理员;2,manager,普通管理员;
 */
public enum AdminTypeEnum {
    /**
     * 1, 可用
     */
    ADMINISTRATOR(1, "超级管理员"),
    /**
     * 2, 不可用
     */
    MANAGER(2, "普通管理员");
    /**
     * 值
     */
    public int value;
    /**
     * 注释
     */
    public String comment;
    public void setValue(int value) {
        this.value = value;
    }
    public int getValue() {
        return this.value;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getComment() {
        return this.comment;
    }
    AdminTypeEnum(int value, String comment) {
        this.value = value;
        this.comment = comment;
    }
    /**
     * 根据值获取对应的枚举
     * @param value 枚举的数值
     * @return 成功返回相应的枚举，否则返回null。
     */
    public static AdminTypeEnum parse(Integer value) {
        if (value != null) {
            AdminTypeEnum[] array = values();
            for (AdminTypeEnum each : array) {
                if (value == each.value) {
                    return each;
                }
            }
        }
        return null;
    }

    /**
     * 判断是否是超级管理员
     * @param value
     * @return
     */
    public static boolean isAdministrator(Object value) {
        return value != null && Integer.parseInt(value.toString()) == ADMINISTRATOR.getValue();
    }
}
