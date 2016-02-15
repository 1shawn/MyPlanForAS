package com.andwho.myplan.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by ys_1shawn on 2016/2/14.
 */
public class DensityUtils {
    private static Context mContext;

    /**
     * dp单位转px
     *
     * @param dpValue
     * @return
     */
    public static int dip2px(float dpValue) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, mContext.getResources().getDisplayMetrics());
        // final float scale =
        // mcontext.getResources().getDisplayMetrics().density;
        // return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px单位转dp
     *
     * @param pxValue
     * @return
     */
    public static int px2dip(float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 初始化本工具
     *
     * @param context
     */
    public static void setContext(Context context) {
        mContext = context;
    }

//    /**
//     * 获得标题栏的高度
//     *
//     * @return
//     */
//    public static int getTitleHeight() {
//        return mContext.getResources().getDimensionPixelSize(
//                R.dimen.title_height);
//    }
//
//    public static int getTitleHeightDp() {
//        return px2dip(getTitleHeight());
//    }

    /**
     * 获得屏幕宽度(单位dp)
     *
     * @return
     */
    public static int getScreenWidthDp() {
        return mContext.getResources().getConfiguration().screenWidthDp;
    }

    /**
     * 获得屏幕高度(单位dp)
     *
     * @return
     */
    public static int getScreenHeightDp() {
        return mContext.getResources().getConfiguration().screenHeightDp;
    }

    public static int getStatusBarHeight() {
        return mContext.getResources().getDisplayMetrics().heightPixels
                - getScreenHeight();

    }

    /**
     * 真正整个屏幕(含状态栏)的高度
     *
     * @return
     */
    public static int getAllHeightPixels() {
        return mContext.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获得屏幕宽度(单位px)
     *
     * @return
     */
    public static int getScreenWidth() {
        return dip2px(mContext.getResources().getConfiguration().screenWidthDp);
    }

    /**
     * 获得屏幕高度(单位px)
     *
     * @return
     */
    public static int getScreenHeight() {
        return dip2px(mContext.getResources().getConfiguration().screenHeightDp);
    }
//
//    /**
//     * 获得屏幕除却标题栏和通知栏外的余下高度(单位px)
//     *
//     * @return
//     */
//    public static int getUIBodyHeight() {
//        return getScreenHeight() - getTitleHeight();
//    }
//
//    /**
//     * 获得屏幕除却标题栏和通知栏外的余下高度(单位dp)
//     *
//     * @return
//     */
//    public static int getUIBodyHeightDp() {
//        return px2dip(getScreenHeight() - getTitleHeight());
//    }
//    // public static int getWindowHeight(){
//    //
//    // }
}
