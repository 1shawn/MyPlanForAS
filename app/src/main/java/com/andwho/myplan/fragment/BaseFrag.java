package com.andwho.myplan.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFrag extends Fragment {

    private Activity myselfContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myselfContext = this.getActivity();
    }

    private ProgressDialog progressDialog;

    public void showProgressDialog() {
        if (!myselfContext.isFinishing()) {
            progressDialog = ProgressDialog.show(myselfContext, "", "请求发送中，请稍候");
            progressDialog.setCancelable(true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {

                }
            });
        }
    }

    public void dismissProgressDialog() {
        if (!myselfContext.isFinishing() && progressDialog != null
                && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
