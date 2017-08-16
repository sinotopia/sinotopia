package com.sinotopia.fundamental.validate.annotaion;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by pc on 2016/4/14.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ValidateFilterBean {
}
