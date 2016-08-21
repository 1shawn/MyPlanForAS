package com.andwho.myplan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.andwho.myplan.R;
import com.andwho.myplan.view.CustomEditView;

/**
 * Created by zhouf on 16/4/24.
 * 消息中心
 */
public class MsgCenter extends BaseAct implements View.OnClickListener {

    private CustomEditView mNewPswView;

    private CustomEditView mPswView;

    private CustomEditView mNewPswAginView;

    private Button mChangeBtn;
    private Activity myselfContext;
    private LinearLayout ll_leftIcon, ll_life;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_psw);

        myselfContext = this;
        findViews();
    }
    private void findViews() {
        findViewById(R.id.login_user_icon_iv).setVisibility(View.GONE);
        mPswView = (CustomEditView) findViewById(R.id.login_psw_et);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_login:
                break;
            case R.id.ll_leftIcon:
                finish();
                break;
            default:
                break;
        }
    }

}
