package com.sinotopia.fundamental.common.assist;

import com.sinotopia.fundamental.common.utils.StrUtils;

/**
 * 参数验证
 */
public class ParameterValidator {
    /**
     * 验证的参数
     */
    private Object object;
    /**
     * 验证提示信息
     */
    private String message;


    private boolean isPhone;
    private Long min;
    private Long max;

    public ParameterValidator(Object object, String message) {
        this.object = object;
        this.message = message;
    }

    public static ParameterValidator newInstance(Object object, String message) {
        return new ParameterValidator(object, message);
    }

    /**
     * 验证各个参数是否为空
     *
     * @param validators 各个参数
     * @return 如果各个参数都不为空则返回null，否则返回指定的验证提示信息
     */
    public static String validate(ParameterValidator... validators) {
        if (validators != null && validators.length > 0) {
            for (ParameterValidator validator : validators) {
                if (!isValid(validator)) {
                    return validator.message;
                }
            }
        }
        return null;
    }

    /**
     * 验证一个参数是否为空
     *
     * @param object  参数对象
     * @param message 当参数对象为空时返回的消息
     * @return
     */
    public static String validate(Object object, String message) {
        ParameterValidator validator = new ParameterValidator(object, message);
        if (!isValid(validator)) {
            return validator.message;
        }
        return null;
    }

    private static boolean isValid(ParameterValidator validator) {
        if (validator.object instanceof String) {
            String value = (String) validator.object;
            if (validator.isPhone) {
                if (!StrUtils.isPhoneNumber(value)) {
                    return false;
                }
            } else if (StrUtils.isEmpty(value)) {
                return false;
            }
        } else if (validator.object == null) {
            return false;
        }

        if (validator.min != null) {
            if (validator.object instanceof String) {
                return (((String) validator.object).length() > validator.min);
            } else if (validator.object instanceof Long) {
                return (((Long) validator.object) > validator.min);
            } else if (validator.object instanceof Integer) {
                return (((Integer) validator.object) > validator.min);
            } else if (validator.object instanceof Double) {
                return (((Double) validator.object) > validator.min);
            } else if (validator.object instanceof Float) {
                return (((Float) validator.object) > validator.min);
            }
        }

        if (validator.max != null) {
            if (validator.object instanceof String) {
                return (((String) validator.object).length() < validator.min);
            } else if (validator.object instanceof Long) {
                return (((Long) validator.object) < validator.min);
            } else if (validator.object instanceof Integer) {
                return (((Integer) validator.object) < validator.min);
            } else if (validator.object instanceof Double) {
                return (((Double) validator.object) < validator.min);
            } else if (validator.object instanceof Float) {
                return (((Float) validator.object) < validator.min);
            }
        }

        return true;
    }

    public ParameterValidator setPhone(boolean isPhone) {
        this.isPhone = isPhone;
        return this;
    }

    public ParameterValidator setMin(Long min) {
        this.min = min;
        return this;
    }

    public ParameterValidator setMax(Long max) {
        this.max = max;
        return this;
    }
}
