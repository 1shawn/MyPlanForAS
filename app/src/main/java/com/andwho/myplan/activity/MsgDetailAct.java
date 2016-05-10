package com.andwho.myplan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andwho.myplan.R;

/**
 * Created by zhouf on 2016/5/10.
 */
public class MsgDetailAct extends BaseAct implements View.OnClickListener {
    private Activity myselfContext;
    private LinearLayout ll_leftIcon;
    private TextView tv_leftIcon;
    private TextView tv_title;
    private ImageView iv_rightIcon;

    private TextView tv_msgTitle,tv_msgTime,tv_msgContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msgdetail_act);
        myselfContext = this;

        initView();
        initHeader();
        initData();

    }
    private void initView() {
        tv_msgTitle=(TextView)this.findViewById(R.id.msg_title);
        tv_msgTime=(TextView)this.findViewById(R.id.msg_time);
        tv_msgContent=(TextView)this.findViewById(R.id.msg_content);
    }
    private void initHeader() {
        ll_leftIcon = (LinearLayout) this.findViewById(R.id.ll_leftIcon);
        tv_leftIcon = (TextView) this.findViewById(R.id.tv_leftIcon);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        iv_rightIcon = (ImageView) this.findViewById(R.id.iv_rightIcon);
        ll_leftIcon.setOnClickListener(this);
        tv_title.setText("消息详情");

        tv_leftIcon.setVisibility(View.INVISIBLE);
        ll_leftIcon.setVisibility(View.VISIBLE);
    }

    private void initData(){
        tv_msgTitle.setText( myselfContext.getIntent().getStringExtra("title"));
        tv_msgTime.setText( myselfContext.getIntent().getStringExtra("msgTime"));
        tv_msgContent.setText( myselfContext.getIntent().getStringExtra("content"));
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_leftIcon:
                finish();
                break;
            default:
                break;
        }
    }
}
