package com.hkfs.fundamental.validate.aspect;

import com.hkfs.fundamental.api.data.DataObjectBase;
import com.hkfs.fundamental.common.utils.PrintUtils;
import com.hkfs.fundamental.validate.utils.ValidateUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ValidateAspect {
    
    private static final String CLASS_NAME = ValidateAspect.class.getName();
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CLASS_NAME);
    
    public ValidateAspect() {
        PrintUtils.printComponentLoaded(LOGGER,CLASS_NAME);
    }
    
    public Object validateParam(ProceedingJoinPoint pjp) throws Throwable {
        for (Object arg : pjp.getArgs()) {
            if (arg instanceof DataObjectBase) {
            	ValidateUtils.validOrThrowException(arg);
            } else {
                // ignore;
            }
        }
        
        return pjp.proceed();
    }
    
}
