package com.andwho.myplan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andwho.myplan.R;
import com.andwho.myplan.model.Messages;
import com.andwho.myplan.utils.ToastUtil;
import com.andwho.myplan.view.SwipeAdapter;
import com.andwho.myplan.view.SwipeListView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zhouf on 16/4/24.
 */
public class MsgCenterAct extends BaseAct implements View.OnClickListener {
    /**
     * 列表
     */
    private SwipeListView mListView;
    private Activity myselfContext;
    private LinearLayout ll_leftIcon;
    private TextView tv_leftIcon;
    private TextView tv_title;
    private ImageView iv_rightIcon;
    SwipeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msgcenter_act);
        myselfContext = this;

        initView();
        initHeader();
        initData();

    }

    /**
     * 初始化界面
     */
    private void initView() {
        mListView = (SwipeListView)findViewById(R.id.listview);
        adapter= new SwipeAdapter(MsgCenterAct.this,mListView.getRightViewWidth(),
                new SwipeAdapter.IOnItemRightClickListener() {
                    @Override
                    public void onRightClick(View v, int position) {
                        // TODO Auto-generated method stub
                        Toast.makeText(MsgCenterAct.this, "right onclick " + position,
                                Toast.LENGTH_SHORT).show();
                    }
                });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, "item onclick " + position, Toast.LENGTH_SHORT)
//                        .show();
                Messages msg=adapter.getItem(position);
                IntentHelper.showMsgDetail(MsgCenterAct.this,msg.title,msg.content,msg.getCreatedAt(),msg.getObjectId());
            }
        });
        mListView.setAdapter(adapter);
       /* mListView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.list_footer, null));
        mListView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.list_footer, null));
        mListView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.list_footer, null));
        mListView.addFooterView(LayoutInflater.from(this).inflate(R.layout.list_footer, null));
        mListView.addFooterView(LayoutInflater.from(this).inflate(R.layout.list_footer, null));
        mListView.addFooterView(LayoutInflater.from(this).inflate(R.layout.list_footer, null));
        mListView.addFooterView(LayoutInflater.from(this).inflate(R.layout.list_footer, null));
        mListView.addFooterView(LayoutInflater.from(this).inflate(R.layout.list_footer, null));
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "item onclick " + position, Toast.LENGTH_SHORT)
                        .show();
            }
        });*/

    }
    private void initHeader() {
        ll_leftIcon = (LinearLayout) this.findViewById(R.id.ll_leftIcon);
        tv_leftIcon = (TextView) this.findViewById(R.id.tv_leftIcon);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        iv_rightIcon = (ImageView) this.findViewById(R.id.iv_rightIcon);

        ll_leftIcon.setOnClickListener(this);

        tv_title.setText("消息中心");

        tv_leftIcon.setVisibility(View.INVISIBLE);
        ll_leftIcon.setVisibility(View.VISIBLE);
    }
    public void initData(){
        BmobQuery<Messages> query = new BmobQuery<Messages>();
        query.addWhereEqualTo("isDeleted", "0");
//        query.order("-isTop");
//        query.addWhereRelatedTo("comments", new BmobPointer(post));
        query.order("-createdAt");// 降序排列
//        query.setLimit(10);
        query.findObjects(myselfContext, new FindListener<Messages>() {
            @Override
            public void onSuccess(final List<Messages> list) {
                // TODO Auto-generated method stub

                if (list != null && list.size() > 0) {

                  /*  tv_nocontent.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);

                    listAdapter = new CommentListAdapter(myselfContext, list);
                    listview.setAdapter(listAdapter);
                    listview.setOnItemClickListener(onItemClickListener);*/

                   /* SwipeAdapter adapter = new SwipeAdapter(MsgCenterAct.this,mListView.getRightViewWidth(),list,
                            new SwipeAdapter.IOnItemRightClickListener() {
                                @Override
                                public void onRightClick(View v, int position) {
                                    // TODO Auto-generated method stub
                                    Toast.makeText(MsgCenterAct.this, "right onclick " + position,
                                            Toast.LENGTH_SHORT).show();
                                }
                            });*/
                    adapter.setData(list);

                } else {
                   /* listview.setVisibility(View.GONE);
                    tv_nocontent.setVisibility(View.VISIBLE);*/
                }

//                new Handler().postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        sv.scrollTo(0, 0);
//                    }
//                }, 500);

            }

            @Override
            public void onError(int arg0, String arg1) {
                // TODO Auto-generated method stub
                ToastUtil.showLongToast(myselfContext, arg1);
            }
        });

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