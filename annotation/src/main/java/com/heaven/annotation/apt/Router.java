package com.heaven.annotation.apt;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者:Heaven
 * 时间: on 2017/3/28 10:26
 * 邮箱:heavenisme@aliyun.com
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface Router{
    String value();
}
