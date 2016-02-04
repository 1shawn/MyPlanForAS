package com.andwho.myplan.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.andwho.myplan.R;

/**
 * Created by zhouf on 2016/1/20.
 */
public class FVBaseLine implements IBorderLine{

    public float topLineLeftMargin;
    public float topLineRightMargin;
    public float bottomLineLeftMargin;
    public float bottomLineRightMargin;
    public boolean hasCloseTopLine;
    public boolean hasMarginTopLine;
    public boolean hasCloseBottomLine;
    public boolean hasMarginBottomLine;
    public View closeBottomLine;
    public View marginBottomLine;
    public View closeTopLine;
    public View marginTopLine;
    public ViewGroup mViewGroup;

    public FVBaseLine(Context context, ViewGroup viewGroup, AttributeSet attrs){
        mViewGroup = viewGroup;
        initAttrs(context, attrs);
    }

    private void initAttrs(Context mContext, AttributeSet attrs){
        TypedArray typedArray = mContext
                .obtainStyledAttributes(attrs, R.styleable.FVBaseLine);
        topLineLeftMargin = typedArray
                .getDimensionPixelSize(R.styleable.FVBaseLine_bl_topLineLeftMargin, 0);
        topLineRightMargin = typedArray
                .getDimensionPixelSize(R.styleable.FVBaseLine_bl_topLineRightMargin, 0);
        bottomLineLeftMargin = typedArray
                .getDimensionPixelSize(R.styleable.FVBaseLine_bl_bottomLineLeftMargin, 0);
        bottomLineRightMargin = typedArray
                .getDimensionPixelSize(R.styleable.FVBaseLine_bl_bottomLineRightMargin, 0);
        hasCloseTopLine = typedArray
                .getBoolean(R.styleable.FVBaseLine_bl_hasCloseTopLine, false);
        hasMarginTopLine = typedArray
                .getBoolean(R.styleable.FVBaseLine_bl_hasMarginTopLine, false);
        hasCloseBottomLine = typedArray
                .getBoolean(R.styleable.FVBaseLine_bl_hasCloseBottomLine, false);
        hasMarginBottomLine = typedArray
                .getBoolean(R.styleable.FVBaseLine_bl_hasMarginBottomLine, false);
        typedArray.recycle();
    }

    @Override
    public void addTopLine(LayoutInflater inflater){
        if (hasCloseTopLine){
            LinearLayout lineArea = (LinearLayout) inflater
                    .inflate(R.layout.divider_closed_more, mViewGroup, false);
            mViewGroup.addView(lineArea);
            closeTopLine = lineArea;
        }
        if (hasMarginTopLine){
            LinearLayout lineArea = (LinearLayout) inflater
                    .inflate(R.layout.divider_closed_more, mViewGroup, false);
            int leftMargin = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_PX,
                    topLineLeftMargin,
                    mViewGroup.getResources().getDisplayMetrics());
            int rightMargin = (int)TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_PX,
                    topLineRightMargin,
                    mViewGroup.getResources().getDisplayMetrics());
            lineArea.setPadding(leftMargin, 0, rightMargin, 0);
            mViewGroup.addView(lineArea);
            marginTopLine = lineArea;
        }
    }

    @Override
    public void addBottomLine(LayoutInflater inflater){
        if (hasCloseBottomLine){
            LinearLayout lineArea = (LinearLayout) inflater
                    .inflate(R.layout.divider_closed_more, mViewGroup, false);
            mViewGroup.addView(lineArea);
            closeBottomLine = lineArea;
        }
        if (hasMarginBottomLine){
            LinearLayout lineArea = (LinearLayout) inflater
                    .inflate(R.layout.divider_closed_more, mViewGroup, false);
            int leftMargin = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_PX,
                    bottomLineLeftMargin,
                    mViewGroup.getResources().getDisplayMetrics());
            int rightMargin = (int)TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_PX,
                    bottomLineRightMargin,
                    mViewGroup.getResources().getDisplayMetrics());
            lineArea.setPadding(leftMargin, 0, rightMargin, 0);
            mViewGroup.addView(lineArea);
            marginBottomLine = lineArea;
        }
    }

    @Override
    public void setCloseTopLineVisible(boolean visible){
        if (closeTopLine == null)
            return;
        if (visible){
            closeTopLine.setVisibility(View.VISIBLE);
        }else {
            closeTopLine.setVisibility(View.GONE);
        }
    }

    @Override
    public void setMarginTopLineVisible(boolean visible){
        if (marginTopLine == null)
            return;
        if (visible){
            marginTopLine.setVisibility(View.VISIBLE);
        }else {
            marginTopLine.setVisibility(View.GONE);
        }
    }

    @Override
    public void setCloseBottomLineVisible(boolean visible){
        if (closeBottomLine == null)
            return;
        if (visible){
            closeBottomLine.setVisibility(View.VISIBLE);
        }else {
            closeBottomLine.setVisibility(View.GONE);
        }
    }

    @Override
    public void setMarginBottomLineVisible(boolean visible){
        if (marginBottomLine == null)
            return;
        if (visible){
            marginBottomLine.setVisibility(View.VISIBLE);
        }else {
            marginBottomLine.setVisibility(View.GONE);
        }
    }

}

