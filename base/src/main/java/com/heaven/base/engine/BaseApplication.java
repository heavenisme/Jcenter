package com.heaven.base.engine;

import com.heaven.base.engine.manager.BaseCoreComponent;
import com.heaven.base.engine.manager.DaggerBaseCoreComponent;

import androidx.multidex.MultiDexApplication;

/**
 * FileName: com.heaven.base.BaseEngine.BaseApplication.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-08-02 09:39
 *
 * @version V1.0 TODO <描述当前版本功能>
 */
public class BaseApplication extends MultiDexApplication {
    private static BaseCoreComponent coreComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        coreComponent = DaggerBaseCoreComponent.builder().application(this).build();
        BaseEngine.instance();
    }

    /**
     * 取得代理
     * @return app代理
     */
    public static BaseCoreComponent getBaseCoreComponent() {
        return coreComponent;
    }

}
