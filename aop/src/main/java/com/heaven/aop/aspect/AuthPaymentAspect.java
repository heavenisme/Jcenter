package com.heaven.aop.aspect;

import android.content.Context;
import android.text.TextUtils;

import com.heaven.aop.AopCore;
import com.heaven.aop.annotation.PayResult;
import com.heaven.aop.common.pay.AopPayActivity;
import com.heaven.aop.common.pay.AuthPaymentInfo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 作者:Heaven
 * 时间: on 2017/3/28 10:34
 * 邮箱:heavenisme@aliyun.com
 * 权限请求
 */
@Aspect
public class AuthPaymentAspect {
    @Pointcut("execution(@com.heaven.aop.annotation.AuthPayment * *(..))")//方法切入点
    public void methodAnnotated() {
    }


    @Around("methodAnnotated()")//在连接点进行方法替换
    public Object aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Context context = null;
        final Object object = joinPoint.getThis();
        if (object instanceof Context) {
            context = (Context) object;
        } else  if (object instanceof androidx.fragment.app.Fragment) {
            context = ((androidx.fragment.app.Fragment) object).getActivity();
        } else {
            //获取切入点方法上的参数列表
            Object[] objects = joinPoint.getArgs();
            if (objects.length > 0) {
                //非静态方法且第一个参数为context
                if (objects[0] instanceof Context) {
                    context = (Context) objects[0];
                } else {
                    //没有传入context 默认使用application
                    context = AopCore.getContext();
                }
            } else {
                context = AopCore.getContext();
            }
        }
        if (context == null) {
            return result;
        }

        if(result instanceof AuthPaymentInfo) {
            AuthPaymentInfo authPaymentInfo = (AuthPaymentInfo) result;
            AuthPaymentInfo.PayResultListener payResultListener = new AuthPaymentInfo.PayResultListener() {
                @Override
                public void payResult(AuthPaymentInfo authPaymentInfo) {
                    Class<?> cls = object.getClass();
                    Method[] methods = cls.getDeclaredMethods();
                    if (methods.length == 0) {
                        return;
                    }
                    for (Method method : methods) {
                        //过滤不含自定义注解PermissionNoAskDenied的方法
                        boolean isHasAnnotation = method.isAnnotationPresent(PayResult.class);
                        if (isHasAnnotation) {
                            method.setAccessible(true);
                            //获取方法类型
                            Class<?>[] types = method.getParameterTypes();
                            if (types.length != 1) {
                                return;
                            }
                            //获取方法上的注解
                            PayResult aInfo = method.getAnnotation(PayResult.class);
                            if (aInfo == null) {
                                return;
                            }

                            //解析注解上对应的信息
                            try {
                                method.invoke(object, authPaymentInfo);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };
            if(!TextUtils.isEmpty(authPaymentInfo.signData)) {
                AopPayActivity.pay(context, authPaymentInfo,payResultListener);
            }
        }
        return result;
    }
}


