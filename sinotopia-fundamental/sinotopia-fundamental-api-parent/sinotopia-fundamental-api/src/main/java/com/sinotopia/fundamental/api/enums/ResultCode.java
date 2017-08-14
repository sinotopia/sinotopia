package com.sinotopia.fundamental.api.enums;

/**
 * 结果码
 */
public enum ResultCode {

    SUCCESS(1, "成功"),
    FAILED(2, "失败"),

    COMMON_ERROR(-101, "系统繁忙，请稍后再试"),
    PARAMETER_ERROR(-102, "参数错误"),
    AUTHORIZATION_ERROR(-103, "权限错误"),
    NOT_LOGIN_ERROR(-104, "用户未登录"),;

    private int code;

    private String description;

    private ResultCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据值获取对应的枚举
     *
     * @param value 枚举的数值
     * @return 成功返回相应的枚举，否则返回null。
     */
    public static ResultCode parse(Integer value) {
        if (value != null) {
            ResultCode[] array = values();
            for (ResultCode each : array) {
                if (value == each.code) {
                    return each;
                }
            }
        }
        return null;
    }
}
