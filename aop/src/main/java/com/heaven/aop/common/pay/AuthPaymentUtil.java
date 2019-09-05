package com.heaven.aop.common.pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.heaven.aop.R;
import com.heaven.aop.annotation.PayResult;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * FileName: com.heaven.flybetter.aop.AuthPaymentUtil.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2017-09-27 10:41
 *
 * @version V1.0 支付统一回调
 */
public class AuthPaymentUtil {
    private static AuthPaymentInfo mAuthPaymentInfo;
    private static AuthPaymentInfo.PayResultListener payResultListener;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    static void authPayMoney(Activity context, AuthPaymentInfo authPaymentInfo, AuthPaymentInfo.PayResultListener payResultListener) {
        AuthPaymentUtil.mAuthPaymentInfo = authPaymentInfo;
        AuthPaymentUtil.payResultListener = payResultListener;
        if (authPaymentInfo.payType == AuthPaymentInfo.WEIXIN) {//微信支付
            weChatPay(context, authPaymentInfo);
            Log.i("authPayMoney", "微信支付" + authPaymentInfo.toString());
        } else if (authPaymentInfo.payType == AuthPaymentInfo.ALIPAY) {//阿里支付
            aliPay(context, authPaymentInfo);
            Log.i("authPayMoney", "支付宝支付" + authPaymentInfo.toString());
        }
    }

    private static void weChatPay(@NonNull Activity currentContext, @NonNull AuthPaymentInfo authPaymentInfo) {
        if (!TextUtils.isEmpty(authPaymentInfo.signData)) {
            Gson gson = new Gson();
            WeixinSign weixinSign = gson.fromJson(authPaymentInfo.signData, WeixinSign.class);
            IWXAPI api = WXAPIFactory.createWXAPI(currentContext, weixinSign.getAppid());
            if (api.isWXAppInstalled()) {
                //将应用的APPId注册到微信
                api.registerApp(weixinSign.getAppid());
                PayReq req = new PayReq();
                req.appId = weixinSign.getAppid();
                req.partnerId = weixinSign.getPartnerid();
                req.prepayId = weixinSign.getPrepayid();
                req.nonceStr = weixinSign.getNoncestr();
                req.timeStamp = weixinSign.getTimestamp();
                req.packageValue = weixinSign.getPackageX();
                req.sign = weixinSign.getSign();
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                api.sendReq(req);
            } else {
                paymentResult(false, "-1", "微信SDK支付签名异常");
            }
            currentContext.finish();
            currentContext.overridePendingTransition(0, 0);
        }
    }


    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        @Override
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayAliResult payResult = new PayAliResult((String) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.result;// 同步返回需要验证的信息
                    String resultStatus = payResult.resultStatus;
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        paymentResult(true, resultStatus, TextUtils.isEmpty(resultInfo) ? "支付成功" : resultInfo);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        paymentResult(false, resultStatus, TextUtils.isEmpty(resultInfo) ? "支付失败" : resultInfo);
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AliAuthResult authResult = new AliAuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                    } else {
                        // 其他状态值则为授权失败
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    private static void aliPay(@NonNull Activity currentContext, @NonNull AuthPaymentInfo authPaymentInfo) {
        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(currentContext);
                String result = alipay.pay(authPaymentInfo.signData,true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
//        currentContext.finish();
//        currentContext.overridePendingTransition(0, 0);
    }

    public static void paymentResult(boolean isSuccess, String codeStatus, @Nullable String errorMsg) {
        if (mAuthPaymentInfo != null && payResultListener != null) {
            mAuthPaymentInfo.isPaySuccess = isSuccess;
            mAuthPaymentInfo.codeStatus = codeStatus;
            mAuthPaymentInfo.errorMsg = errorMsg;
            payResultListener.payResult(mAuthPaymentInfo);
            mAuthPaymentInfo = null;
            payResultListener = null;
        }
    }

}
