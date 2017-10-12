package com.heaven.annotation.aspect;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * 作者:Heaven
 * 时间: on 2017/9/15 14:25
 * 邮箱:heavenisme@aliyun.com
 * TD数据统计
 */
@Target({TYPE, METHOD, CONSTRUCTOR}) @Retention(CLASS)
public @interface DataCollect {
    String type();
    String describe();
}
