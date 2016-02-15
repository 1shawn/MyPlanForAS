package com.andwho.myplan.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.andwho.myplan.R;
import com.andwho.myplan.activity.IntentHelper;
import com.andwho.myplan.contentprovider.DbManger;
import com.andwho.myplan.model.Banner;
import com.andwho.myplan.model.Plan;
import com.andwho.myplan.utils.Log;
import com.andwho.myplan.utils.MyPlanUtil;
import com.andwho.myplan.view.AdView;
import com.andwho.myplan.view.myexpandablelistview.PullToRefreshBase;
import com.andwho.myplan.view.myexpandablelistview.PullToRefreshBase.Mode;
import com.andwho.myplan.view.myexpandablelistview.PullToRefreshBase.OnRefreshListener;
import com.andwho.myplan.view.myexpandablelistview.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * @author ouyyx 小区(顶部广告，分享列表)
 */
public class CommunityFrag extends BaseFrag implements OnClickListener {

    private static final String TAG = CommunityFrag.class.getSimpleName();

    private Activity myselfContext;
    private TextView tv_title;
    private ImageView iv_rightIcon;

    private TextView tv_nocontent;

    private AdView ad;
    private PullToRefreshListView listview;
    private ListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myselfContext = this.getActivity();
        View view = findViews(inflater, container);
        setListener();
        init();

        return view;
    }

    private View findViews(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.community_frag, container,
                false);
        initHeader(view);

        ad = (AdView) view.findViewById(R.id.ad);
        tv_nocontent = (TextView) view.findViewById(R.id.tv_nocontent);
        listview = (PullToRefreshListView) view.findViewById(R.id.listview);
        return view;
    }

    private void setListener() {

        listview.setMode(Mode.BOTH);
        listview.setOnRefreshListener(mOnRefreshListener);
    }

    private void initHeader(View view) {
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("小区");
        iv_rightIcon = (ImageView) view.findViewById(R.id.iv_rightIcon);
        iv_rightIcon.setVisibility(View.VISIBLE);
        iv_rightIcon.setImageResource(R.drawable.icon_add);
        iv_rightIcon.setOnClickListener(this);
    }

    private OnRefreshListener<ListView> mOnRefreshListener = new OnRefreshListener<ListView>() {

        @Override
        public void onRefresh(PullToRefreshBase<ListView> refreshView) {
            // TODO Auto-generated method sub
            listview.onRefreshComplete();
        }

    };

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
            final Plan plan = listAdapter.getItem(arg2 - 1);
            IntentHelper.showPlanEdit(myselfContext, "0", plan);
        }

    };
    private OnItemLongClickListener mOnItemLongClickListener = new OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
            // TODO Auto-generated method stub
            Plan plan = listAdapter.getItem(position - 1);
            initPopuptWindow(arg1, plan);
            return true;
        }

    };

    private void init() {
        initList();
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

    }

    private void initList() {
        addHeader();

        ArrayList<Plan> list = DbManger.getInstance(myselfContext).queryPlans(
                "0");
        if (list != null && list.size() > 0) {
            tv_nocontent.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);

            listAdapter = new ListAdapter(myselfContext, list);
            listview.setAdapter(listAdapter);
            listview.setOnItemClickListener(mOnItemClickListener);
            listview.setOnItemLongClickListener(mOnItemLongClickListener);

        } else {
            listview.setVisibility(View.GONE);
            tv_nocontent.setVisibility(View.VISIBLE);
        }
    }

    private void addHeader() {
//        LayoutInflater vi = (LayoutInflater) myselfContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        ad = (AdView) vi.inflate(R.layout.ad_header, null, false);
        ad.showDefaultImg();

//        BmobQuery<Banner> query = new BmobQuery<Banner>();
//        query.findObjects(myselfContext, new FindListener<Banner>() {
//            @Override
//            public void onSuccess(final List<Banner> listBanner) {
//                // TODO Auto-generated method stub
//
//                Log.e(TAG, "@@...smpp...CheckUpdate size = " + listBanner);
//
//                for (Banner banner : listBanner) {
//                    Log.e(TAG, "@@...smpp..-----------------> ");
//                    Log.e(TAG, "@@...smpp...banner title = "
//                            + banner.title);
//                    Log.e(TAG, "@@...smpp...banner imgURL = "
//                            + banner.imgURL);
//                    Log.e(TAG, "@@...smpp...banner  detailURL = "
//                            + banner.detailURL);
//
//                }
//                myselfContext.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ad.init(listBanner);
//                    }
//                });
//
//                //listview.addHeaderView(ad , null, false);
//            }
//
//            @Override
//            public void onError(int arg0, String arg1) {
//                // TODO Auto-generated method stub
//
//            }
//        });


    }

    private class ListAdapter extends BaseAdapter {

        public List<Plan> data;

        private Activity mActivity;
        private LayoutInflater inflater;
        private String activityType;

        public ListAdapter(Activity mActivity, List<Plan> data) {
            this.mActivity = mActivity;
            this.data = data;
            inflater = mActivity.getLayoutInflater();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Plan getItem(int arg0) {
            return data.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = (LinearLayout) inflater.inflate(
                        R.layout.plans_child_item, null);
                holder.tv_name = (TextView) convertView
                        .findViewById(R.id.tv_name);
                holder.iv_iscompleted = (ImageView) convertView
                        .findViewById(R.id.iv_iscompleted);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Plan plan = data.get(position);
            holder.tv_name.setText(plan.content);
            if ("1".equals(plan.iscompleted)) {
                holder.tv_name.getPaint().setFlags(
                        Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                holder.tv_name.setTextColor(Color.parseColor("#909090"));

                // holder.iv_iscompleted.setVisibility(View.VISIBLE);
            } else {
                holder.tv_name.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
                holder.tv_name.setTextColor(Color.parseColor("#333333"));
                // holder.iv_iscompleted.setVisibility(View.GONE);
            }
            return convertView;
        }

        class ViewHolder {
            TextView tv_name;
            ImageView iv_iscompleted;
        }

    }


    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int id = view.getId();
        switch (id) {
            case R.id.iv_rightIcon:
//                IntentHelper.showPlanEdit(myselfContext, curPlanType);
                break;
            default:
                break;
        }
    }


    protected void initPopuptWindow(View view, final Plan plan) {
        // TODO Auto-generated method stub

        View popupWindow_view = ((LayoutInflater) myselfContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.popupwindow_2items, null);
        int popWidth = MyPlanUtil.dip2px(myselfContext, 120);
        final PopupWindow popupWindow = new PopupWindow(popupWindow_view,
                popWidth, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(true);
        popupWindow_view.setFocusableInTouchMode(true);// 不然按KEYCODE_BACK不生效
        popupWindow_view.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK && popupWindow != null
                        && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });

        popupWindow_view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });

        Button btn1 = (Button) popupWindow_view.findViewById(R.id.btn1);
        Button btn2 = (Button) popupWindow_view.findViewById(R.id.btn2);
        if ("1".equals(plan.iscompleted)) {
            btn1.setVisibility(View.GONE);
        } else {
            btn1.setVisibility(View.VISIBLE);
        }
        btn1.setText("完成");
        btn2.setText("删除");
        btn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                plan.iscompleted = "1";
                DbManger.getInstance(myselfContext).updatePlan(plan);
                initList();
                popupWindow.dismiss();
            }
        });

        btn2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                confirmDialog(plan);
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(view, view.getWidth() / 5, -20);

    }

    private void confirmDialog(final Plan plan) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(myselfContext);
        dialog.setCancelable(true);
        dialog.setMessage("确定删除'" + plan.content + "'吗？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                DbManger.getInstance(myselfContext).deletePlan(plan.planid);
                initList();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        dialog.show();
    }

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // StatService.onResume(this);
        ad.startAdAutoSwitch();
    }

    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // StatService.onPause(this);
        ad.stopAdAutoSwitch();
    }
}