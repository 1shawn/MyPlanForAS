package com.andwho.myplan.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andwho.myplan.R;


/**
 * @author ouyyx 计划
 */
public class PlanFrag extends BaseFrag implements OnClickListener {

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

        View view = inflater.inflate(R.layout.plan_frag, container, false);
        initHeader(view);

        rl_everyday_plan = (RelativeLayout) view
                .findViewById(R.id.rl_everyday_plan);
        rl_longterm_plan = (RelativeLayout) view
                .findViewById(R.id.rl_longterm_plan);

        v_everyday_plan = (View) view.findViewById(R.id.v_everyday_plan);
        v_longterm_plan = (View) view.findViewById(R.id.v_longterm_plan);


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
        switchItemSelected(0);
    }


    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int id = view.getId();
        switch (id) {
            case R.id.rl_everyday_plan:
                switchItemSelected(0);
                break;
            case R.id.rl_longterm_plan:
                switchItemSelected(1);
                break;
            default:
                break;
        }
    }

    private String planType = "1";
    private EverydayPlanFrag content1;
    private LongtermPlanFrag content2;

    private void switchItemSelected(int position) {
        switch (position) {
            case 0:
                content1 = new EverydayPlanFrag();
                planType = "1";
                rl_everyday_plan.setSelected(true);
                rl_longterm_plan.setSelected(false);
                v_everyday_plan.setVisibility(View.VISIBLE);
                v_longterm_plan.setVisibility(View.INVISIBLE);
                ((FragmentActivity) myselfContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.ll_content2, content1).commit();
                break;
            case 1:
                content2 = new LongtermPlanFrag();
                planType = "0";
                rl_everyday_plan.setSelected(false);
                rl_longterm_plan.setSelected(true);
                v_everyday_plan.setVisibility(View.INVISIBLE);
                v_longterm_plan.setVisibility(View.VISIBLE);
                 ((FragmentActivity) myselfContext).getSupportFragmentManager() .beginTransaction()
                        .replace(R.id.ll_content2, content2).commit();

                break;

            default:
                break;
        }

    }


}
