package com.sinotopia.fundamental.session.annotation;

import java.lang.annotation.*;

/**
 * 用户类型
 * Created by brucezee on 2017/2/6.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface UserType {
    /**
     * 用户所需类型
     * @return
     */
    String[] value();
}
