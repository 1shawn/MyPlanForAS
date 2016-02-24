package com.andwho.myplan.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andwho.myplan.R;

/**
 * Created by zhouf on 2016/1/22.
 */
public class CustomDialogHelper {
    /**
     * 统一加载框
     *
     * @param context
     * @param message       加载框文字 null：默认为“加载中…”
     * @param cancelable    是否点返回dismiss
     * @param keybackFinish 是否点返回finish Activity
     * @return 对话框
     */
    public static Dialog showUniLoadingDialog(Context context, String message, boolean cancelable, boolean keybackFinish) {
        Activity activity = (Activity) context;
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }
        final Dialog dialog = new Dialog(activity, R.style.uni_load_dialog);
        final Animation rotateAnimation = AnimationUtils.loadAnimation(activity, R.anim.loading_rotate_anim);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.loading_progress_layout, null);
        final LinearLayout loadingLyt = (LinearLayout) view.findViewById(R.id.uni_loading_lyt);
        final ImageView img = (ImageView) view.findViewById(R.id.uni_loading_drawable);
        TextView textView = (TextView) view.findViewById(R.id.uni_loading_txt);
        img.post(new Runnable(){

            @Override
            public void run() {
                // AnimationDrawable ad = (AnimationDrawable) img.getBackground();
                // ad.start();
                img.startAnimation(rotateAnimation);

            }
        });
        if (message == null) {
            message = "加载中…";
        }
        textView.setText(message);

        ViewTreeObserver observer = loadingLyt.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){

            @Override
            public void onGlobalLayout() {
                loadingLyt.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = loadingLyt.getWidth();
                int height = loadingLyt.getHeight();
                if (width < height) {
                    loadingLyt.setLayoutParams(new FrameLayout.LayoutParams(height, height));
                } else {
                    loadingLyt.setLayoutParams(new FrameLayout.LayoutParams(width, width));
                }
            }
        });

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(cancelable);
        if (cancelable && keybackFinish) {
            final Activity activity2 = (Activity) context;
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener(){

                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        img.clearAnimation();
                        activity2.finish();
                    }
                    return false;
                }
            });
        }
        return dialog;
    }
}
