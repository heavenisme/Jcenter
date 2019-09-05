package com.heaven.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者:Heaven
 * 时间: on 2017/3/28 10:34
 * 邮箱:heavenisme@aliyun.com
 * 权限请求
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Permission {
    int requestCode() default 1;

    String[] value();

    String rationale() default "";

    boolean force() default false;
}
