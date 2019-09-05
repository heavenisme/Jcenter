package com.heaven.aop.aspect;

import android.content.Context;


import com.heaven.aop.AopCore;
import com.heaven.aop.annotation.Permission;
import com.heaven.aop.annotation.PermissionDeny;
import com.heaven.aop.common.permission.IHpermission;
import com.heaven.aop.common.permission.PermissionDenyResult;
import com.heaven.aop.common.permission.PermissionInfo;
import com.heaven.aop.common.permission.AopPermissionActivity;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 作者:Heaven
 * 时间: on 2017/3/28 10:34
 * 邮箱:heavenisme@aliyun.com
 * 权限请求
 */
@Aspect
public class SysPermissionAspect {
    @Pointcut("execution(@com.heaven.aop.annotation.Permission * *(..))")//方法切入点
    public void methodAnnotated() {
    }


    @Around("methodAnnotated() && @annotation(permission)")//在连接点进行方法替换
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint, Permission permission) throws Throwable {
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
        if (context == null || permission == null) {
            joinPoint.proceed();
            return;
        }

        PermissionInfo permissionInfo = new PermissionInfo();
        permissionInfo.permissions = permission.value();
        permissionInfo.requestCode = permission.requestCode();
        permissionInfo.rationale = permission.rationale();
        permissionInfo.isForce = permission.force();
        permissionInfo.permissionListener = new IHpermission() {
            @Override
            public void permissionGranted() {
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void permissionNeverAskDenied(int requestCode, List<String> denyNeverAskList) {
                if(!permissionInfo.isForce) {
                    try {
                        joinPoint.proceed();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    return;
                }
                Class<?> cls = object.getClass();
                Method[] methods = cls.getDeclaredMethods();
                if (methods.length == 0) {
                    return;
                }
                for (Method method : methods) {
                    //过滤不含自定义注解PermissionNoAskDenied的方法
                    boolean isHasAnnotation = method.isAnnotationPresent(PermissionDeny.class);
                    if (isHasAnnotation) {
                        method.setAccessible(true);
                        //获取方法类型
                        Class<?>[] types = method.getParameterTypes();
                        if (types.length != 1) {
                            return;
                        }
                        //获取方法上的注解
                        PermissionDeny aInfo = method.getAnnotation(PermissionDeny.class);
                        if (aInfo == null) {
                            return;
                        }

                        PermissionDenyResult denyResult = new PermissionDenyResult();
                        denyResult.isNeverAskDeny = true;
                        denyResult.requestCode = requestCode;
                        denyResult.denyPermissionList = denyNeverAskList;
                        //解析注解上对应的信息
                        try {
                            method.invoke(object, denyResult);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void permissionDenied(int requestCode, List<String> denyList) {
                if(!permissionInfo.isForce) {
                    try {
                        joinPoint.proceed();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    return;
                }
                Class<?> cls = object.getClass();
                Method[] methods = cls.getDeclaredMethods();
                if (methods.length == 0) {
                    return;
                }
                for (Method method : methods) {
                    //过滤不含自定义注解PermissionDenied的方法
                    boolean isHasAnnotation = method.isAnnotationPresent(PermissionDeny.class);
                    if (isHasAnnotation) {
                        method.setAccessible(true);
                        //获取方法类型
                        Class<?>[] types = method.getParameterTypes();
                        if (types.length != 1) {
                            return;
                        }
                        //获取方法上的注解
                        PermissionDeny aInfo = method.getAnnotation(PermissionDeny.class);
                        if (aInfo == null) {
                            return;
                        }

                        PermissionDenyResult denyResult = new PermissionDenyResult();
                        denyResult.isNeverAskDeny = true;
                        denyResult.requestCode = requestCode;
                        denyResult.denyPermissionList = denyList;
                        //解析注解上对应的信息
                        try {
                            method.invoke(object, denyResult);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        AopPermissionActivity.requestPermission(context,permissionInfo);
    }
}


