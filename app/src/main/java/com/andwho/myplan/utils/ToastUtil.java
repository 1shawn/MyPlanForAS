package com.andwho.myplan.utils;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andwho.myplan.R;

/**
 * Created by zhouf on 2016/1/21.
 */
public class ToastUtil {
    private static Object object = new Object();

    private static Toast mToast = null;

    private static Toast mCustomToast = null;

    public static Toast getmToast() {
        return mToast;
    }

    public static void showLongToast(Context c, String message) {
        showToast(c, message, Toast.LENGTH_LONG);
    }

    public static void showLongToast(Context c, int resId) {
        showToast(c, c.getString(resId), Toast.LENGTH_LONG);
    }

    public static void showShortToast(Context c, String message) {
        showToast(c, message, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(Context c, int resId) {
        showToast(c, c.getString(resId), Toast.LENGTH_SHORT);
    }

    public static long showSameToastTime = 0;
    public static String lastToastMsg = "";
    public static void showToast(Context c, String msg, int duration) {
        if (c == null) {
            return;
        }
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {// 非UI主线程
            return;
        }
        synchronized (object) {
            long currentShowTime = System.currentTimeMillis();
            if (!msg.equals(lastToastMsg) ||
                    (currentShowTime - showSameToastTime) > 1800){
                showSameToastTime = currentShowTime;
                lastToastMsg = msg;
                if (mToast != null &&
                        mToast.getView() != null &&
                        mToast.getView().getParent() == null) {
                    View v = mToast.getView();
                    TextView tv = (TextView)v.findViewById(R.id.toast_msg);
                    tv.setText(msg);
                    mToast.setDuration(duration);
                } else {
                    if (mToast != null)
                        mToast.cancel();
                    mToast = new Toast(c.getApplicationContext());
                    LayoutInflater inflate = LayoutInflater.from(c);
                    View v = inflate.inflate(R.layout.toast_layout, null);
                    TextView tv = (TextView)v.findViewById(R.id.toast_msg);
                    tv.setText(msg);
                    mToast.setView(v);
                    mToast.setDuration(duration);
                }
                mToast.show();
            }
        }
    }

    /**
     * 自定义View的toast
     */
    public static void showCustomToast(Context context, View view, int duration, int gravity) {
        synchronized (object) {
            if (mCustomToast == null) {
                mCustomToast = new Toast(context.getApplicationContext());
            }
            mCustomToast.setView(view);
            mCustomToast.setGravity(gravity, 0, 0);
            mCustomToast.setDuration(duration);
            mCustomToast.show();
        }
    }
}
