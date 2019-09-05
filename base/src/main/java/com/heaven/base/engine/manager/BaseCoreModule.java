package com.heaven.base.engine.manager;

import android.content.Context;

import com.heaven.base.engine.BaseApplication;

import dagger.Module;
import dagger.Provides;

/**
 * FileName: com.heaven.news.engine.manager.BaseCoreModule.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-04-16 11:35
 *
 * @version V1.0 TODO <描述当前版本功能>
 */
@Module
public class BaseCoreModule {

    @Provides
    Context provideContext(BaseApplication application) {
        return application.getApplicationContext();
    }
}
