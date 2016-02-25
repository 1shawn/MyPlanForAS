package com.andwho.myplan.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.andwho.myplan.helper.CustomDialogHelper;

public class BaseAct extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }


//    private ProgressDialog progressDialog;

    private Dialog mDialog;

    public void showProgressDialog(String msg,boolean cancelable,boolean keybackFinish) {
        if (!this.isFinishing()) {
           /* progressDialog = ProgressDialog.show(this, "", "请求发送中，请稍候");
            progressDialog.setCancelable(true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {

                }
            });*/
            mDialog = CustomDialogHelper.showUniLoadingDialog(
                    this,
                    msg,
                    cancelable,
                    keybackFinish);
            mDialog.show();
        }
    }

    public void dismissProgressDialog() {
        /*if (!this.isFinishing() && progressDialog != null
                && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }*/
        if (!this.isFinishing() && mDialog != null
                && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
