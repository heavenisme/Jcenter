package com.heaven.base.engine;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.heaven.base.engine.manager.BaseCoreComponent;

import java.lang.ref.WeakReference;
import java.util.Stack;

import javax.inject.Inject;

/**
 * FileName: com.heaven.base.engine.BaseEngine.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-08-02 09:48
 *
 * @version V1.0 TODO <描述当前版本功能>
 */
public class BaseEngine implements Application.ActivityLifecycleCallbacks {
    /**
     * 应用程序引擎.
     */
    private static WeakReference<BaseEngine> engineRef;

    @Inject
    BaseApplication context;

    private Activity curActivity;

    /**
     * 当前activity存储栈.
     */
    private static Stack<Activity> store;

    /**
     * 私有构造方法.
     */
    private BaseEngine() {
        init();
    }

    private void init() {
        store = new Stack<>();
        //上下文代理
        BaseCoreComponent coreComponent = BaseApplication.getBaseCoreComponent();
        coreComponent.inject(this);
        context.registerActivityLifecycleCallbacks(this);
    }

    /**
     * 引擎单类.
     *
     * @return 返回引擎
     */
    public static BaseEngine instance() {
        if (engineRef == null) {
            synchronized (BaseEngine.class) {
                if (engineRef == null) {
                    engineRef = new WeakReference<>(new BaseEngine());
                }
            }
        } else {
            if (engineRef.get() == null) {
                synchronized (BaseEngine.class) {
                    if (engineRef.get() == null) {
                        engineRef = new WeakReference<>(new BaseEngine());
                    }
                }
            }
        }
        return engineRef.get();
    }

    public Context getContext() {
        return context;
    }

    public Activity getCurActivity() {
        return curActivity;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        this.curActivity = activity;
        store.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        store.remove(activity);
    }

    public void exitApp() {
        if(store.size() > 0) {
            for(Activity activity : store) {
                activity.finish();
            }
        }
    }
}
