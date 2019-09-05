package com.heaven.base.ui.view.widget.popwindow;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Created by gavin
 * Created date 17/9/13
 */


@IntDef({
        HorizontalPosition.CENTER,
        HorizontalPosition.LEFT,
        HorizontalPosition.RIGHT,
        HorizontalPosition.ALIGN_LEFT,
        HorizontalPosition.ALIGN_RIGHT,
})
@Retention(RetentionPolicy.SOURCE)
public @interface HorizontalPosition {
    int CENTER = 0;
    int LEFT = 1;
    int RIGHT = 2;
    int ALIGN_LEFT = 3;
    int ALIGN_RIGHT = 4;
}