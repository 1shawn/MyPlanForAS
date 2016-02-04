package com.andwho.myplan.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andwho.myplan.R;
import com.andwho.myplan.contentprovider.DbManger;
import com.andwho.myplan.model.Plan;
import com.andwho.myplan.preference.MyPlanPreference;
import com.andwho.myplan.utils.DateUtil;
import com.andwho.myplan.utils.MyPlanUtil;
import com.andwho.myplan.view.MpDatePickerDialog;

/**
 * @author ouyyx 关于我们
 */
@SuppressWarnings("ALL")
public class AboutUsAct extends BaseAct implements OnClickListener {

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
		setContentView(R.layout.act_about_us);

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
