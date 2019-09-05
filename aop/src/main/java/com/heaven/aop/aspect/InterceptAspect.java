package com.heaven.aop.aspect;

import android.text.TextUtils;

import com.heaven.aop.AopCore;
import com.heaven.aop.annotation.Intercept;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * FileName: com.heaven.aop.aspect.InterceptAspect.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-08-31 20:37
 *
 * @version V1.0 TODO <描述当前版本功能>
 */
@Aspect
public class InterceptAspect {
    @Pointcut("execution(@com.heaven.aop.annotation.Intercept * *(..))")//方法切入点
    public void methodAnnotated() {
    }

    @Around("methodAnnotated() && @annotation(intercept)")
    public Object doInterceptMethod(ProceedingJoinPoint joinPoint, Intercept intercept) throws Throwable {
        if (AopCore.getInterceptor() == null) {
            return joinPoint.proceed();
        }
        int interceptType = intercept.interceptType();
        if (-1 != interceptType){
            //拦截
            boolean result = proceedIntercept(intercept.interceptType(), joinPoint);
            return result ? null : joinPoint.proceed();
        }
        return joinPoint.proceed();
    }

    private boolean proceedIntercept(int interceptType, ProceedingJoinPoint joinPoint) throws Throwable {
        boolean intercept = AopCore.getInterceptor().intercept(interceptType, joinPoint.getSignature().getName());
        if (intercept){
            return true;
        }
        return false;
    }
}
