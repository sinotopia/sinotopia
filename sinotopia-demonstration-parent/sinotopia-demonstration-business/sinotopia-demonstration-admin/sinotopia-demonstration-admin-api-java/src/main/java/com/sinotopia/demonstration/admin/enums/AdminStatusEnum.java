package com.sinotopia.demonstration.admin.enums;

/**
 * 通用状态:1,enabled,可用;2,disabled,不可用;3,deleted,已删除;
 */
public enum AdminStatusEnum {
    /**
     * 1, 可用
     */
    ENABLED(1, "可用"),
    /**
     * 2, 不可用
     */
    DISABLED(2, "不可用"),
    /**
     * 3, 已删除
     */
    DELETED(3, "已删除");
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

    AdminStatusEnum(int value, String comment) {
        this.value = value;
        this.comment = comment;
    }

    /**
     * 根据值获取对应的枚举
     *
     * @param value 枚举的数值
     * @return 成功返回相应的枚举，否则返回null。
     */
    public static AdminStatusEnum parse(Integer value) {
        if (value != null) {
            AdminStatusEnum[] array = values();
            for (AdminStatusEnum each : array) {
                if (value == each.value) {
                    return each;
                }
            }
        }
        return null;
    }
}
