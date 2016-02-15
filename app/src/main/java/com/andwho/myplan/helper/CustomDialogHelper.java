package com.andwho.myplan.helper;

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
     *//*
    public static Dialog showUniLoadingDialog(Context context, String message, boolean cancelable,
                                              boolean keybackFinish) {
        Activity activity = (Activity) context;
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }
        final Dialog dialog = new Dialog(activity, R.style.uni_load_dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.webview_uni_progress_layout, null);
        final LinearLayout loadingLyt = (LinearLayout) view.findViewById(R.id.uni_loading_lyt);
        LinearLayout loadingLayout = (LinearLayout) view.findViewById(R.id.uni_loading_rl);
        final ImageView img = (ImageView) view.findViewById(R.id.uni_loading_drawable);
        TextView textView = (TextView) view.findViewById(R.id.uni_loading_txt);
        AndroidUtil.setViewBackground(loadingLayout, context.getResources().getDrawable(
                R.drawable.uni_progress_bg));
        textView.setTextColor(context.getResources().getColor(R.color.white));
        final AnimationDrawable mNbDrawable = (AnimationDrawable) img.getDrawable();
        img.post(new Runnable() {

            @Override
            public void run() {
                mNbDrawable.start();
            }
        });
        if (!TextUtils.isEmpty(message)) {
            textView.setText(message);
        }
        ViewTreeObserver observer = loadingLyt.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

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
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        if (mNbDrawable != null) {
                            mNbDrawable.stop();
                        }
                        activity2.finish();
                    }
                    return false;
                }
            });
        }
        return dialog;
    }*/
}
