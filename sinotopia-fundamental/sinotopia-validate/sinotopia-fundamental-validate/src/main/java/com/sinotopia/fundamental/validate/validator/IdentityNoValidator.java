package com.sinotopia.fundamental.validate.validator;

import com.sinotopia.fundamental.common.utils.NCIISUtils;
import com.sinotopia.fundamental.validate.annotaion.validation.IdentityNo;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdentityNoValidator implements ConstraintValidator<IdentityNo, Object> {
    
    private static final Logger LOG = LoggerFactory.getLogger(IdentityNoValidator.class);
    
    private String idKindField;
    
    private String idNoField;
    
    private String messageTemplate;
    
    @Override
    public void initialize(IdentityNo constraintAnnotation) {
        idKindField = constraintAnnotation.idKindField();
        idNoField = constraintAnnotation.idNoField();
        messageTemplate = constraintAnnotation.message();
    }
    
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (null == value) {
            return true;
        }
        boolean valid = true;
        if (value instanceof String) {
            valid = validNCID((String) value);
        } else {
            try {
                String id_kind = BeanUtils.getProperty(value, idKindField);
                String id_no = BeanUtils.getProperty(value, idNoField);
                if ("0".equals(id_kind)) {
                    valid = validNCID(id_no);
                }
            } catch (Exception e) {
                LOG.error("validate identity failed", e);
            }
        }
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(messageTemplate).addNode(idNoField)
                    .addConstraintViolation();
        }
        return valid;
    }
    
    private boolean validNCID(String idNo) {
        NCIISUtils.NCID ncid = NCIISUtils.parseNCID(idNo, false);
        return null != ncid && ncid.isValid();
    }
    
}
