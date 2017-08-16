package com.sinotopia.fundamental.validate.annotaion.validation;

import com.sinotopia.fundamental.validate.constants.ValidationConstants;
import com.sinotopia.fundamental.validate.validator.IdentityNoValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE,
        ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IdentityNoValidator.class)
public @interface IdentityNo {
    
    Class<?>[] groups() default {};
    
    String message() default ValidationConstants.MSG_IDENTITY_NO;
    
    Class<? extends Payload>[] payload() default {};
    
    String idKindField() default "id_kind";
    
    String idNoField() default "id_no";
    
}
