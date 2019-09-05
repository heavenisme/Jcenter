package com.heaven.aop.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者:Heaven
 * 时间: on 2017/3/28 10:35
 * 邮箱:heavenisme@aliyun.com
 * 防止连续点击
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SingleClick {
    long value() default  500L;

    int[] ids() default {View.NO_ID};
}
