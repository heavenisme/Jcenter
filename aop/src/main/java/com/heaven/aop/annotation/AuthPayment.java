package com.heaven.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * FileName: com.heaven.news.aop.aspect.AuthPayment.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-06-26 11:27
 *
 * @version V1.0 TODO <描述当前版本功能>
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface AuthPayment {
}
