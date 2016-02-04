package com.andwho.myplan.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.andwho.myplan.R;

/**
 * Created by zhouf on 2016/1/20.
 */
public abstract class ScaleBase<T extends View> extends LinearLayout implements IScale<T> {
    private static final float FRICTION = 2.0f;
    protected T mRootView;
    protected View mHeaderView;
    protected View mScaleView;

    protected int mScreenHeight;
    protected int mScreenWidth;

    private boolean isScaleEnabled = true;
    private boolean isParallax = true;
    private boolean isScaleing = false;
    private boolean isHideHeader = false;

    private int mTouchSlop;
    private boolean mIsBeingDragged = false;
    private float mLastMotionY;
    private float mLastMotionX;
    private float mInitialMotionY;
    private float mInitialMotionX;
    private OnPullScaleListener onPullScaleListener;

    public ScaleBase(Context context) {
        this(context, null);
    }

    public ScaleBase(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setGravity(Gravity.CENTER);

        ViewConfiguration config = ViewConfiguration.get(context);
        mTouchSlop = config.getScaledTouchSlop();

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        mScreenHeight = localDisplayMetrics.heightPixels;
        mScreenWidth = localDisplayMetrics.widthPixels;

        // Refreshable View
        // By passing the attrs, we can add ListView/GridView params via XML
        mRootView = createRootView(context, attrs);

        if (attrs != null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(getContext());
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PullToScaleView);

            int ScaleViewResId = a.getResourceId(R.styleable.PullToScaleView_scaleView, 0);
            if (ScaleViewResId > 0) {
                mScaleView = mLayoutInflater.inflate(ScaleViewResId, null, false);
            }

            int headerViewResId = a.getResourceId(R.styleable.PullToScaleView_headerView, 0);
            if (headerViewResId > 0) {
                mHeaderView = mLayoutInflater.inflate(headerViewResId, null, false);
            }

            isParallax = a.getBoolean(R.styleable.PullToScaleView_isHeaderParallax, true);

            // Let the derivative classes have a go at handling attributes, then
            // recycle them...
            handleStyledAttributes(a);
            a.recycle();
        }
        addView(mRootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void setOnPullScaleListener(OnPullScaleListener onPullScaleListener) {
        this.onPullScaleListener = onPullScaleListener;
    }

    @Override
    public T getPullRootView() {
        return mRootView;
    }

    @Override
    public View getScaleView() {
        return mScaleView;
    }

    @Override
    public View getHeaderView() {
        return mHeaderView;
    }

    @Override
    public boolean isPullToScaleEnabled() {
        return isScaleEnabled;
    }

    @Override
    public boolean isScaleing() {
        return isScaleing;
    }

    @Override
    public boolean isParallax() {
        return isParallax;
    }

    @Override
    public boolean isHideHeader() {
        return isHideHeader;
    }

    public void setScaleEnabled(boolean isScaleEnabled) {
        this.isScaleEnabled = isScaleEnabled;
    }

    public void setParallax(boolean isParallax) {
        this.isParallax = isParallax;
    }

    public void setHideHeader(boolean isHideHeader) {
        this.isHideHeader = isHideHeader;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!isPullToScaleEnabled() || isHideHeader()) {
            return false;
        }

        final int action = event.getAction();

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsBeingDragged = false;
            return false;
        }

        if (action != MotionEvent.ACTION_DOWN && mIsBeingDragged) {
            return true;
        }
        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                if (isReadyForPullStart()) {
                    final float y = event.getY(), x = event.getX();
                    final float diff, oppositeDiff, absDiff;

                    // We need to use the correct values, based on scroll
                    // direction
                    diff = y - mLastMotionY;
                    oppositeDiff = x - mLastMotionX;
                    absDiff = Math.abs(diff);

                    if (absDiff > mTouchSlop && absDiff > Math.abs(oppositeDiff)) {
                        if (diff >= 1f && isReadyForPullStart()) {
                            mLastMotionY = y;
                            mLastMotionX = x;
                            mIsBeingDragged = true;
                        }
                    }
                }
                break;
            }
            case MotionEvent.ACTION_DOWN: {
                if (isReadyForPullStart()) {
                    mLastMotionY = mInitialMotionY = event.getY();
                    mLastMotionX = mInitialMotionX = event.getX();
                    mIsBeingDragged = false;
                }
                break;
            }
        }

        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (!isPullToScaleEnabled() || isHideHeader()) {
            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN && event.getEdgeFlags() != 0) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                if (mIsBeingDragged) {
                    mLastMotionY = event.getY();
                    mLastMotionX = event.getX();
                    pullEvent();
                    isScaleing = true;
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                if (isReadyForPullStart()) {
                    mLastMotionY = mInitialMotionY = event.getY();
                    mLastMotionX = mInitialMotionX = event.getX();
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (mIsBeingDragged) {
                    mIsBeingDragged = false;
                    // If we're already refreshing, just scroll back to the top
                    if (isScaleing()) {
                        smoothScrollToTop();
                        if (onPullScaleListener != null) {
                            onPullScaleListener.onPullScaleEnd();
                        }
                        isScaleing = false;
                        return true;
                    }
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private void pullEvent() {
        final int newScrollValue;
        final float initialMotionValue, lastMotionValue;

        initialMotionValue = mInitialMotionY;
        lastMotionValue = mLastMotionY;

        newScrollValue = Math.round(Math.min(initialMotionValue - lastMotionValue, 0) / FRICTION);

        pullHeaderToScale(newScrollValue);
        if (onPullScaleListener != null) {
            onPullScaleListener.onPullScaleing(newScrollValue);
        }
    }

    protected abstract void pullHeaderToScale(int newScrollValue);

    public abstract void setHeaderView(View headerView);

    public abstract void setScaleView(View ScaleView);

    protected abstract T createRootView(Context context, AttributeSet attrs);

    protected abstract void smoothScrollToTop();

    protected abstract boolean isReadyForPullStart();

    public interface OnPullScaleListener {
        public void onPullScaleing(int newScrollValue);

        public void onPullScaleEnd();
    }
}
