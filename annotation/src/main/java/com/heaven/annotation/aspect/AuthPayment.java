package com.heaven.annotation.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * FileName: com.heaven.annotation.aspect.AuthPayment.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2017-09-26 20:01
 *
 * @version V1.0 支付
 */
@Target(ElementType.METHOD) @Retention(CLASS)
public @interface AuthPayment {
    int type() default AopConstant.WEIXIN;
}
