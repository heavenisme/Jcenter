package com.heaven.annotation.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * FileName: com.heaven.annotation.aspect.Share.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2017-09-05 19:45
 *
 * @version V1.0 分享功能
 */
@Target(ElementType.METHOD) @Retention(CLASS)
public @interface Share {
    int type() default AopConstant.WEIXIN;
}
