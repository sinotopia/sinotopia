package com.hkfs.fundamental.session.annotation;

import java.lang.annotation.*;

/**
 * 用户角色
 * Created by brucezee on 2017/2/6.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface UserRole {
    /**
     * 用户所需角色
     * @return
     */
    String[] value();
}
