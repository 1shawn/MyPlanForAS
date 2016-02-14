package com.andwho.myplan.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.andwho.myplan.R;

/**
 * @author ouyyx 主页
 */
public class IndexFrag extends MyPlanViewPagerFrag implements View.OnClickListener {

    private static final String TAG = IndexFrag.class.getSimpleName();

    private Activity myselfContext;

    private RelativeLayout rl_mine, rl_plan, rl_post_list;


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

        View view = inflater.inflate(R.layout.index_frag, container, false);
        rl_mine = (RelativeLayout) view.findViewById(R.id.rl_mine);
        rl_plan = (RelativeLayout) view.findViewById(R.id.rl_plan);
        rl_post_list = (RelativeLayout) view.findViewById(R.id.rl_post_list);
        initViewPager(view);

        return view;
    }


    private void setListener() {
        rl_mine.setOnClickListener(this);
        rl_plan.setOnClickListener(this);
        rl_post_list.setOnClickListener(this);
    }

    private void init() {
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int id = view.getId();
        switch (id) {
            case R.id.rl_mine:
                setTabSelectedStatus(0);
                setPageSelected(0);
                break;
            case R.id.rl_plan:
                setTabSelectedStatus(1);
                setPageSelected(1);
                break;
            case R.id.rl_post_list:
                setTabSelectedStatus(2);
                setPageSelected(2);
                break;
            default:
                break;
        }
    }


    private void setTabSelectedStatus(int position) {
        switch (position) {
            case 0:
                rl_mine.setSelected(true);
                rl_plan.setSelected(false);
                rl_post_list.setSelected(false);
                break;
            case 1:
                rl_mine.setSelected(false);
                rl_plan.setSelected(true);
                rl_post_list.setSelected(false);
                break;
            case 2:
                rl_mine.setSelected(false);
                rl_plan.setSelected(false);
                rl_post_list.setSelected(true);
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
        Fragment[] frags = new Fragment[3];
        frags[0] = new MineFrag();
        frags[1] = new PlanFrag();
        frags[2] = new PostListFrag();
        return frags;
    }

    @Override
    public ViewGroup getContainer(View view) {
        // TODO Auto-generated method stub
        return (LinearLayout) view.findViewById(R.id.ll_content2);
    }
}
