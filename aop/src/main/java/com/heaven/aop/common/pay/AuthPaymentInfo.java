package com.heaven.aop.common.pay;

/**
 * FileName: com.heaven.flybetter.aop.AuthPaymentInfo.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2017-09-26 20:20
 *
 * @version V1.0 支付信息
 */
public class AuthPaymentInfo {
    public static final int WEIXIN = 1;
    public static final int QQ = 2;
    public static final int ALIPAY = 3;
    public int payType = WEIXIN;
    public String appId;//必填
    public String partnerId;//必填
    public String signData;//必填
    public int orderType;//订单类型
    public boolean isPaySuccess;
    public String codeStatus;
    public String errorMsg;

    public interface PayResultListener{
        void payResult(AuthPaymentInfo authPaymentInfo);
    }
}
