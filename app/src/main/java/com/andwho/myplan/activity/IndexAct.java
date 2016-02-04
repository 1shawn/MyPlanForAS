package com.andwho.myplan.activity;


import android.app.Activity;
import android.os.Bundle;

import com.andwho.myplan.R;
import com.andwho.myplan.fragment.IndexFrag;
import com.andwho.myplan.upgrade.UpgradeUtils;
import com.andwho.myplan.utils.Log;

/**
 * 欢迎页 ouyyx
 */
public class IndexAct extends BaseAct {

    private static final String TAG = IndexAct.class.getSimpleName();

    private Activity myselfContext;

//    private RelativeLayout rl_mine, rl_plan, rl_post_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_act);

        myselfContext = this;

        findViews();
        setListener();
        init();
    }

    private void findViews() {
//        rl_mine = (RelativeLayout) this.findViewById(R.id.rl_mine);
//        rl_plan = (RelativeLayout) this.findViewById(R.id.rl_plan);
//        rl_post_list = (RelativeLayout) this.findViewById(R.id.rl_post_list);
    }

    private void setListener() {
//        rl_mine.setOnClickListener(this);
//        rl_plan.setOnClickListener(this);
//        rl_post_list.setOnClickListener(this);
    }

    private void init() {

        // 检查更新
        UpgradeUtils.checkNewVersion(myselfContext);

        Log.d(TAG, "@@...测试提交新内容3334444");
//        switchItemSelected(0);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ll_content, new IndexFrag()).commit();
    }

//
//    @Override
//    public void onClick(View view) {
//        // TODO Auto-generated method stub
//        int id = view.getId();
//        switch (id) {
//            case R.id.rl_mine:
//                switchItemSelected(0);
//                break;
//            case R.id.rl_plan:
//                switchItemSelected(1);
//                break;
//            case R.id.rl_post_list:
//                switchItemSelected(2);
//                break;
//            default:
//                break;
//        }
//    }
//
//    private MineFrag content1;
//    private PlanFrag content2;
//    private PostListFrag content3;
//
//    private void switchItemSelected(int position) {
//        switch (position) {
//            case 0:
//                content1 = new MineFrag();
//                rl_mine.setSelected(true);
//                rl_plan.setSelected(false);
//                rl_post_list.setSelected(false);
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.ll_content, content1).commit();
//                break;
//            case 1:
//                content2 = new PlanFrag();
//                rl_mine.setSelected(false);
//                rl_plan.setSelected(true);
//                rl_post_list.setSelected(false);
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.ll_content, content2).commit();
//
//                break;
//            case 2:
//                content3 = new PostListFrag();
//                rl_mine.setSelected(false);
//                rl_plan.setSelected(false);
//                rl_post_list.setSelected(true);
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.ll_content, content3).commit();
//
//                break;
//
//
//            default:
//                break;
//        }
//
//    }

}
