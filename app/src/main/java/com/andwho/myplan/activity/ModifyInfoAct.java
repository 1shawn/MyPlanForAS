package com.andwho.myplan.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andwho.myplan.R;
import com.andwho.myplan.preference.MyPlanPreference;

/**
 * 编辑计划 ouyyx
 */
public class ModifyInfoAct extends SlideAct implements OnClickListener {

	private static final String TAG = ModifyInfoAct.class.getSimpleName();

	private Activity myselfContext;

	private LinearLayout ll_leftIcon;
	private TextView tv_leftIcon;
	private TextView tv_title;
	private ImageView iv_rightIcon;

	private EditText et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_info_act);

		myselfContext = this;

		initHeader();
		findViews();
		setListener();
		init();
	}

	private String type;

	private void initHeader() {
		ll_leftIcon = (LinearLayout) this.findViewById(R.id.ll_leftIcon);
		tv_leftIcon = (TextView) this.findViewById(R.id.tv_leftIcon);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		iv_rightIcon = (ImageView) this.findViewById(R.id.iv_rightIcon);

		ll_leftIcon.setOnClickListener(this);
		iv_rightIcon.setOnClickListener(this);

		tv_title.setText("编辑");
		ll_leftIcon.setVisibility(View.VISIBLE);
		iv_rightIcon.setVisibility(View.VISIBLE);
		iv_rightIcon.setImageResource(R.drawable.icon_save);
	}

	private void findViews() {
		et = (EditText) this.findViewById(R.id.et);
	}

	private void setListener() {

	}

	private void init() {

		type = this.getIntent().getStringExtra("type");
		if ("nickname".equals(type)) {
			tv_leftIcon.setText("昵称");
			et.setInputType(InputType.TYPE_CLASS_TEXT);

			String nickName = MyPlanPreference.getInstance(myselfContext)
					.getNickname();
			if (!TextUtils.isEmpty(nickName)) {
				et.setText(nickName);
				et.setSelection(nickName.length());
			}
		} else if ("lifespan".equals(type)) {
			tv_leftIcon.setText("生命");
			et.setInputType(InputType.TYPE_CLASS_NUMBER);
			String lifespan = MyPlanPreference.getInstance(myselfContext)
					.getLifeSpan();
			if (!TextUtils.isEmpty(lifespan)) {
				et.setText(lifespan);
				et.setSelection(lifespan.length());
			}
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int id = view.getId();
		switch (id) {
		case R.id.ll_leftIcon:
			hideSoftKeyboard();
			finish();
			break;
		case R.id.iv_rightIcon:
			save();
			break;
		default:
			break;
		}
	}

	private void save() {
		String content = et.getText().toString();
		if (TextUtils.isEmpty(content)) {
			Toast.makeText(myselfContext, "请输入你的计划内容", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		if ("nickname".equals(type)) {
			MyPlanPreference.getInstance(myselfContext).setNickname(content);
		} else if ("lifespan".equals(type)) {
			int life = Integer.parseInt(content);
			if (life <= 0) {
				Toast.makeText(myselfContext, "请输入正确的年龄", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			
			if (life >= 150) {
				Toast.makeText(myselfContext, "没有谁会有150岁的呢", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			
			MyPlanPreference.getInstance(myselfContext).setLifeSpan(content);
		}

		hideSoftKeyboard();
		finish();

	}

	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		showInputMethod();
	}

	private InputMethodManager imm;

	private void showInputMethod() {
		imm = (InputMethodManager) myselfContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(et, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);

	}

	public void hideSoftKeyboard() {
		if (imm != null && imm.isActive()) {
			imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
		}
	}
}
