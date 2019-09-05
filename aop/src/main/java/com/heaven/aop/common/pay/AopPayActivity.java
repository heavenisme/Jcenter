package com.heaven.aop.common.pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.heaven.aop.R;

import androidx.annotation.Nullable;

/**
 * FileName: com.heaven.aop.common.pay.AopPayActivity.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-08-30 15:13
 *
 * @author heaven
 * @version V1.0 TODO <描述当前版本功能>
 */
public class AopPayActivity extends Activity {
    private static AuthPaymentInfo authPaymentInfo;
    private static AuthPaymentInfo.PayResultListener payResultListener;

    public static void pay(Context context, AuthPaymentInfo authPaymentInfo, AuthPaymentInfo.PayResultListener payResultListener) {
        if (authPaymentInfo != null) {
            AopPayActivity.authPaymentInfo = authPaymentInfo;
            AopPayActivity.payResultListener = payResultListener;
            Intent intent = new Intent(context, AopPayActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            if (context instanceof Activity) {
                ((Activity) context).overridePendingTransition(0, 0);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hpermission);
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if (authPaymentInfo != null && payResultListener != null && !TextUtils.isEmpty(authPaymentInfo.signData)) {
            payOrder(authPaymentInfo);
        } else {
            finish();
            overridePendingTransition(0, 0);
        }
    }

    private void payOrder(AuthPaymentInfo authPaymentInfo) {
        AuthPaymentUtil.authPayMoney(this, authPaymentInfo, payResultListener);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    public interface PayResultListener {
        void payResult(AuthPaymentInfo authPaymentInfo);
    }

}
