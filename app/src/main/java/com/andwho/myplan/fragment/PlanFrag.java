package com.andwho.myplan.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.CursorTreeAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.andwho.myplan.R;
import com.andwho.myplan.activity.IntentHelper;
import com.andwho.myplan.constants.CompleteStatus;
import com.andwho.myplan.constants.PlanType;
import com.andwho.myplan.contentprovider.DbManger;
import com.andwho.myplan.contentprovider.MyPlanDBOpenHelper;
import com.andwho.myplan.model.Plan;
import com.andwho.myplan.utils.DateUtil;
import com.andwho.myplan.utils.MyPlanUtil;
import com.andwho.myplan.view.myexpandablelistview.PinnedExpandableListView;
import com.andwho.myplan.view.myexpandablelistview.PullToRefreshBase;
import com.andwho.myplan.view.myexpandablelistview.PullToRefreshExpandableListView;
import com.andwho.myplan.view.myexpandablelistview.PullToRefreshListView;

import java.util.HashSet;
import java.util.Set;


/**
 * @author ouyyx 计划
 */
public class PlanFrag extends BaseFrag implements OnClickListener {

    private static final String TAG = PlanFrag.class.getSimpleName();

    private FragmentActivity myselfContext;
    private TextView tv_title;
    private ImageView iv_rightIcon;
    private LinearLayout ll_everyday_plan, ll_longterm_plan;
    private View v_everyday_plan, v_longterm_plan;

    private TextView tv_nocontent;
    // "每日计划"
    private PullToRefreshExpandableListView expandable_list;
    private EverydayTreeAdapter expandableListAdapter;
    // "长远计划"
    private PullToRefreshListView listview;
    private longtermListAdapter listAdapter;

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

        View view = inflater.inflate(R.layout.plan_frag, container, false);
        initHeader(view);

        ll_everyday_plan = (LinearLayout) view
                .findViewById(R.id.ll_everyday_plan);
        ll_longterm_plan = (LinearLayout) view
                .findViewById(R.id.ll_longterm_plan);
        v_everyday_plan = (View) view.findViewById(R.id.v_everyday_plan);
        v_longterm_plan = (View) view.findViewById(R.id.v_longterm_plan);
        tv_nocontent = (TextView) view.findViewById(R.id.tv_nocontent);
        expandable_list = (PullToRefreshExpandableListView) view
                .findViewById(R.id.expandable_list);
        listview = (PullToRefreshListView) view.findViewById(R.id.listview);

        return view;
    }

    private void initHeader(View view) {
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("计划");
        iv_rightIcon = (ImageView) view.findViewById(R.id.iv_rightIcon);
        iv_rightIcon.setVisibility(View.VISIBLE);
        iv_rightIcon.setImageResource(R.drawable.icon_add);
        iv_rightIcon.setOnClickListener(this);
    }

    private void setListener() {
        ll_everyday_plan.setOnClickListener(this);
        ll_longterm_plan.setOnClickListener(this);

        listview.setMode(PullToRefreshBase.Mode.DISABLED);
    }

    private void init() {
        switchItemSelected(0);
    }


    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int id = view.getId();
        switch (id) {
            case R.id.ll_everyday_plan:
                switchItemSelected(0);
                break;
            case R.id.ll_longterm_plan:
                switchItemSelected(1);
                break;
            case R.id.iv_rightIcon:
                IntentHelper.showPlanEdit(myselfContext, curPlanType);
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // 添加，修改“计划”后返回，刷新cursor
        // Log.e(TAG, "@@----onStart 开始onStart  ");

        if (curPlanType.equals(PlanType.LONGTERM_PLAN)) {
            if (longPlanCursor != null && listAdapter != null) {
                longPlanCursor.requery();
                listAdapter.notifyDataSetChanged();
            } else {
                initLongtermList();
            }
        } else {
            if (everydayCursor != null && expandableListAdapter != null) {
                //Log.e(TAG, "@@----onStart   everydayCursor ， expandableListAdapter 都不为空");
                everydayCursor.requery();
                expandableListAdapter.notifyDataSetChanged();
                // 更新cursor以后，展开之前已经展开的项目，否则列表全部收缩
                expandExist();
            } else {
                //Log.e(TAG, "@@----onStart initEverydayList ");
                initEverydayList();
            }
        }
    }


    private void expandExist() {
        for (Integer groupPosition : expandPositionsEveryday) {
            expandable_list.expandGroup(groupPosition);
        }
    }

    private String curPlanType = PlanType.EVERYDAY_PLAN;

    private void switchItemSelected(int position) {
        switch (position) {
            case 0:
                curPlanType = PlanType.EVERYDAY_PLAN;
                ll_everyday_plan.setSelected(true);
                ll_longterm_plan.setSelected(false);
                v_everyday_plan.setVisibility(View.VISIBLE);
                v_longterm_plan.setVisibility(View.INVISIBLE);

                initEverydayList();

                break;
            case 1:
                curPlanType = PlanType.LONGTERM_PLAN;
                ll_everyday_plan.setSelected(false);
                ll_longterm_plan.setSelected(true);
                v_everyday_plan.setVisibility(View.INVISIBLE);
                v_longterm_plan.setVisibility(View.VISIBLE);

                initLongtermList();
                break;

            default:
                break;
        }

    }

    private Cursor everydayCursor;


    private void initEverydayList() {
        listview.setVisibility(View.GONE);

        if (expandableListAdapter != null && expandableListAdapter.getGroupCount() > 0) {
            // Log.e(TAG, "@@---------------------initEverydayList  adapter group count > 0 ");
            // 如果列表已经加载了数据则只要显示
            // 这个判断在“每日计划”“长远计划”tab切换的时候使用
            tv_nocontent.setVisibility(View.GONE);
            expandable_list.setVisibility(View.VISIBLE);
            // 在切换到“长远计划”，点击“新增，编辑计划”后，返回，切换到“每日计划”，需要保持之前展开状态
            expandExist();
        } else {
            // Log.e(TAG, "@@---------------------重新查询 ");
            everydayCursor = DbManger.getInstance(myselfContext).getEverydayPlanDate();
//            Log.e(TAG, "@@---------------------cursor count = " + everydayCursor.getCount());
//            while (everydayCursor.moveToNext()) {
//                Plan plan = new Plan();
//
//                plan.content = everydayCursor.getString(everydayCursor
//                        .getColumnIndex(MyPlanDBOpenHelper.CONTENT));
//                plan.createtime = everydayCursor.getString(everydayCursor
//                        .getColumnIndex(MyPlanDBOpenHelper.CREATETIME));
//                Log.e(TAG, "@@--------------------->查到的日期 ： " + plan.createtime);
//                Log.e(TAG, "@@--------------------->查到的日期 content： " + plan.content);
//
//            }

            if (everydayCursor != null && everydayCursor.getCount() > 0) {
                tv_nocontent.setVisibility(View.GONE);
                expandable_list.setVisibility(View.VISIBLE);

                myselfContext.startManagingCursor(everydayCursor);
                expandableListAdapter = new EverydayTreeAdapter(everydayCursor, myselfContext);
                expandable_list.setAdapter(expandableListAdapter);
                expandable_list.setMode(PullToRefreshBase.Mode.DISABLED);
                // ExpandableListView的头部
                View view = LayoutInflater.from(myselfContext).inflate(
                        R.layout.plans_group_item, null, false);
                expandable_list.setPinnedHeaderView(view);
                expandable_list.expandGroup(0);
//                expandable_list
//                        .setOnPinnedHeaderClickLisenter(new OnClickListener() {
//
//                            @Override
//                            public void onClick(View v) {
//                                // TODO Auto-generated method stub
//                                Integer groupPosition = (Integer) v.getTag();
//
//                                Log.e(TAG, "@@---setOnPinnedHeaderClickLisenter groupPosition： " + groupPosition);
//
//                                boolean isContain = expandPositionsEveryday
//                                        .contains(groupPosition);
//                                Log.e(TAG, "@@---setOnPinnedHeaderClickLisenter isContains： " + isContain);
//                                if (isContain) {
//                                    expandable_list.collapseGroup(groupPosition);
//                                } else {
//                                    expandable_list.expandGroup(groupPosition);
//                                }
//
//                            }
//                        });
            } else {
                tv_nocontent.setVisibility(View.VISIBLE);
                expandable_list.setVisibility(View.GONE);
            }
        }
    }


    private Set<Integer> expandPositionsEveryday = new HashSet<Integer>();

    /***
     * "每日计划"的适配器
     **/

    public class EverydayTreeAdapter extends CursorTreeAdapter implements PinnedExpandableListView.PinnedExpandableListViewAdapter {


        public EverydayTreeAdapter(Cursor cursor, Context context) {
            super(cursor, context);
        }

        @Override
        protected void bindChildView(final View view, Context context, Cursor cursor, boolean isExpanded) {
            // Bind the related data with this child view

            LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll);

            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_name.setText(cursor.getString(cursor
                    .getColumnIndex(MyPlanDBOpenHelper.CONTENT)));
            if (CompleteStatus.IS_COMPLETED.equals(cursor.getString(cursor
                    .getColumnIndex(MyPlanDBOpenHelper.ISCOMPLETED)))) {
                ll.setBackgroundResource(R.color.complete_bg);
                tv_name.getPaint().setFlags(
                        Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                tv_name.setTextColor(Color.parseColor("#909090"));
            } else {
                ll.setBackgroundResource(R.drawable.common_btn_selector);
                tv_name.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
                tv_name.setTextColor(Color.parseColor("#333333"));
            }

            TextView tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_date.setVisibility(View.VISIBLE);
            tv_date.setText("更新时间：" + cursor.getString(cursor
                    .getColumnIndex(MyPlanDBOpenHelper.UPDATETIME)));


            // 点击事件
            final Plan plan = DbManger.getInstance(myselfContext).getPlanFromCursor(cursor);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    initPopuptWindow(view, PlanType.EVERYDAY_PLAN, plan);
                    return true;
                }
            });
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentHelper.showPlanEdit(myselfContext, PlanType.EVERYDAY_PLAN, plan);
                }
            });


        }

        @Override
        protected void bindGroupView(View view, Context context, Cursor cursor, final boolean isExpanded) {
            // Bind the related data with this group view

            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            ImageView iv_group_indicator = (ImageView) view
                    .findViewById(R.id.iv_group_indicator);

            String createTime = cursor.getString(cursor
                    .getColumnIndex(MyPlanDBOpenHelper.CREATETIME));

            if (DateUtil.isToday(createTime)) {
                tv_name.setText(createTime + " .今天");
            } else {
                tv_name.setText(createTime);
            }

            if (!isExpanded) {
                iv_group_indicator
                        .setImageResource(R.drawable.icon_arrow_collapsed);
            } else {
                iv_group_indicator
                        .setImageResource(R.drawable.icon_arrow_expanded);
            }

            final int groupPosition = cursor.getPosition();
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (isExpanded) {
                        expandable_list.collapseGroup(groupPosition);
                    } else {
                        expandable_list.expandGroup(groupPosition);
                    }
                }
            });

            if (isExpanded) {
                // Log.d(TAG, "@@---------------------bindGroupView expand groupPosition： " + groupPosition);
                expandPositionsEveryday.add(groupPosition);
            } else {
                expandPositionsEveryday.remove(groupPosition);
            }

        }

        @Override
        public Cursor getGroup(int groupPosition) {
            return super.getGroup(groupPosition);
        }

        @Override
        public Cursor getChild(int groupPosition, int childPosition) {
            return super.getChild(groupPosition, childPosition);
        }

        @Override
        protected Cursor getChildrenCursor(Cursor groupCursor) {
            String createDate = groupCursor.getString(groupCursor
                    .getColumnIndex(MyPlanDBOpenHelper.CREATETIME));

            //  Log.e(TAG, "@@---getChildrenCursor createDate=  " + createDate);

            Cursor cursor = DbManger.getInstance(myselfContext).getEverydayPlanByDate(createDate);
//            Log.e(TAG, "@@---getChildrenCursor cursor count=  " + cursor.getCount());
//            while (cursor.moveToNext()) {
//                Plan plan = new Plan();
//                plan.planid = String.valueOf(cursor.getInt(cursor
//                        .getColumnIndex(MyPlanDBOpenHelper.PLANID)));
//                plan.content = cursor.getString(cursor
//                        .getColumnIndex(MyPlanDBOpenHelper.CONTENT));
//                plan.createtime = cursor.getString(cursor
//                        .getColumnIndex(MyPlanDBOpenHelper.CREATETIME));
//
//
//                Log.e(TAG, "@@--------------------->查到的日期 content： " + plan.content);
//            }
            return cursor;
        }

        @Override
        protected View newChildView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
            //Log.d(TAG, "@@...myplan...newChildView");

            return LayoutInflater.from(myselfContext).inflate(
                    R.layout.plans_child_item, null);
        }

        @Override
        protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
            // Log.d(TAG, "@@...myplan...newGroupView");
            return LayoutInflater.from(myselfContext).inflate(
                    R.layout.plans_group_item, null);
        }

        @Override
        public int getPinnedHeaderState(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            final int childCount = getChildrenCount(groupPosition);
            if (childPosition == childCount - 1) {
                return PINNED_HEADER_PUSHED_UP;
            } else if (childPosition == -1
                    && !expandable_list.isGroupExpanded(groupPosition)) {
                return PINNED_HEADER_GONE;
            } else {
                return PINNED_HEADER_VISIBLE;
            }
        }

        @Override
        public void configurePinnedHeader(View header, final int groupPosition,
                                          int childPosition, int alpha) {
            // TODO Auto-generated method stub

            TextView tv_name = (TextView) header.findViewById(R.id.tv_name);


            Cursor groupCursor = getGroup(groupPosition);
            String createDate = groupCursor.getString(groupCursor
                    .getColumnIndex(MyPlanDBOpenHelper.CREATETIME));

            if (DateUtil.isToday(createDate)) {
                tv_name.setText(createDate + " .今天");
            } else {
                tv_name.setText(createDate);
            }
//            tv_name.setTextColor(Color.argb(alpha, 76, 76, 76)); // #767676

            ImageView iv_group_indicator = (ImageView) header
                    .findViewById(R.id.iv_group_indicator);
            // iv_group_indicator.setImageAlpha(alpha);
            iv_group_indicator.setImageResource(R.drawable.icon_arrow_expanded);

            header.setTag(groupPosition);

        }
    }

    private Cursor longPlanCursor;

    private void initLongtermList() {
        expandable_list.setVisibility(View.GONE);

        if (listAdapter != null && listAdapter.getCount() > 0) {
            // 如果列表已经加载了数据则只要显示
            // 这个判断在“每日计划”“长远计划”tab切换的时候使用
            tv_nocontent.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
        } else {
            // 初始化，第一次加载列表数据
            longPlanCursor = DbManger.getInstance(myselfContext).getPlanCursor(PlanType.LONGTERM_PLAN, null);
            if (longPlanCursor != null && longPlanCursor.getCount() > 0) {
                tv_nocontent.setVisibility(View.GONE);
                listview.setVisibility(View.VISIBLE);

                myselfContext.startManagingCursor(longPlanCursor);
                listAdapter = new longtermListAdapter(myselfContext, longPlanCursor);
                listview.setAdapter(listAdapter);
                listview.setOnItemClickListener(onItemClickListener);
                listview.setOnItemLongClickListener(onItemLongClickListenerLongterm);

            } else {
                listview.setVisibility(View.GONE);
                tv_nocontent.setVisibility(View.VISIBLE);
            }

        }

    }


    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View arg1, int position,
                                long arg3) {
            // TODO Auto-generated method stub
            final Cursor cursor = (Cursor) listAdapter.getItem(position - 1);
            final Plan plan = DbManger.getInstance(myselfContext).getPlanFromCursor(cursor);

            IntentHelper.showPlanEdit(myselfContext, PlanType.LONGTERM_PLAN, plan);
        }

    };

    private AdapterView.OnItemLongClickListener onItemLongClickListenerLongterm = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int position, long arg3) {
            // TODO Auto-generated method stub
            final Cursor cursor = (Cursor) listAdapter.getItem(position - 1);
            final Plan plan = DbManger.getInstance(myselfContext).getPlanFromCursor(cursor);

            initPopuptWindow(view, PlanType.LONGTERM_PLAN, plan);
            return true;
        }

    };

    private class longtermListAdapter extends CursorAdapter {


        private Activity mActivity;

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public longtermListAdapter(Activity mActivity, Cursor cursor) {

            super(mActivity, cursor, 0);
            this.mActivity = mActivity;
        }


        @Override
        public Object getItem(int position) {
            return super.getItem(position);
        }


        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll);
            TextView tv_name = (TextView) view
                    .findViewById(R.id.tv_name);


            tv_name.setText(cursor.getString(cursor
                    .getColumnIndex(MyPlanDBOpenHelper.CONTENT)));

            String iscompleted = cursor.getString(cursor
                    .getColumnIndex(MyPlanDBOpenHelper.ISCOMPLETED));
            if (CompleteStatus.IS_COMPLETED.equals(iscompleted)) {
                ll.setBackgroundResource(R.color.complete_bg);
                tv_name.getPaint().setFlags(
                        Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                tv_name.setTextColor(Color.parseColor("#909090"));
            } else {
                ll.setBackgroundResource(R.drawable.common_btn_selector);
                tv_name.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
                tv_name.setTextColor(Color.parseColor("#333333"));
            }

            TextView tv_date = (TextView) view
                    .findViewById(R.id.tv_date);
            tv_date.setVisibility(View.VISIBLE);
            tv_date.setText(cursor.getString(cursor
                    .getColumnIndex(MyPlanDBOpenHelper.CREATETIME)));


        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {

            LayoutInflater vi = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout view = (LinearLayout) vi.inflate(R.layout.longterm_plan_item, parent, false);

            return view;

        }

    }


    protected void initPopuptWindow(View view, final String planType, final Plan plan) {
        // TODO Auto-generated method stub

        View popupWindow_view = ((LayoutInflater) myselfContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.popupwindow_2items, null);
        int popWidth = MyPlanUtil.dip2px(myselfContext, 120);
        final PopupWindow popupWindow = new PopupWindow(popupWindow_view,
                popWidth, ViewPager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(true);
        popupWindow_view.setFocusableInTouchMode(true);// 不然按KEYCODE_BACK不生效
        popupWindow_view.setOnKeyListener(new View.OnKeyListener() {

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

        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
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
        btn1.setVisibility(View.VISIBLE);
        if (CompleteStatus.IS_COMPLETED.equals(plan.iscompleted)) {
            btn1.setText("未完成");
        } else {
            btn1.setText("完成");
        }
        btn2.setText("删除");
        btn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (CompleteStatus.IS_COMPLETED.equals(plan.iscompleted)) {
                    plan.iscompleted = CompleteStatus.IS_NOT_COMPLETED;
                } else {
                    plan.iscompleted = CompleteStatus.IS_COMPLETED;
                }

                DbManger.getInstance(myselfContext).updatePlan(plan);
                if (planType.equals(PlanType.LONGTERM_PLAN)) {
                    longPlanCursor.requery();
                    listAdapter.notifyDataSetChanged();
                } else {
                    everydayCursor.requery();
                    expandableListAdapter.notifyDataSetChanged();
                }

                popupWindow.dismiss();
            }
        });

        btn2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                confirmDialog(planType, plan);
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(view, view.getWidth() / 2, (-150));

    }

    private void confirmDialog(final String planType, final Plan plan) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(myselfContext);
        dialog.setCancelable(true);
        dialog.setMessage("确定删除'" + plan.content + "'吗？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        if (planType.equals(PlanType.LONGTERM_PLAN)) {
                            DbManger.getInstance(myselfContext).deletePlan(plan.planid);
                            longPlanCursor.requery();
                            // 删除后,如果没有记录，提示“暂无数据”
                            if (longPlanCursor != null && longPlanCursor.getCount() == 0) {
                                listview.setVisibility(View.GONE);
                                tv_nocontent.setVisibility(View.VISIBLE);
                                listAdapter = null;// 初始化
                            } else {
                                listAdapter.notifyDataSetChanged();
                            }
                        } else {
                            DbManger.getInstance(myselfContext).deletePlan(plan.planid);
                            everydayCursor.requery();
                            // 删除后,如果没有记录，提示“暂无数据”
                            if (everydayCursor != null && everydayCursor.getCount() == 0) {
                                expandable_list.setVisibility(View.GONE);
                                tv_nocontent.setVisibility(View.VISIBLE);
                                expandableListAdapter = null;// 初始化
                            } else {
                                expandableListAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }

        );
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener()

                {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                }

        );
        dialog.show();
    }

}
