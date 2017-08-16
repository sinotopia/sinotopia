package com.sinotopia.fundamental.validate.annotaion.validation;

import com.sinotopia.fundamental.validate.constants.ValidationConstants;

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
public @interface Email {
    
    Class<?>[] groups() default {};
    
    String message() default ValidationConstants.MSG_EMAIL;
    
    @OverridesAttribute(constraint = Pattern.class, name = "regexp")
    String regexp() default "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
    
    Class<? extends Payload>[] payload() default {};
    
}
