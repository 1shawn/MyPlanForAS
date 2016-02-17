package com.andwho.myplan.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public class BaseAct extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }


    private ProgressDialog progressDialog;

    public void showProgressDialog() {
        if (!this.isFinishing()) {
            progressDialog = ProgressDialog.show(this, "", "请求发送中，请稍候");
            progressDialog.setCancelable(true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {

                }
            });
        }
    }

    public void dismissProgressDialog() {
        if (!this.isFinishing() && progressDialog != null
                && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
