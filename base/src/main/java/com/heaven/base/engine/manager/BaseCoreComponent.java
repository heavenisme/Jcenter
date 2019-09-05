package com.heaven.base.engine.manager;

import com.heaven.base.engine.BaseApplication;
import com.heaven.base.engine.BaseEngine;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * FileName: com.heaven.news.engine.manager.BaseCoreComponent.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-04-16 11:40
 *
 * @version V1.0 TODO <描述当前版本功能>
 */
@Singleton
@Component(modules = {BaseCoreModule.class})
public interface BaseCoreComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(BaseApplication application);
        BaseCoreComponent build();
    }

    void inject(BaseEngine appEngine);
}
