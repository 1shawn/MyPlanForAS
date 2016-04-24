package com.andwho.myplan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.andwho.myplan.R;
import com.andwho.myplan.preference.MyPlanPreference;
import com.andwho.myplan.utils.AndroidUtil;
import com.andwho.myplan.utils.BmobAgent;
import com.andwho.myplan.utils.ToastUtil;
import com.andwho.myplan.view.CustomEditView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zhouf on 16/4/24.
 */
public class ForgetPswAct extends BaseAct implements View.OnClickListener {

    private CustomEditView mNewPswView;

    private CustomEditView mPswView;

    private CustomEditView mNewPswAginView;

    private Button mChangeBtn;
    private Activity myselfContext;
    private LinearLayout ll_leftIcon,ll_life;

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
        mPswView.setSingleLine(true);
        mPswView.setEditViewHint("请输入当前密码");
        mPswView.setFocus();
        mPswView.setDeleteBtnVisibility(true);
        mPswView.setCustomEditViewLister(new CustomEditViewListener());
        mNewPswView = (CustomEditView) findViewById(R.id.login_newpsw_et);
        mNewPswView.setSingleLine(true);
        mNewPswView.setDeleteBtnVisibility(true);
        mNewPswView.setEditViewHint("请输入新密码");

        mNewPswAginView = (CustomEditView) findViewById(R.id.login_newpsw_et);
        mNewPswAginView.setSingleLine(true);
        mNewPswAginView.setDeleteBtnVisibility(true);
        mNewPswView.setEditViewHint("请重新输入一次新密码");

        mNewPswView.setVisibility(View.GONE);
        mChangeBtn = (Button) findViewById(R.id.btn_change);
        mChangeBtn.setText("修改密码");
        mChangeBtn.setOnClickListener(this);
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
        if (TextUtils.isEmpty(mPswView.getEditViewContent())) {
            ToastUtil.showShortToast(this, "请输入原密码！");
            return;
        }
        if (TextUtils.isEmpty(mNewPswView.getEditViewContent())) {
            ToastUtil.showShortToast(this, "请输入新密码！");
            return;
        }
        if (TextUtils.isEmpty(mNewPswAginView.getEditViewContent())) {
            ToastUtil.showShortToast(this, "请再次输入新密码！");
            return;
        }
        if (mNewPswView.getEditViewContent().toString().equals(mNewPswAginView.getEditViewContent().toString())) {
            ToastUtil.showShortToast(this, "两次输入密码不一致！");
            return;
        }

        showProgressDialog(null, true, false);
        BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
        query.addWhereEqualTo("username", MyPlanPreference.getInstance(myselfContext).getUsername());
        query.addWhereEqualTo("password", mPswView.getEditViewContent().toString());
        query.findObjects(myselfContext, new FindListener(){
            @Override
            public void onSuccess(List list) {
                if(list!=null&&list.size()==1){//否则有可能是整个表内容
                    BmobUser bmobUser = BmobUser.getCurrentUser(myselfContext);
                    if(bmobUser != null){
                        // 允许用户使用应用
                        bmobUser.update(myselfContext, new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                ToastUtil.showLongToast(myselfContext,"修改密码成功！");
                                BmobAgent.loginOut(myselfContext);
                                IntentHelper.showLogin(myselfContext);
                                dismissProgressDialog();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                ToastUtil.showLongToast(myselfContext,"修改密码失败！");
                                dismissProgressDialog();

                            }
                        });

                    }else{
                        ToastUtil.showLongToast(myselfContext,"网络异常！");
                        dismissProgressDialog();
                    }

                }else {
                    ToastUtil.showLongToast(myselfContext, "原密码输入不正确！");
                    dismissProgressDialog();
                }
            }

            @Override
            public void onError(int i, String s) {
                ToastUtil.showLongToast(myselfContext,"原密码输入不正确！");
                dismissProgressDialog();
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
