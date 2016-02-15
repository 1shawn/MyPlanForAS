package com.andwho.myplan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * Created by ys_1shawn on 2016/2/14.
 */

/**
 * 调整Gallery左右滑动时的速度，每滑动一次，仅显示上/下一张
 */

public class SlowGallery extends Gallery {
    public SlowGallery(Context context) {
        super(context);
    }

    public SlowGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlowGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
        return e2.getX() > e1.getX();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        int keyCode;
        if (isScrollingLeft(e1, e2)) {
            keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
        } else {
            keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
        }
        onKeyDown(keyCode, null);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return super.onKeyDown(keyCode, event);
    }

}
