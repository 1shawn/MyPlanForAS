package com.andwho.myplan.activity;


import android.app.Activity;
import android.os.Bundle;

import com.andwho.myplan.R;
import com.andwho.myplan.fragment.IndexFrag;
import com.andwho.myplan.upgrade.UpgradeUtils;

/**
 * 欢迎页 ouyyx
 */
public class IndexAct extends BaseAct {

    private static final String TAG = IndexAct.class.getSimpleName();

    private Activity myselfContext;


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
    }

    private void setListener() {

    }

    private void init() {

        // 检查更新
        UpgradeUtils.checkNewVersion(myselfContext);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ll_content, new IndexFrag()).commit();
    }


}
