package com.hkfs.fundamental.validate;

/**
 * Created by zhoubing on 2016/5/24.
 */
public enum ValidateRetCode {
    FILTER_PARAM_MUST_ONLY_ONE(1005, "该方法参数必须唯一请检查！"),
    FILTER_METHOD_MUST_RETURN_RESULT(1007, "该方法返回类型必须为Result类型！"),
    FILTER_PARAM_ERROR(1008, "过滤器参数没传！参数："),
    FILTER_ERROR(1009, "过滤器错误"),
    
    ;
    private int code;
    private String description;

    private ValidateRetCode(int code, String description) {
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
     * @param value 枚举的数值
     * @return 成功返回相应的枚举，否则返回null。
     */
    public static ValidateRetCode parse(Integer value) {
        if (value != null) {
            ValidateRetCode[] array = values();
            for (ValidateRetCode each : array) {
                if (value == each.code) {
                    return each;
                }
            }
        }
        return null;
    }
}
