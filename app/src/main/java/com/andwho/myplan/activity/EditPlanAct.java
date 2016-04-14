package com.andwho.myplan.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andwho.myplan.R;
import com.andwho.myplan.constants.CompleteStatus;
import com.andwho.myplan.constants.PlanType;
import com.andwho.myplan.contentprovider.DbManger;
import com.andwho.myplan.model.Plan;
import com.andwho.myplan.utils.DateUtil;
import com.andwho.myplan.utils.Log;

import java.util.ArrayList;

/**
 * 编辑计划 ouyyx
 */
public class EditPlanAct extends SlideAct implements OnClickListener {

    private static final String TAG = EditPlanAct.class.getSimpleName();

    private Activity myselfContext;

    private LinearLayout ll_leftIcon;
    private TextView tv_leftIcon;
    private TextView tv_title;
    private ImageView iv_rightIcon;

    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_plan_act);

        myselfContext = this;

        initHeader();
        findViews();
        setListener();
        init();
    }

    private String planType;

    private void initHeader() {
        ll_leftIcon = (LinearLayout) this.findViewById(R.id.ll_leftIcon);
        tv_leftIcon = (TextView) this.findViewById(R.id.tv_leftIcon);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        iv_rightIcon = (ImageView) this.findViewById(R.id.iv_rightIcon);

        ll_leftIcon.setOnClickListener(this);
        iv_rightIcon.setOnClickListener(this);

        // 1每日计划 0长远计划
        planType = this.getIntent().getStringExtra("planType");
        if (PlanType.EVERYDAY_PLAN.equals(planType)) {
            tv_leftIcon.setText("每日计划");
        } else if (PlanType.LONGTERM_PLAN.equals(planType)) {
            tv_leftIcon.setText("长远计划");
        }
        tv_title.setText("编辑计划");
        ll_leftIcon.setVisibility(View.VISIBLE);
        iv_rightIcon.setVisibility(View.VISIBLE);
        iv_rightIcon.setImageResource(R.drawable.icon_save);
    }

    private void findViews() {
        et = (EditText) this.findViewById(R.id.et);
    }

    private void setListener() {

    }

    private boolean isEditModel = false;
    private Plan plan;

    private void init() {
        plan = (Plan) myselfContext.getIntent().getSerializableExtra("plan");
        if (plan != null) {
            // et.clearFocus();
            // et.setFocusableInTouchMode(true);
            et.setText(plan.content);
            et.setSelection(plan.content.length());
            isEditModel = true;
        }
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int id = view.getId();
        switch (id) {
            case R.id.ll_leftIcon:
                hideSoftKeyboard();
                finish();
                break;
            case R.id.iv_rightIcon:
                save();
                break;
            default:
                break;
        }
    }

    private void save() {
        String content = et.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(myselfContext, "请输入你的计划内容", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (isEditModel) {
            plan.content = content;
            plan.updatetime = DateUtil.getCurDateYYYYMMDDHHMM();
            DbManger.getInstance(myselfContext).updatePlan(plan);
        } else {
            DbManger.getInstance(myselfContext).addPlan(getPlan());
        }

        Toast.makeText(myselfContext, "保存成功", Toast.LENGTH_SHORT).show();
        ArrayList<Plan> listPlan = DbManger.getInstance(myselfContext)
                .queryPlans(planType);
        if (listPlan != null) {
            for (Plan plan : listPlan) {
                Log.e(TAG, "@@...mp...plan----------------------->");
                Log.e(TAG, "@@...mp...plan id = " + plan.planid);
                Log.e(TAG, "@@...mp...plan content = " + plan.content);
                Log.e(TAG, "@@...mp...plan createtime = " + plan.createtime);
                Log.e(TAG, "@@...mp...plan completetime = " + plan.completetime);
                Log.e(TAG, "@@...mp...plan updatetime = " + plan.updatetime);
                Log.e(TAG, "@@...mp...plan iscompleted = " + plan.iscompleted);
                Log.e(TAG, "@@...mp...plan isnotify = " + plan.isnotify);
                Log.e(TAG, "@@...mp...plan notifytime = " + plan.notifytime);
                Log.e(TAG, "@@...mp...plan plantype = " + plan.plantype);
                Log.e(TAG, "@@...mp...plan isdeleted = " + plan.isdeleted);
            }
        }

        hideSoftKeyboard();
        finish();
    }

    private Plan getPlan() {
        Plan plan = new Plan();
        plan.content = et.getText().toString();
        plan.iscompleted = CompleteStatus.IS_NOT_COMPLETED;
        plan.createtime = DateUtil.getCurDateYYYYMMDDHHMM();
        plan.updatetime = plan.createtime;
        plan.plantype = planType;
        return plan;
    }

    @Override
    public void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        if (!isEditModel) {
            showInputMethod();
        }
    }

    private InputMethodManager imm;

    private void showInputMethod() {
        imm = (InputMethodManager) myselfContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

    public void hideSoftKeyboard() {
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }
}
