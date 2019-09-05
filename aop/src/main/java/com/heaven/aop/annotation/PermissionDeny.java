package com.heaven.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * FileName: com.heaven.aop.annotation.PermissionDeny.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-08-30 15:55
 *
 * @version V1.0 TODO <描述当前版本功能>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionDeny {
}
