package com.hakim.demo.admin.enums;

/**
 * 返回结果枚举
 * Created by brucezee on 2017/3/1.
 */
public enum AdminResultEnum {
    USERNAME_OR_PASSWORD_ERROR(100, "用户名或密码错误"),
    ORIGIN_PASSWORD_ERROR(101, "原密码错误"),
    NOT_FOUND(102, "记录未找到"),
    RECORD_DELETED(103, "记录已删除"),

    ROLE_CODE_EXISTS(120, "角色代码已经存在"),
    USERNAME_EXISTS(121, "登录用户名已经存在"),
    USER_DISABLED(123, "用户已被冻结"),
    USER_ROLE_EXISTS(124, "用户已经具有该项角色"),

    ;


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
    AdminResultEnum(int value, String comment) {
        this.value = value;
        this.comment = comment;
    }
    /**
     * 根据值获取对应的枚举
     * @param value 枚举的数值
     * @return 成功返回相应的枚举，否则返回null。
     */
    public static AdminResultEnum parse(Integer value) {
        if (value != null) {
            AdminResultEnum[] array = values();
            for (AdminResultEnum each : array) {
                if (value == each.value) {
                    return each;
                }
            }
        }
        return null;
    }
}
