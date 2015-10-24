package com.andwho.myplan.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andwho.myplan.R;
import com.andwho.myplan.activity.IntentHelper;
 

/**
 * @author ouyyx 计划
 */
public class PlanFrag extends MyPlanViewPagerFrag implements OnClickListener {

	private static final String TAG = PlanFrag.class.getSimpleName();

	private Activity myselfContext;
	private TextView tv_title;
	private ImageView iv_rightIcon;
	private RelativeLayout rl_everyday_plan, rl_longterm_plan;
	private View v_everyday_plan, v_longterm_plan;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		myselfContext = this.getActivity();
		View view = findViews(inflater, container);
		setListener();
		init();
		return view;
	}

	private View findViews(LayoutInflater inflater, ViewGroup container) {

		View view = inflater.inflate(R.layout.frag_plan, container, false);
		initHeader(view);

		rl_everyday_plan = (RelativeLayout) view
				.findViewById(R.id.rl_everyday_plan);
		rl_longterm_plan = (RelativeLayout) view
				.findViewById(R.id.rl_longterm_plan);

		v_everyday_plan = (View) view.findViewById(R.id.v_everyday_plan);
		v_longterm_plan = (View) view.findViewById(R.id.v_longterm_plan);

		initViewPager(view);

		return view;
	}

	private void initHeader(View view) {
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText("计划");
		iv_rightIcon = (ImageView) view.findViewById(R.id.iv_rightIcon);
		iv_rightIcon.setVisibility(View.VISIBLE);
		iv_rightIcon.setImageResource(R.drawable.icon_add);
		iv_rightIcon.setOnClickListener(this);
	}

	private void setListener() {
		rl_everyday_plan.setOnClickListener(this);
		rl_longterm_plan.setOnClickListener(this);
	}

	private void init() {
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int id = view.getId();
		switch (id) {
		case R.id.rl_everyday_plan:
			setTabSelectedStatus(0);
			setPageSelected(0);
			break;
		case R.id.rl_longterm_plan:
			setTabSelectedStatus(1);
			setPageSelected(1);
			break;
		case R.id.iv_rightIcon:
			IntentHelper.showPlanEdit(myselfContext, planType);
			break;
		default:
			break;
		}
	}

	private String planType = "1";

	private void setTabSelectedStatus(int position) {
		switch (position) {
		case 0:
			planType = "1";
			rl_everyday_plan.setSelected(true);
			rl_longterm_plan.setSelected(false);
			v_everyday_plan.setVisibility(View.VISIBLE);
			v_longterm_plan.setVisibility(View.INVISIBLE);
			break;
		case 1:
			planType = "0";
			rl_everyday_plan.setSelected(false);
			rl_longterm_plan.setSelected(true);
			v_everyday_plan.setVisibility(View.INVISIBLE);
			v_longterm_plan.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	@Override
	public void setTabSelected(int position) {
		// TODO Auto-generated method stub
		setTabSelectedStatus(position);
	}

	@Override
	public Fragment[] getFragments() {
		// TODO Auto-generated method stub
		Fragment[] frags = new Fragment[2];
		frags[0] = new EverydayPlanFrag();
		frags[1] = new LongtermPlanFrag();
		return frags;
	}

	@Override
	public ViewGroup getContainer(View view) {
		// TODO Auto-generated method stub
		return (LinearLayout) view.findViewById(R.id.ll_content2);
	}
}
