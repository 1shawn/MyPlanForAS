package com.andwho.myplan.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by zhouf on 2016/1/20.
 */
public abstract class FVBaseView extends LinearLayout {

    protected Context mContext;
    protected FVBaseLine mFVBaseLine;

    public FVBaseView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public FVBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initAttrs(attrs);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public FVBaseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initAttrs(attrs);
        initView();
    }

    protected void initFVBaseLine(ViewGroup viewGroup, AttributeSet attrs){
        mFVBaseLine = new FVBaseLine(mContext, viewGroup, attrs);
    }

    protected abstract void initAttrs(AttributeSet attrs);
    protected abstract void initView();
}

