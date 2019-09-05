package com.heaven.base.vm;

import android.app.Application;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

/**
 * FileName: com.heaven.base.vm.BaseContextViewModel.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-02-25 13:56
 *
 * @version V1.0 TODO <描述当前版本功能>
 */
public abstract class BaseViewModel extends ViewModel implements IViewModel {
   public Application application;
   public LifecycleOwner owner;
   @Override
   public void inject() {

   }
}
