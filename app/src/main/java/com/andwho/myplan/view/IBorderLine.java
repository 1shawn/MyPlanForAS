package com.andwho.myplan.view;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by zhouf on 2016/1/20.
 */
public interface  IBorderLine {

    void addTopLine(LayoutInflater inflater);
    void addBottomLine(LayoutInflater inflater);
    void setCloseTopLineVisible(boolean visible);
    void setMarginTopLineVisible(boolean visible);
    void setCloseBottomLineVisible(boolean visible);
    void setMarginBottomLineVisible(boolean visible);

    interface IInitBorderLine{
        void initFVBaseLine(ViewGroup viewGroup, AttributeSet attrs);
    }
}