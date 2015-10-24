package com.andwho.myplan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.andwho.myplan.R;
/**
 * 欢迎页 ouyyx
 */
public class WelcomeAct extends BaseAct {

	private static final String TAG = WelcomeAct.class.getSimpleName();

	private Activity myselfContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_welcome);

		myselfContext = this;

		findViews();
		setListener();
		init();
	}

	private void findViews() {

	}

	private void setListener() {

	}

	private void init() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				gotoIndexAct();
				finish();
			}
		}, 2000);
	}

	private void gotoIndexAct() {
		Intent intent = new Intent(myselfContext, IndexAct.class);
		startActivity(intent);
	}

}
