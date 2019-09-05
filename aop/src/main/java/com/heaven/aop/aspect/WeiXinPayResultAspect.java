package com.heaven.aop.aspect;//package com.heaven.news.aop;

import android.app.Activity;
import android.os.CountDownTimer;

import com.heaven.aop.aspect.model.CountDownTime;
import com.heaven.aop.common.pay.AuthPaymentUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseResp;

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
public class WeiXinPayResultAspect {
    @Pointcut("execution(@com.heaven.aop.annotation.WeiXinPayResult * *(..))")//方法切入点
    public void methodAnnotated() {
    }


    @Around(value = "methodAnnotated()")
    public void payResult(final ProceedingJoinPoint joinPoint) throws Throwable {
        BaseResp payResult = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof BaseResp) {
                payResult = (BaseResp) arg;
            }
        }
        if(payResult != null) {
            if (payResult.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
                String msg = "";
                if (payResult.errCode == 0) {
                    AuthPaymentUtil.paymentResult(true,String.valueOf(payResult.errCode),"");
                } else if (payResult.errCode == -2) {
                    AuthPaymentUtil.paymentResult(false,String.valueOf(payResult.errCode),"用户中途取消支付操作");
                } else {
                    AuthPaymentUtil.paymentResult(false,String.valueOf(payResult.errCode),"微信支付失败");
                }
            }
        }
      Object object = joinPoint.getThis();
        if(object instanceof Activity) {
            ((Activity)object).finish();
        }
    }
}
