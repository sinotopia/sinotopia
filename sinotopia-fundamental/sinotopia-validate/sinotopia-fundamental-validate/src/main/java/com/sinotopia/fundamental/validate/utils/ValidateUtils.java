package com.hkfs.fundamental.validate.utils;

import com.sinotopia.fundamental.api.enums.BizRetCode;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ValidateUtils {
    
    private static Validator validator;
    
    static {
        init();
    }
    
    private static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    public static <T> void validOrThrowException(T bean) {
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            throw new com.hkfs.fundamental.exception.ApplicationBizException(BizRetCode.PARAMETER_ERROR.getCode(), buildMessageFromViolations(violations));
        }
    }
    
    public static <T> Set<ConstraintViolation<T>> validOrReturnViolations(T bean) {
        return validator.validate(bean);
    }
 
    private static <T> String buildMessageFromViolations(Set<ConstraintViolation<T>> violations) {
        // !violations.isEmpty()
        StringBuilder sBuilder = new StringBuilder(violations.size() * 32);
        for (ConstraintViolation<T> violation : violations) {
            //violation.
            sBuilder.append(violation.getMessage()).append(',');
        }
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
    
}
