package com.heaven.aop;

import android.annotation.SuppressLint;
import android.content.Context;

import com.heaven.aop.aspect.Interceptor;

/**
 * FileName: com.heaven.aop.AopCore.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-08-30 14:36
 *
 * @version V1.0 TODO <描述当前版本功能>
 */
public class AopCore {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static Interceptor sInterceptor;

    static void init(Context context){
        mContext = context;
    }

    public static Context getContext(){
        if (mContext == null){
            throw new IllegalStateException("please init AopArms");
        }
        return mContext;
    }

    public static void setInterceptor(Interceptor interceptor){
        sInterceptor = interceptor;
    }

    public static Interceptor getInterceptor(){
        return sInterceptor;
    }
}
