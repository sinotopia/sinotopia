package com.sinotopia.fundamental.validate.annotaion.validation;

import com.sinotopia.fundamental.validate.constants.ValidationConstants;
import com.sinotopia.fundamental.validate.validator.DateValueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DateValueValidator.class)
public @interface DateValue {
    
    Class<?>[] groups() default {};
    
    String message() default ValidationConstants.MSG_DATE;
    
    Class<? extends Payload>[] payload() default {};
    
    /**
     * 指明格式
     * @return
     */
    String pattern() default "yyyy-MM-dd hh:mm:ss";
    
}
