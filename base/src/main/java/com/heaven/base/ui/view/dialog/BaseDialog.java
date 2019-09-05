package com.heaven.base.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.heaven.base.R;
import com.heaven.base.ui.adapter.viewholder.BaseViewHolder;

import java.lang.reflect.InvocationTargetException;

import androidx.annotation.AnimRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

/**
 * FileName: com.heaven.base.ui.view.dialog.BaseDialog.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-08-05 11:41
 *
 * @version V1.0 TODO <描述当前版本功能>
 */
public class BaseDialog extends Dialog {
    //Dialog样式
    public static int STYLE_BASE = R.style.BaseDialog;    //无任何特性,Dialog基础样式
    public static int STYLE_TRANSLUCENT = R.style.TranslucentDialog;  //在上基础上，弹出时附带黑色背景效果
    public static int STYLE_ALERT = R.style.AlertDialog;  //参照AlertDialog效果，Dialog宽度固定且附带阴影效果

    //弹出动画（包含进入与进出）
    //弹出动画（包含进入与进出）
    public static int ANIMATE_ALPHA = R.anim.alpha_enter;
    public static int ANIMATE_BOTTOM = R.anim.bottom_enter;
    public static int ANIMATE_TOP = R.anim.top_enter;
    public static int ANIMATE_LEFT = R.anim.left_enter;
    public static int ANIMATE_RIGHT = R.anim.right_enter;

    public static int ANIMATE_ALPHA_OUT = R.anim.alpha_exit;
    public static int ANIMATE_BOTTOM_OUT = R.anim.bottom_exit;
    public static int ANIMATE_TOP_OUT = R.anim.top_exit;
    public static int ANIMATE_LEFT_OUT = R.anim.left_exit;
    public static int ANIMATE_RIGHT_OUT = R.anim.right_exit;

    private ViewGroup root;
    private View contentView;
    private Builder builder;
    private Context context;

    private View.OnClickListener dismissListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(builder.showDismissListener != null) {
                builder.showDismissListener.showOrDismiss(false);
            }
            dismiss();
        }
    };

    public BaseDialog(Builder builder) {
        super(builder.context, builder.style);
        this.builder = builder;
        this.context = builder.context;
        initAttr(builder.context);
        initView(builder.context);
    }

    private void initAttr(Context context) {
        Window window = getWindow();
        if (window != null) {
            View decorView = window.getDecorView();
            decorView.setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            params.x = 0;
            params.y = 0;
            window.setAttributes(params);
            //设置dialog沉浸式效果
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    private void initView(Context context) {
        if (builder.contentResId != 0) {
            root = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.base_dialog, null);
            root.setFocusable(false);
            if (builder.contentResId != 0) {
                LayoutInflater.from(context).inflate(builder.contentResId, root);
                if (root.getChildCount() > 0) {
                    contentView = root.getChildAt(0);
                }
                Animation animation = AnimationUtils.loadAnimation(context, builder.animate);
                contentView.setAnimation(animation);
                contentView.getViewTreeObserver().addOnGlobalLayoutListener(this::calculateContentHeight);
                if (contentView != null) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentView.getLayoutParams();
                    if (params != null) {
                        params.gravity = builder.gravity;
                        root.requestLayout();
                    }
                    bindContentViewData();
                }
            }
            setContentView(root);
        }
    }

    private void bindContentViewData() {
        if (contentView != null) {
            BaseViewHolder viewHolder = new BaseViewHolder(contentView);
            builder.baseViewHolder = viewHolder;
            if (builder.bindViewDataStr.size() > 0) {
                int totalIndex = builder.bindViewDataStr.size();
                for (int index = 0; index < totalIndex; index++) {
                    int key = builder.bindViewDataStr.keyAt(0);
                    String value = builder.bindViewDataStr.get(key);
                    viewHolder.setText(key, value);
                }
            }

            if (builder.bindViewDataInt.size() > 0) {
                int totalIndex = builder.bindViewDataInt.size();
                for (int index = 0; index < totalIndex; index++) {
                    int key = builder.bindViewDataInt.keyAt(0);
                    int value = builder.bindViewDataInt.get(key);
                    viewHolder.setText(key, context.getResources().getString(value));
                }
            }

            if (builder.bindViewColor.size() > 0) {
                int totalIndex = builder.bindViewColor.size();
                for (int index = 0; index < totalIndex; index++) {
                    int key = builder.bindViewColor.keyAt(0);
                    int value = builder.bindViewColor.get(key);
                    viewHolder.setText(key, context.getResources().getColor(value));
                }
            }

            if (builder.viewIds != null && builder.viewIds.length > 0 && builder.listener != null) {
                for (int viewId : builder.viewIds) {
                    viewHolder.setOnClickListener(viewId, builder.listener);
                }
            }

            if(builder.dimissViewId != null && builder.dimissViewId.length > 0) {
                for(int dismissId : builder.dimissViewId) {
                    viewHolder.setOnClickListener(dismissId,dismissListener);
                }
            }
        }
    }

    private void calculateContentHeight() {
        if (contentView != null) {
            int contentHeight = contentView.getMeasuredHeight();
            int contentWidth = contentView.getMeasuredWidth();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentView.getLayoutParams();
            if (params != null) {
                if (builder.maxHeight > 0) {
                    if (contentHeight > builder.maxHeight) {
                        params.height = builder.maxHeight;
                        contentView.setLayoutParams(params);
                    }
                }

                if (builder.minHeight > 0) {
                    if (contentHeight < builder.minHeight) {
                        params.height = builder.minHeight;
                        contentView.setLayoutParams(params);
                    }
                }

                if (builder.maxWidth > 0) {
                    if (contentWidth > builder.maxWidth) {
                        params.width = builder.maxWidth;
                        contentView.setLayoutParams(params);
                    }
                }

                if (builder.minWidth > 0) {
                    if (contentWidth < builder.minWidth) {
                        params.width = builder.minWidth;
                        contentView.setLayoutParams(params);
                    }
                }
            }

        }
    }


    public static final class Builder {
        private BaseViewHolder baseViewHolder;
        private BaseDialog dialog;
        public Context context;
        int style = STYLE_BASE;
        int contentResId;
        boolean isFull;
        int gravity = Gravity.CENTER;
        int backGroudColorId;
        int[] viewIds;
        View.OnClickListener listener;
        SparseArray<String> bindViewDataStr = new SparseArray<>();
        SparseIntArray bindViewDataInt = new SparseIntArray();
        SparseIntArray bindViewColor = new SparseIntArray();
        int maxHeight;
        int minHeight;
        int maxWidth;
        int minWidth;
        boolean cancelOutSide = true;
        int animate = ANIMATE_ALPHA;
        int animateExit = ANIMATE_ALPHA_OUT;
        private View mAnchorView;
        private DialogShowDismissListener showDismissListener;
        int[] dimissViewId;
        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTheme(int style) {
            this.style = style;
            return this;
        }

        public Builder setContentId(@LayoutRes int contentId) {
            this.contentResId = contentId;
            return this;
        }

        public Builder setBackGroudColorId(@ColorRes int colorId) {
            this.backGroudColorId = colorId;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setOnClickListener(View.OnClickListener listener, @IdRes int... viewId) {
            this.listener = listener;
            this.viewIds = viewId;
            return this;
        }

        public Builder setDismissIds(int ...dismissIds) {
            this.dimissViewId = dismissIds;
            return this;
        }

        public Builder bindViewData(@IdRes int viewId, String data) {
            bindViewDataStr.put(viewId, data);
            return this;
        }

        public Builder bindViewData(@IdRes int viewId, @StringRes int strId) {
            bindViewDataInt.put(viewId, strId);
            return this;
        }

        public Builder bindViewColor(@IdRes int viewId, @ColorInt int colorId) {
            bindViewColor.put(viewId, colorId);
            return this;
        }

        public Builder setCancelOutSide(boolean canCancel) {
            this.cancelOutSide = canCancel;
            return this;
        }

        public Builder setMaxHeight(int maxHeight) {
            this.maxHeight = maxHeight;
            return this;
        }

        public Builder setMaxWidth(int maxWidth) {
            this.maxWidth = maxWidth;
            return this;
        }

        public Builder setMinHeight(int minHeight) {
            this.minHeight = minHeight;
            return this;
        }

        public Builder setMinWidth(int minWidth) {
            this.minWidth = minWidth;
            return this;
        }

        public Builder setShowDismissListener(DialogShowDismissListener showDismissListener) {
            this.showDismissListener = showDismissListener;
            return this;
        }

        public Builder setAnim(@AnimRes int anim) {
            this.animate = anim;
            if (anim == ANIMATE_ALPHA) {
                this.animateExit = ANIMATE_ALPHA_OUT;
            } else if (anim == ANIMATE_BOTTOM) {
                this.animateExit = ANIMATE_BOTTOM_OUT;
            } else if (anim == ANIMATE_TOP) {
                this.animateExit = ANIMATE_TOP_OUT;
            } else if (anim == ANIMATE_LEFT) {
                this.animateExit = ANIMATE_LEFT_OUT;
            } else if (anim == ANIMATE_RIGHT) {
                this.animateExit = ANIMATE_RIGHT_OUT;
            }
            return this;
        }

        public Builder setIsFull(boolean isFull) {
            this.isFull = isFull;
            return this;
        }

        public Builder setmAnchorView(View mAnchorView) {
            this.mAnchorView = mAnchorView;
            return this;
        }

        public View getmAnchorView() {
            return mAnchorView;
        }

        @SuppressWarnings(value = {"unchecked"})
        public <T extends View> T getView(int id) {
            if (baseViewHolder != null) {
                return (T) baseViewHolder.getView(id);
            }
            return null;
        }

        public BaseViewHolder getBaseViewHolder() {
            return baseViewHolder;
        }

        public DialogShowDismissListener getShowDismissListener() {
            return showDismissListener;
        }

        public Builder create() {
            dialog = new BaseDialog(this);
            return this;
        }

        public <T extends BaseDialog> BaseDialog create(Class<T> baseDialog) {
            Class[] classes = new Class[]{Builder.class};
            try {
                dialog = baseDialog.getConstructor(classes).newInstance(this);
                return dialog;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            dialog = new BaseDialog(this);
            return dialog;
        }

        public void dismissDialog() {
            if(dialog != null) {
                dialog.dismiss();
            }
        }

        public void showDialog() {
            if(dialog != null) {
                dialog.show();
            }
        }

    }

   public interface DialogShowDismissListener {
        void showOrDismiss(boolean isShow);
    }
}
