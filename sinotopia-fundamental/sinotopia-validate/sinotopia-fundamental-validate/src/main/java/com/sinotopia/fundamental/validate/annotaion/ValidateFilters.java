package com.sinotopia.fundamental.validate.annotaion;

import com.sinotopia.fundamental.validate.ValidateFilter;

import java.lang.annotation.*;

/**
 * Created by pc on 2016/4/14.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ValidateFilters {

    /**
     * 验证过滤器
     * @return
     */
    Class<? extends ValidateFilter>[] filterClasses() default {};
}
