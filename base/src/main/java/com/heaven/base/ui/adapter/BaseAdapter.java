package com.heaven.base.ui.adapter;

import android.animation.Animator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.heaven.base.ui.adapter.anim.BaseAnimation;
import com.heaven.base.ui.adapter.anim.ScaleInAnimation;
import com.heaven.base.ui.adapter.viewholder.BaseMultItem;
import com.heaven.base.ui.adapter.viewholder.BaseViewHolder;
import com.heaven.base.ui.adapter.viewholder.MultTypeManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * FileName: com.heaven.base.ui.adapter.BaseMultAdapter.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-03-19 17:14
 *
 * @version V1.0 多类型适配器
 */
public class BaseAdapter<E> extends RecyclerView.Adapter<BaseViewHolder<E>> {
    public int groupPosition;
    public int currentSpreadPosition = -1;
    private int currentOldSpreadPosition = -1;
    protected List<E> dataItems = new ArrayList<>();
    protected MultTypeManager multTypeManager = new MultTypeManager();
    protected Context context;
    protected @Nullable
    LayoutInflater inflater;

    private boolean openAnimationEnable = false;
    private boolean firstOnlyEnable = true;
    private int lastPosition = 15;
    private int duration = 300;
    private boolean isStaggeredGridLayout;
    private Interpolator interpolator = new LinearInterpolator();
    private BaseAnimation mCustomAnimation;
    private BaseAnimation mSelectAnimation = new ScaleInAnimation();

    protected OnItemClickListener onItemClickListener;
    protected OnGroupItemClickListener onGroupItemClickListener;

    public BaseAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public BaseAdapter(Context context,List<E> dataItems) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.dataItems = dataItems;
    }

    public <T> BaseAdapter register(@NonNull BaseMultItem<T> multiItem) {
        multTypeManager.register(multiItem.getLayoutBindModel(), multiItem);
        return this;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = dataItems.get(position);
        return multTypeManager.getItemType(item, position);
    }

    @NonNull
    @Override
    public BaseViewHolder<E> onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        BaseMultItem multItem = multTypeManager.getMultItemByType(viewType);
        BaseViewHolder<E> viewHolder;
        if (inflater != null) {
            if (multItem != null) {
                multTypeManager.setMaxRecycledViews(viewGroup, viewType);
                viewHolder = new BaseViewHolder<E>(inflater.inflate(multItem.getItemLayoutId(), viewGroup, false));
                viewHolder.multItem = multItem;
            } else {
                viewHolder = new BaseViewHolder<E>(new TextView(context));
                viewHolder.multItem = multItem;
            }
        } else {
            viewHolder = new BaseViewHolder<E>(new TextView(context));
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

    @Override
    public int getItemCount() {
        if (dataItems != null) {
            return dataItems.size();
        } else {
            return 0;
        }
    }

    public E getItemData(int position) {
        E itemData = null;
        if(dataItems != null && dataItems.size() > 0 && position < dataItems.size()) {
            itemData = dataItems.get(position);
        }
        return itemData;
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            //GridLayoutManager时设置每行的span
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            if(gridManager.getSpanSizeLookup() instanceof GridLayoutManager.DefaultSpanSizeLookup) {
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        BaseMultItem multItem = multTypeManager.getMultItemByType(getItemViewType(position));
                        return multItem.getSpanSize(gridManager.getSpanCount());
                    }
                });
            };
            isStaggeredGridLayout = false;
        } else if (manager instanceof StaggeredGridLayoutManager) {
            isStaggeredGridLayout = true;
        } else if (manager instanceof LinearLayoutManager) {
            isStaggeredGridLayout = false;
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BaseViewHolder<E> holder) {
        super.onViewAttachedToWindow(holder);
        if (isStaggeredGridLayout) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            if (holder.getMultItem() != null) {
                if (holder.getMultItem().isFullSpan()) {
                    p.setFullSpan(true);
                } else {
                    p.setFullSpan(false);
                }
            }
        }
        onViewAttachedToWindow(holder,holder.getLayoutPosition());
        addAnimation(holder);
    }

    public void onViewAttachedToWindow(BaseViewHolder holder, int postion) {
        if (postion < getItemCount()) {
            multTypeManager.getMultItemByType(multTypeManager.getItemType(dataItems.get(postion),postion)).onViewAttachedToWindow(holder);
        }
    }

    public void updateItems(List<E> items) {
        if(items != null && items.size() > 0) {
            this.dataItems = items;
            notifyDataSetChanged();
        }
    }

    public void updateBatch(List<E> data, boolean isRefresh) {
        if(data != null && data.size() > 0) {
            int position = getDataCount();
            if (dataItems == null) {
                dataItems = new ArrayList<>();
            }

            if (isRefresh) {
                dataItems.clear();
                position = 0;
                this.dataItems.addAll(data);
                notifyDataSetChanged();
            } else {
                this.dataItems.addAll(data);
                notifyItemRangeChanged(position, data.size());
            }
        }
    }

    /**
     * @return 除去header footer的个数
     */
    public int getDataCount() {
        return dataItems.size();
    }

    /**
     * @return 获取当前数据源List，不包含head和foot
     */
    public List<E> getDataList() {
        return dataItems;
    }


    public int itemPosition(E item) {
        if(dataItems != null && dataItems.size() > 0) {
           return dataItems.indexOf(item);
        }
        return 0;
    }
    /**
     * 清空Item数据不含head 和 foot
     */
    public void clearData() {
        dataItems.clear();
        notifyDataSetChanged();
    }

    public class DiffCallBack extends DiffUtil.Callback {
        private List<E> diffNewDataItems;

        public DiffCallBack(List<E> diffNewDataItems) {
            this.diffNewDataItems = diffNewDataItems;
        }

        @Override
        public int getOldListSize() {
            return dataItems != null ? dataItems.size() : 0;
        }

        @Override
        public int getNewListSize() {
            return diffNewDataItems != null ? diffNewDataItems.size() : 0;
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            boolean isSame = false;
            int itemType = getItemViewType(oldItemPosition);
            BaseMultItem multItem = multTypeManager.getMultItemByType(itemType);
            if (multItem != null) {
                isSame = multItem.isItemSame(dataItems.get(oldItemPosition), diffNewDataItems.get(newItemPosition));
            }
            return isSame;
        }


        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            boolean isSame = false;
            int itemType = getItemViewType(oldItemPosition);
            BaseMultItem multItem = multTypeManager.getMultItemByType(itemType);
            if (multItem != null) {
                isSame = multItem.isContentSame(dataItems.get(oldItemPosition), diffNewDataItems.get(newItemPosition));
            }
            return isSame;
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
    }

    /**
     * add animation when you want to show time
     *
     * @param holder
     */
    private void addAnimation(RecyclerView.ViewHolder holder) {
        if (openAnimationEnable) {
            if (!firstOnlyEnable || holder.getLayoutPosition() > lastPosition) {
                BaseAnimation animation = null;
                if (mCustomAnimation != null) {
                    animation = mCustomAnimation;
                } else {
                    animation = mSelectAnimation;
                }
                for (Animator anim : animation.getAnimators(holder.itemView)) {
                    startAnim(anim, holder.getLayoutPosition());
                }
                lastPosition = holder.getLayoutPosition();
            }
        }
    }

    /**
     * set anim to start when loading
     *
     * @param anim
     * @param index
     */
    private void startAnim(Animator anim, int index) {
        anim.setDuration(duration).start();
        anim.setInterpolator(interpolator);
    }

    public interface OnItemClickListener<E> {
        void onItemClick(View view, BaseViewHolder holder, E t);

        boolean onItemLongClick(View view, BaseViewHolder holder, E t);

    }

    public interface OnGroupItemClickListener<E> {
        void onItemClick(View view, BaseViewHolder holder, E t);

        boolean onItemLongClick(View view, BaseViewHolder holder, E t);

    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    public void setOnGroupItemClickListener(OnGroupItemClickListener onGroupItemClickListener) {
        this.onGroupItemClickListener = onGroupItemClickListener;
    }

    public void spreadGroup(int newPosition) {
        if(newPosition != currentSpreadPosition) {
            currentSpreadPosition = newPosition;
            notifyItemChanged(currentSpreadPosition);
            notifyItemChanged(currentOldSpreadPosition);
            currentOldSpreadPosition = currentSpreadPosition;
        } else {
            currentSpreadPosition = -1;
            notifyItemChanged(currentOldSpreadPosition);
            currentOldSpreadPosition = -1;
        }
    }
}
