package com.andwho.myplan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andwho.myplan.R;

/**
 * @author ouyyx 关于我们
 */
public class ProblemsAct extends BaseAct implements OnClickListener {

	private static final String TAG = ProblemsAct.class.getSimpleName();

	private Activity myselfContext;

	private LinearLayout ll_leftIcon;
	private TextView tv_leftIcon;
	private TextView tv_title;
	private ImageView iv_rightIcon;

	private WebView wv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.problems_act);

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

		tv_leftIcon.setText("常见问题");

		tv_title.setVisibility(View.INVISIBLE);
		ll_leftIcon.setVisibility(View.VISIBLE);
	}

	private void findViews() {
		wv = (WebView) this.findViewById(R.id.wv);
	}

	private void setListener() {

	}

	private void init() {

		String targetUrl = "file:///android_asset/help.html";
		// init webview
		wv.getSettings().setJavaScriptEnabled(true); // 设置WebView属性,能够执行Javascript脚本
		if (android.os.Build.VERSION.SDK_INT >= 8) {
			wv.getSettings().setPluginState(PluginState.ON);
		}
		wv.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

		wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //
		wv.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url);

				return true;
			}
		});

		wv.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {

				}
			}
		});
		// time out
		if (!TextUtils.isEmpty(targetUrl)) {
			wv.loadUrl(targetUrl);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
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
}
