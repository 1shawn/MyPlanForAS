package com.andwho.myplan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.andwho.myplan.R;
import com.andwho.myplan.utils.AndroidUtil;
import com.andwho.myplan.utils.BmobAgent;
import com.andwho.myplan.utils.ToastUtil;
import com.andwho.myplan.view.CustomEditView;

import cn.bmob.v3.listener.ResetPasswordByEmailListener;


/**
 * Created by zhouf on 2016/2/4.
 * 找回密码
 */
public class FindPswAct extends BaseAct implements View.OnClickListener {

    private CustomEditView mAccountView;

    private CustomEditView mPswView;

    private Button mLoginBtn;
    private Activity myselfContext;
    private LinearLayout ll_leftIcon,ll_life;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_act);

        myselfContext = this;
        findViews();
    }

    private void findViews() {
        findViewById(R.id.login_user_icon_iv).setVisibility(View.GONE);
        mAccountView = (CustomEditView) findViewById(R.id.login_account_et);
        mAccountView.setSingleLine(true);
        mAccountView.setEditViewHint("请输入邮箱");
        mAccountView.setFocus();
        mAccountView.setDeleteBtnVisibility(true);
        mAccountView.setCustomEditViewLister(new CustomEditViewListener());
        mPswView = (CustomEditView) findViewById(R.id.login_password_et);
        mPswView.setVisibility(View.GONE);
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        mLoginBtn.setText("找回密码");
        mLoginBtn.setOnClickListener(this);
        findViewById(R.id.tv_forget).setVisibility(View.GONE);
        findViewById(R.id.tv_sign).setVisibility(View.GONE);
        findViewById(R.id.tv_tips).setVisibility(View.GONE);

        ll_leftIcon = (LinearLayout) this.findViewById(R.id.ll_leftIcon);

        ll_leftIcon.setVisibility(View.VISIBLE);
        ll_leftIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_login:
                findPsw();
                break;
            case R.id.ll_leftIcon:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 输入框监听
     */
    private class CustomEditViewListener implements CustomEditView.CustomEditViewListner {
        @Override
        public void onDownBtnClick() {
            showAndHideAccoutsPopup();
        }

        @Override
        public void onFuzzyMatch(Editable s, boolean autoCompleteFlag) {

        }

        @Override
        public void onOtherBtnClick() {
        }
    }

    /**
     * 显示或隐藏弹框
     */
    private void showAndHideAccoutsPopup() {
        AndroidUtil.hideKeyboard(this);
//        new ShowOrHidePopwindow(getAutoCancelController()).executeOnExecutor(AppApplication.app.getLocalDataExcutor());
    }

    public void findPsw() {
        if (TextUtils.isEmpty(mAccountView.getEditViewContent())) {
            ToastUtil.showShortToast(this, "邮箱不能为空！");
            return;
        }

        if (!emailValidation(mAccountView.getEditViewContent())) {
            ToastUtil.showShortToast(this, getResources().getString(R.string.str_Register_Tips3));
            return;
        }
        BmobAgent.forgetPsw(this, mAccountView.getEditViewContent(), new ResetPasswordByEmailListener() {
            @Override
            public void onSuccess() {
                ToastUtil.showShortToast(FindPswAct.this, getResources().getString(R.string.str_find_psw));
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtil.showShortToast(FindPswAct.this, getResources().getString(R.string.str_fail));
            }
        });
    }

    /**
     * 验证邮箱格式是否正确
     */
    public boolean emailValidation(String email) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return email.matches(regex);
    }
}
