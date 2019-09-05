package com.heaven.base.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.heaven.base.ui.adapter.viewholder.BaseMultItem;
import com.heaven.base.ui.adapter.viewholder.BaseViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * FileName: com.heaven.base.ui.adapter.BindAdapter.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-08-29 10:38
 *
 * @version V1.0 TODO <描述当前版本功能>
 */
public class BindAdapter<BV extends ViewDataBinding,E> extends BaseAdapter<E> {

    public BindAdapter(Context context) {
        super(context);
    }

    public BindAdapter(Context context, List<E> dataItems) {
        super(context, dataItems);
    }

    @NonNull
    @Override
    public BaseViewHolder<E> onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        BaseMultItem multItem = multTypeManager.getMultItemByType(viewType);
        BaseViewHolder<E> viewHolder;
        if (inflater != null) {
            if (multItem != null) {
                multTypeManager.setMaxRecycledViews(viewGroup, viewType);
                viewHolder = new BindViewHolder(inflater.inflate(multItem.getItemLayoutId(), viewGroup, false));
                viewHolder.multItem = multItem;
            } else {
                viewHolder = new BindViewHolder(new TextView(context));
                viewHolder.multItem = multItem;
            }
        } else {
            viewHolder = new BindViewHolder(new TextView(context));
            viewHolder.multItem = multItem;
        }
        viewHolder.baseAdapter = this;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<E> holder, int position) {
        final E item = dataItems.get(position);
        holder.groupPosition = groupPosition;
        BaseMultItem<E> binder = multTypeManager.getMultItemByType(holder.getItemViewType());
        if (binder != null) {
            if (onItemClickListener != null) {
                holder.onItemClickListener = onItemClickListener;
            }
            if (onGroupItemClickListener != null) {
                holder.onGroupItemClickListener = onGroupItemClickListener;
            }

            binder.onBindViewHolder(holder, item);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<E> holder, int position, @NonNull List<Object> payloads) {
        final E item = dataItems.get(position);
        holder.groupPosition = groupPosition;
        BaseMultItem<E> binder = multTypeManager.getMultItemByType(holder.getItemViewType());
        holder.itemData = item;
        if (binder != null) {
            if (onItemClickListener != null) {
                holder.onItemClickListener = onItemClickListener;
            }

            if (onGroupItemClickListener != null) {
                holder.onGroupItemClickListener = onGroupItemClickListener;
            }

            if(payloads.isEmpty()) {
                binder.onBindViewHolder(holder, item);
            } else {
                binder.onBindViewHolder(holder, item, payloads);
            }
        }
    }

    public class BindViewHolder extends BaseViewHolder<E> {
        public BindViewHolder(View itemView) {
            super(itemView);
            bindView = DataBindingUtil.bind(itemView);
        }
    }
}
