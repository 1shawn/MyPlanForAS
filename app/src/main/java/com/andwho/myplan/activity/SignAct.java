package com.andwho.myplan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.andwho.myplan.R;
import com.andwho.myplan.utils.AndroidUtil;
import com.andwho.myplan.utils.BmobAgent;
import com.andwho.myplan.utils.ToastUtil;
import com.andwho.myplan.view.CustomEditView;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by zhouf on 2016/1/21.
 */
public class SignAct  extends BaseAct implements View.OnClickListener {

    private CustomEditView mAccountView;

    private CustomEditView mPswView;

    private Button mLoginBtn;
    private Activity myselfContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        myselfContext = this;
        findViews();
    }
    private void findViews(){
        findViewById(R.id.login_user_icon_iv).setVisibility(View.GONE);
        mAccountView = (CustomEditView) findViewById(R.id.login_account_et);
        mAccountView.setSingleLine(true);
        mAccountView.setEditViewHint("请输入邮箱");
        mAccountView.setNumberInput();
        mAccountView.setFocus();
        mAccountView.setDeleteBtnVisibility(true);
        mAccountView.setCustomEditViewLister(new CustomEditViewListener());
        mPswView = (CustomEditView) findViewById(R.id.login_password_et);
        mPswView.setSingleLine(true);
        mPswView.setPassWord();
        mPswView.setEditViewHint("请输入密码");
        mPswView.setDeleteBtnVisibility(true);
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        mLoginBtn.setText("快速注册");
        mLoginBtn.setOnClickListener(this);
        findViewById(R.id.tv_forget).setVisibility(View.GONE);
        findViewById(R.id.tv_sign).setVisibility(View.GONE);
        findViewById(R.id.tv_tips).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_login:
                signIn();
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

    public void signIn(){
        if(TextUtils.isEmpty(mAccountView.getEditViewContent())){
            ToastUtil.showShortToast(this, "邮箱不能为空！");
            return;
        }
        if(TextUtils.isEmpty(mPswView.getEditViewContent())){
            ToastUtil.showShortToast(this, "密码不能为空！");
            return;
        }
        if(!emailValidation(mAccountView.getEditViewContent())){
            ToastUtil.showShortToast(this, getResources().getString(R.string.str_Register_Tips3));
            return;
        }
        BmobAgent.checkUser(this, mAccountView.getEditViewContent(), mPswView.getEditViewContent(), new FindListener<BmobUser>() {
            @Override
            public void onSuccess(List<BmobUser> list) {
               BmobAgent.signIn(SignAct.this, mAccountView.getEditViewContent(), mPswView.getEditViewContent(), new SaveListener() {
                   @Override
                   public void onSuccess() {
                       ToastUtil.showLongToast(SignAct.this, getResources().getString(R.string.str_Register_Tips6));
                   }

                   @Override
                   public void onFailure(int i, String s) {
                       ToastUtil.showLongToast(SignAct.this, s);
                   }
               });
            }

            @Override
            public void onError(int i, String s) {
                ToastUtil.showLongToast(SignAct.this, getResources().getString(R.string.str_Register_Tips5));
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
