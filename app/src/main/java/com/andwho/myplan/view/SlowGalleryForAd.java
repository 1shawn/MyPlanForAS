package com.andwho.myplan.view;


import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ys_1shawn on 2016/2/14.
 */

public class SlowGalleryForAd extends SlowGallery {

    public SlowGalleryForAd(Context context) {
        super(context);
    }

    public SlowGalleryForAd(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlowGalleryForAd(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // private static final String TAG = SlowGallery.class.getSimpleName();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        Rect rect = new Rect();
        this.getHitRect(rect);

        float x = 0;
        float y = 0;

        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            x = event.getX();
            y = event.getY();

            if (listener != null) {
                listener.onTouchDownOrMove();
            }
        } else if (action == MotionEvent.ACTION_MOVE) {

            x = event.getX();
            y = event.getY();

            if (listener != null) {
                listener.onTouchDownOrMove();
            }
        } else if (action == MotionEvent.ACTION_UP) {
            if (listener != null) {
                listener.onTouchUp();
            }
        }

        // Log.d(TAG, "@@...scs...rect.left = " + rect.left +
        // "  --rect.right = "
        // + rect.right + "  --rect.top = " + rect.top
        // + "   -- rect.bottom" + rect.bottom);
        // Log.d(TAG, "@@...scs...x = " + x + "  y =" + y);

        boolean isIn = false;
        if (x > rect.left && x < rect.right && y > rect.top && y < rect.bottom) {
            isIn = true;
        }

        if (listener != null) {
            listener.onTouchInGallery(isIn);
        }

        return super.onTouchEvent(event);

    }

    private OnGalleryTouchListener listener;

    public void setOnGalleryTouchListener(OnGalleryTouchListener listener) {
        this.listener = listener;
    }

    public interface OnGalleryTouchListener {
        public void onTouchInGallery(boolean isIn);

        public void onTouchDownOrMove();

        public void onTouchUp();
    }
}