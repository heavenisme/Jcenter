package com.heaven.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者:Heaven
 * 时间: on 2017/3/28 10:35
 * 邮箱:heavenisme@aliyun.com
 * 耗时跟踪
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface TraceTime {
}
