package com.heaven.base.ui.adapter.viewholder;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

/**
 * 作者:Heaven
 * 时间: on 2017/4/12 12:27
 * 邮箱:heavenisme@aliyun.com
 */

interface IMultManager<T> {

    void onBindViewHolder(@NonNull BaseViewHolder<T> holder, @NonNull T t);

    void onBindViewHolder(@NonNull BaseViewHolder<T> holder, @NonNull T t, Object payload);

    View getItemView(ViewGroup parent);

    void setFullSpan(boolean fullSpan);

    boolean isFullSpan();

    int getSpanSize(int spanCount);

    void setSpanSize(int spanSize);

    boolean isClickable();

    boolean isTargetViewType(T item, int position);

    int getMaxRecycleCount();

    void onViewAttachedToWindow(BaseViewHolder holder);
}
