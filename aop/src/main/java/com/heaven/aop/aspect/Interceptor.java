package com.heaven.aop.aspect;

/**
 * description $desc$
 * created by jerry on 2019/6/3.
 */
public interface Interceptor {
    boolean intercept(int key, String methodName) throws Throwable;
}
