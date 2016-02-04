package com.andwho.myplan.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andwho.myplan.R;

/**
 * 列表小组件
 * Created by zhouf on 2016/1/20.
 */
public class FVItemSmall extends FVBaseView {

    private boolean hasLeftRedDot = false;
    private boolean hasRightRedDotCount = false;
    private boolean hasRightText = false;

    private boolean hasTopLine = false;
    private boolean hasCloseBottomLine = false;
    private boolean hasMarginBottomLine = false;

    private LayoutInflater inflater;
    private ImageView leftIcon;
    private TextView tv;
    private RelativeLayout dotCountArea;
    private TextView dotCount;
    private TextView rightTv;
    private ImageView leftDot;

    public FVItemSmall(Context context) {
        super(context);
    }

    public FVItemSmall(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FVItemSmall(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void initAttrs(AttributeSet attrs){
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.FVItemSmall);
        hasLeftRedDot = typedArray.getBoolean(R.styleable.FVItemSmall_hasLeftRedDot, false);
        hasRightRedDotCount = typedArray.getBoolean(R.styleable.FVItemSmall_hasRightRedDotCount, false);
        hasRightText = typedArray.getBoolean(R.styleable.FVItemSmall_hasRightText, false);

        hasTopLine = typedArray.getBoolean(R.styleable.FVItemSmall_hasTopLine, false);
        hasCloseBottomLine = typedArray.getBoolean(R.styleable.FVItemSmall_hasCloseBottomLine, false);
        hasMarginBottomLine = typedArray.getBoolean(R.styleable.FVItemSmall_hasMarginBottomLine, false);

        typedArray.recycle();
    }

    @Override
    protected void initView(){
        setOrientation(VERTICAL);
        inflater = LayoutInflater.from(mContext);
        View view  = inflater.inflate(R.layout.view_item_small, this, true);
        leftIcon = (ImageView)view.findViewById(R.id.view_item_small_left_icon);
        tv = (TextView)view.findViewById(R.id.view_item_small_tv);
        dotCountArea = (RelativeLayout)findViewById(R.id.view_item_small_dot_count_area);
        dotCount = (TextView)findViewById(R.id.view_item_small_dot_count);
        rightTv = (TextView)findViewById(R.id.view_item_small_right_tv);
        leftDot = (ImageView)findViewById(R.id.view_item_small_left_dot);

        setLeftDotVisible(hasLeftRedDot);
        setRightRedDotCountVisible(hasRightRedDotCount);
        setRightTextVisible(hasRightText);

        addTopLine();
        addCloseBottomLine();
        addMarginBottomLine();
    }

    private void addTopLine(){
        if (hasTopLine){
            View topLine = inflater.inflate(R.layout.divider_closed_more, this, false);
            addView(topLine, 0);
        }
    }

    private void addCloseBottomLine(){
        if (hasCloseBottomLine){
            View closeBottomLine = inflater.inflate(R.layout.divider_closed_more, this, false);
            addView(closeBottomLine);
        }
    }

    private void addMarginBottomLine(){
        if (hasMarginBottomLine){
            View marginBottomLine = inflater.inflate(R.layout.divider_margin_55, this, false);
            addView(marginBottomLine);
        }
    }

    /**
     * 设置左边红点是否显示
     * @param hasLeftRedDot true则显示，false不显示
     */
    public  void setLeftDotVisible(boolean hasLeftRedDot){
        if (hasLeftRedDot){
            leftDot.setVisibility(View.VISIBLE);
        }else{
            leftDot.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右边红点数是否显示
     * @param hasRightRedDotCount true则显示，false不显示
     */
    public  void setRightRedDotCountVisible(boolean hasRightRedDotCount){
        if (hasRightRedDotCount){
            dotCountArea.setVisibility(View.VISIBLE);
        }else{
            dotCountArea.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右边文字是否显示
     * @param hasRightText  true则显示，false不显示
     */
    public  void setRightTextVisible(boolean hasRightText){
        if (hasRightText){
            rightTv.setVisibility(View.VISIBLE);
        }else{
            rightTv.setVisibility(View.GONE);
        }
    }

    /**
     * 设置左边图标
     * @param leftIconSrc 图标资源
     */
    public void setLeftIcon(int leftIconSrc){
        leftIcon.setImageResource(leftIconSrc);
    }

    /**
     * 设置左边文字
     * @param content 文字
     */
    public void setLeftTv(String content){
        tv.setText(content);
    }

    /**
     * 设置右边红点数
     * @param count 数量
     */
    public void setDotCount(String count){
        dotCount.setText(count);
    }

    /**
     * 设置右边文字
     * @param content 文字
     */
    public void setRightTv(String content){
        rightTv.setText(content);
    }

    /**
     * 设置右边文字颜色
     * @param color 文字颜色
     */
    public void setRightTvColor(int color){
        rightTv.setTextColor(color);
    }

}
