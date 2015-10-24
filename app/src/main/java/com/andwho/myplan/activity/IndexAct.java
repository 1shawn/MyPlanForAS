package com.andwho.myplan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.andwho.myplan.R;
import com.andwho.myplan.fragment.MineFrag;
import com.andwho.myplan.fragment.PlanFrag;
import com.andwho.myplan.upgrade.UpgradeUtils;
import com.andwho.myplan.utils.Log;

/**
 * 欢迎页 ouyyx
 */
public class IndexAct extends BaseAct implements OnClickListener {

	private static final String TAG = IndexAct.class.getSimpleName();

	private Activity myselfContext;

	private RelativeLayout rl_mine, rl_plan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_index);

		myselfContext = this;

		findViews();
		setListener();
		init();
	}

	private void findViews() {
		rl_mine = (RelativeLayout) this.findViewById(R.id.rl_mine);
		rl_plan = (RelativeLayout) this.findViewById(R.id.rl_plan);
	}

	private void setListener() {
		rl_mine.setOnClickListener(this);
		rl_plan.setOnClickListener(this);
	}

	private void init() {

		// 检查更新
		UpgradeUtils.checkNewVersion(myselfContext);

		Log.d(TAG, "@@...测试提交到github");
		switchItemSelected(0);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int id = view.getId();
		switch (id) {
		case R.id.rl_mine:
			switchItemSelected(0);
			break;
		case R.id.rl_plan:
			switchItemSelected(1);
			break;
		default:
			break;
		}
	}

	private Fragment content1;
	private Fragment content2;

	private void switchItemSelected(int position) {
		switch (position) {
		case 0:
			content1 = new MineFrag();
			rl_mine.setSelected(true);
			rl_plan.setSelected(false);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.ll_content, content1).commit();
			break;
		case 1:
			content2 = new PlanFrag();
			rl_mine.setSelected(false);
			rl_plan.setSelected(true);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.ll_content, content2).commit();

			break;
		default:
			break;
		}

	}

}
