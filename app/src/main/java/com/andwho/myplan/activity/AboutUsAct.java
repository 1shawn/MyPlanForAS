package com.andwho.myplan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andwho.myplan.R;
import com.andwho.myplan.utils.MyPlanUtil;

/**
 * @author ouyyx 关于我们
 */
@SuppressWarnings("ALL")
public class AboutUsAct extends SlideAct implements OnClickListener {

	private static final String TAG = AboutUsAct.class.getSimpleName();

	private Activity myselfContext;

	private LinearLayout ll_leftIcon;
	@SuppressWarnings("FieldCanBeLocal")
	private TextView tv_leftIcon;
	private TextView tv_title;
	private ImageView iv_rightIcon;

	private TextView tv_version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us_act);
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
		tv_leftIcon.setText("关于我们");
		tv_title.setVisibility(View.INVISIBLE);
		ll_leftIcon.setVisibility(View.VISIBLE);
	}

	private void findViews() {
		tv_version = (TextView) this.findViewById(R.id.tv_version);
	}

	private void setListener() {

	}

	private void init() {
	 
		tv_version.setText("当前版本：" + MyPlanUtil.getSoftVersion(myselfContext));
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
