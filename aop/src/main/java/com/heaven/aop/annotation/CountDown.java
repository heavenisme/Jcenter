package com.heaven.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * FileName: com.heaven.news.aop.annotation.CountDown.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-08-26 11:14
 *
 * @version V1.0 TODO <描述当前版本功能>
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface CountDown {

}
