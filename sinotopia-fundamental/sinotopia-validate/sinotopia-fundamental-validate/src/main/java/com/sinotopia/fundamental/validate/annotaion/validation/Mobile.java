package com.hkfs.fundamental.validate.annotaion.validation;

import com.hkfs.fundamental.validate.constants.ValidationConstants;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;


@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@ReportAsSingleViolation
@Pattern(regexp = "")
public @interface Mobile {
    
    Class<?>[] groups() default {};
    
    String message() default ValidationConstants.MSG_MOBILE;
    
    @OverridesAttribute(constraint = Pattern.class, name = "regexp")
    String regexp() default "^(1[34578]\\d{9})?$";
    
    Class<? extends Payload>[] payload() default {};
    
}
