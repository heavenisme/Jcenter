package com.heaven.aop.aspect;//package com.heaven.news.aop;

import android.os.CountDownTimer;


import com.heaven.aop.aspect.model.CountDownTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * FileName: com.heaven.flybetter.aop.ShareAspect.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2017-09-05 19:52
 *
 * @version V1.0 分享功能
 */
@Aspect
public class CountDownAspect {
    private static final int THUMB_SIZE = 150;

    @Pointcut("execution(@com.heaven.aop.annotation.CountDown * *(..))")//方法切入点
    public void methodAnnotated() {
    }


    @Around(value = "methodAnnotated()")
    public Object countDownTimer(final ProceedingJoinPoint joinPoint) throws Throwable {
        CountDownTime countDownTime = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof CountDownTime) {
                countDownTime = (CountDownTime) arg;
            }
        }
        if (countDownTime != null) {
            final CountDownTime finalCountDownTime = countDownTime;
            new CountDownTimer(finalCountDownTime.countDownTime, 1000) {
                @Override
                public void onTick(long currentTime) {
                    try {
                        finalCountDownTime.currentCountTime = currentTime;
                        joinPoint.proceed(new Object[]{finalCountDownTime});
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }

                @Override
                public void onFinish() {
                    try {
                        finalCountDownTime.isFinish = true;
                        joinPoint.proceed(new Object[]{finalCountDownTime});
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }.start();
        }

        return null;
    }
}
