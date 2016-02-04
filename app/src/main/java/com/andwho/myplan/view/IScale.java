package com.andwho.myplan.view;

import android.content.res.TypedArray;
import android.view.View;

/**
 * Created by zhouf on 2016/1/20.
 */
public interface IScale<T extends View> {

    public View getScaleView();

    public View getHeaderView();

    public T getPullRootView();

    public boolean isPullToScaleEnabled();

    public boolean isScaleing();

    public boolean isParallax();

    public boolean isHideHeader();

    public void handleStyledAttributes(TypedArray a);
}

