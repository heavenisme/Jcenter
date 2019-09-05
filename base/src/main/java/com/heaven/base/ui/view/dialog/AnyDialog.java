package com.heaven.base.ui.view.dialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.heaven.base.R;
import com.heaven.base.engine.BaseEngine;
import com.heaven.base.ui.adapter.viewholder.BaseViewHolder;
import com.heaven.base.ui.view.SystemBarTintManager;

import androidx.annotation.AnimRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;


/**
 * FileName: com.heaven.base.ui.view.dialog.AnyDialog.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-08-01 16:08
 *
 * @version V1.0 TODO <描述当前版本功能>
 */
public class AnyDialog extends AppCompatActivity {
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

    private static Builder dialogBuilder = null;
    private ViewGroup root;
    private View contentView;

    private static void show(Builder builder) {
        dialogBuilder = builder;
        Intent intent = new Intent(BaseEngine.instance().getContext(), AnyDialog.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        BaseEngine.instance().getContext().startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(dialogBuilder.style);
        initMmersionTitleBar();
        if (dialogBuilder != null) {
            dialogBuilder.tipDialog = this;
            root = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.base_dialog, null);
            if (dialogBuilder.contentResId != 0) {
                LayoutInflater.from(this).inflate(dialogBuilder.contentResId, root);
                if (root.getChildCount() > 0) {
                    contentView = root.getChildAt(0);
                }
                Animation animation = AnimationUtils.loadAnimation(this, dialogBuilder.animate);
                contentView.setAnimation(animation);
                contentView.getViewTreeObserver().addOnGlobalLayoutListener(this::calculateContentHeight);
            }
            initView();
            getWindow().getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            getWindow().setAttributes(lp);
            getWindow().setContentView(root);
            getWindow().getDecorView().requestLayout();
        }
    }

    protected void initMmersionTitleBar() {
        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        if (dialogBuilder.backGroudColorId != 0) {
            tintManager.setStatusBarAlpha((float) 0.001);
//            tintManager.setStatusBarTintColor(getResources().getColor(dialogBuilder.backGroudColorId));
        } else {
            tintManager.setStatusBarAlpha((float) 0.001);
        }
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void initView() {
        initRootView();
        initContentView();
        bindContentViewData();
    }


    private void initRootView() {
        ViewGroup.LayoutParams rootParam = root.getLayoutParams();
        if (dialogBuilder.isFull) {
            if (rootParam != null) {
                rootParam.width = ViewGroup.LayoutParams.MATCH_PARENT;
                rootParam.height = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        } else {
            if (rootParam != null) {
                rootParam.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                rootParam.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            } else {
                root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }

        if (dialogBuilder.backGroudColorId != 0) {
            root.findViewById(R.id.root).setBackgroundColor(getResources().getColor(dialogBuilder.backGroudColorId));
        }
    }

    private void initContentView() {
        if (contentView != null) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentView.getLayoutParams();
            if (params != null) {
                params.gravity = dialogBuilder.gravity;
                root.requestLayout();
            }
        }
    }

    private void calculateContentHeight() {
        if (contentView != null) {
            int contentHeight = contentView.getMeasuredHeight();
            int contentWidth = contentView.getMeasuredWidth();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentView.getLayoutParams();
            if (params != null) {
                if (dialogBuilder.maxHeight > 0) {
                    if (contentHeight > dialogBuilder.maxHeight) {
                        params.height = dialogBuilder.maxHeight;
                        contentView.setLayoutParams(params);
                    }
                }

                if (dialogBuilder.minHeight > 0) {
                    if (contentHeight < dialogBuilder.minHeight) {
                        params.height = dialogBuilder.minHeight;
                        contentView.setLayoutParams(params);
                    }
                }

                if (dialogBuilder.maxWidth > 0) {
                    if(contentWidth > dialogBuilder.maxWidth) {
                        params.width = dialogBuilder.maxWidth;
                        contentView.setLayoutParams(params);
                    }
                }

                if (dialogBuilder.minWidth > 0) {
                    if(contentWidth < dialogBuilder.minWidth) {
                        params.width = dialogBuilder.minWidth;
                        contentView.setLayoutParams(params);
                    }
                }
            }

        }
    }

    private void bindContentViewData() {
        if (contentView != null) {
            BaseViewHolder viewHolder = new BaseViewHolder(contentView);
            dialogBuilder.baseViewHolder = viewHolder;
            if (dialogBuilder.bindViewDataStr.size() > 0) {
                int totalIndex = dialogBuilder.bindViewDataStr.size();
                for (int index = 0; index < totalIndex; index++) {
                    int key = dialogBuilder.bindViewDataStr.keyAt(0);
                    String value = dialogBuilder.bindViewDataStr.get(key);
                    viewHolder.setText(key, value);
                }
            }

            if (dialogBuilder.bindViewDataInt.size() > 0) {
                int totalIndex = dialogBuilder.bindViewDataInt.size();
                for (int index = 0; index < totalIndex; index++) {
                    int key = dialogBuilder.bindViewDataInt.keyAt(0);
                    int value = dialogBuilder.bindViewDataInt.get(key);
                    viewHolder.setText(key, getString(value));
                }
            }

            if (dialogBuilder.bindViewColor.size() > 0) {
                int totalIndex = dialogBuilder.bindViewColor.size();
                for (int index = 0; index < totalIndex; index++) {
                    int key = dialogBuilder.bindViewColor.keyAt(0);
                    int value = dialogBuilder.bindViewColor.get(key);
                    viewHolder.setText(key, getResources().getColor(value));
                }
            }

            if (dialogBuilder.viewIds != null && dialogBuilder.viewIds.length > 0 && dialogBuilder.listener != null) {
                for (int viewId : dialogBuilder.viewIds) {
                    viewHolder.setOnClickListener(viewId, dialogBuilder.listener);
                }
            }
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void finish() {
        super.finish();
        Animation animation = AnimationUtils.loadAnimation(this, dialogBuilder.animateExit);
        contentView.startAnimation(animation);
        overridePendingTransition(AnyDialog.ANIMATE_ALPHA_OUT, AnyDialog.ANIMATE_ALPHA_OUT);
        dialogBuilder = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (dialogBuilder.cancelOutSide) {
                return super.onKeyDown(keyCode, event);
            } else {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void finishSelf() {
        Animation animation = AnimationUtils.loadAnimation(this, dialogBuilder.animateExit);
        contentView.startAnimation(animation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static final class Builder {
        public static int PHONE_TYPE = 1;
        AnyDialog tipDialog;

        public enum ORIENTATION {
            ABOVE,
            BELOW,
            LEFT,
            RIGHT,
        }


        Context context;
        int style = R.style.HalfTransparentActivity;
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
        private BaseViewHolder baseViewHolder;

        public Builder() {

        }

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

        @SuppressWarnings(value = {"unchecked"})
        public <T extends View> T getView(int id) {
            if (baseViewHolder != null) {
                return (T) baseViewHolder.getView(id);
            }
            return null;
        }

        public void show() {
            AnyDialog.show(this);
        }

        public void dismiss() {
            if (tipDialog != null) {
                tipDialog.finish();
                tipDialog = null;
                baseViewHolder = null;
                listener = null;
            }
        }
    }
}
