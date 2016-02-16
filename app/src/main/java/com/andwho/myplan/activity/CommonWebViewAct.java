package com.andwho.myplan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andwho.myplan.R;
import com.andwho.myplan.model.Banner;


/**
 * Created by ys_1shawn on 2016/2/16.
 */

public class CommonWebViewAct extends BaseAct implements View.OnClickListener {

    private static final String TAG = CommonWebViewAct.class.getSimpleName();

    private Activity myselfContext;

    private LinearLayout ll_leftIcon;
    private TextView tv_leftIcon;
    private TextView tv_title;
    private ImageView iv_rightIcon;

    private LinearLayout ll_progressBar;
    private LinearLayout ll_noContent;
    private WebView webview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_webview_act);

        myselfContext = this;

        initHeader();
        findViews();
        setListener();
        init();
    }


    private void initHeader() {
        ll_leftIcon = (LinearLayout) this.findViewById(R.id.ll_leftIcon);
        tv_leftIcon = (TextView) this.findViewById(R.id.tv_leftIcon);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        iv_rightIcon = (ImageView) this.findViewById(R.id.iv_rightIcon);

        ll_leftIcon.setOnClickListener(this);

        tv_leftIcon.setText("");
        tv_title.setText("");
        ll_leftIcon.setVisibility(View.VISIBLE);
        iv_rightIcon.setVisibility(View.GONE);
        iv_rightIcon.setImageResource(R.drawable.icon_save);
    }

    private void findViews() {
        ll_progressBar = (LinearLayout) findViewById(R.id.ll_progressBar);
        ll_noContent = (LinearLayout) findViewById(R.id.ll_noContent);
        webview = (WebView) findViewById(R.id.webview);
    }

    private void setListener() {

    }

    private void init() {

        Banner banner = (Banner) myselfContext.getIntent()
                .getSerializableExtra("Banner");
        if (banner != null) {
            tv_title.setText(banner.title);
            if (!TextUtils.isEmpty(banner.detailURL)) {
                initPage(banner.detailURL);
            } else {
                initPage("http://www.baidu.com");
            }
        } else {
            tv_title.setText("");
            initPage("http://www.baidu.com");
        }
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int id = view.getId();
        switch (id) {
            case R.id.ll_leftIcon:
                finish();
                break;

            default:
                break;
        }
    }


    private void initPage(String url) {
        webview.setVisibility(View.GONE);
        ll_noContent.setVisibility(View.GONE);
        ll_progressBar.setVisibility(View.VISIBLE);

        webview.getSettings().setJavaScriptEnabled(true); //
        // 设置WebView属性,能够执行Javascript脚本
        if (android.os.Build.VERSION.SDK_INT >= 8) {
            webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        }

        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                // Log.d(TAG, "@@...shouldOverrideUrlLoading ...url = " + url);

                view.loadUrl(url);

                return true;
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            ll_progressBar.setVisibility(View.GONE);
                            webview.setVisibility(View.VISIBLE);
                        }
                    }, 0);
                }
            }
        });

        // Log.d(TAG, "@@...loadUrl ...url = " + url);

        webview.loadUrl(url);
    }


    public boolean onKeyDown(int keyCoder, KeyEvent event) {
        if (webview.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
            webview.goBack();
        } else {
            onBackPressed();
        }
        return true;
    }

}
