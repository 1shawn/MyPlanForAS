package com.andwho.myplan.fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.andwho.myplan.R;
import com.andwho.myplan.activity.IntentHelper;
import com.andwho.myplan.contentprovider.DbManger;
import com.andwho.myplan.model.DatePlans;
import com.andwho.myplan.model.Plan;
import com.andwho.myplan.utils.Log;
import com.andwho.myplan.utils.MyPlanUtil;
import com.andwho.myplan.view.myexpandablelistview.PinnedExpandableListView.PinnedExpandableListViewAdapter;
import com.andwho.myplan.view.myexpandablelistview.PullToRefreshBase.Mode;
import com.andwho.myplan.view.myexpandablelistview.PullToRefreshExpandableListView;

/**
 * @author ouyyx 每日计划
 */
public class EverydayPlanFrag extends BaseFrag implements OnClickListener {

	private static final String TAG = EverydayPlanFrag.class.getSimpleName();

	private Activity myselfContext;

	private TextView tv_nocontent;

	private PullToRefreshExpandableListView expandable_list;

	private ExpandableListAdapter listAdapter;

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

		View view = inflater.inflate(R.layout.frag_everyday_plan, container,
				false);

		tv_nocontent = (TextView) view.findViewById(R.id.tv_nocontent);
		expandable_list = (PullToRefreshExpandableListView) view
				.findViewById(R.id.expandable_list);
		return view;
	}

	private void setListener() {

	}

	private void init() {
		//
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initList();
	}

	private void initList() {
		ArrayList<DatePlans> listData = DbManger.getInstance(myselfContext)
				.getEverydayPlanData();
		if (listData != null && listData.size() > 0) {
			expandable_list.setVisibility(View.VISIBLE);
			tv_nocontent.setVisibility(View.GONE);

			listAdapter = new ExpandableListAdapter(listData);
			View view = LayoutInflater.from(myselfContext).inflate(
					R.layout.plans_group_item, null, false);
			expandable_list.setPinnedHeaderView(view);
			expandable_list
					.setOnPinnedHeaderClickLisenter(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Integer groupPosition = (Integer) v.getTag();
							boolean isContains = expandPositions
									.contains(groupPosition);
							if (isContains) {
								expandable_list.collapseGroup(groupPosition);
							} else {
								expandable_list.expandGroup(groupPosition);
							}

						}
					});
			expandable_list.setOnChildClickListener(new OnChildClickListener() {

				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					// TODO Auto-generated method stub
					final Plan plan = listAdapter.getChild(groupPosition,
							childPosition);
					IntentHelper.showPlanEdit(myselfContext, "1", plan);
					return true;
				}
			});
			expandable_list
					.setOnItemLongClickListener(mOnItemLongClickListener);

			expandable_list.setMode(Mode.DISABLED);
			expandable_list.setAdapter(listAdapter);

			for (int i = 0; i < listData.size(); i++) {
				expandable_list.expandGroup(i);
			}

		} else {
			tv_nocontent.setVisibility(View.VISIBLE);
			expandable_list.setVisibility(View.GONE);
		}

	}

	private OnItemLongClickListener mOnItemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			// TODO Auto-generated method stub
			Object obj = arg0.getAdapter().getItem(position);
			if (obj instanceof Plan) {
				Plan plan = (Plan) obj;
				initPopuptWindow(arg1, plan);
			}
			return true;
		}

	};

	private Set<Integer> expandPositions = new HashSet<Integer>();

	@SuppressLint("InflateParams")
	private class ExpandableListAdapter extends BaseExpandableListAdapter
			implements PinnedExpandableListViewAdapter {

		private ArrayList<DatePlans> listData = new ArrayList<DatePlans>();

		public ExpandableListAdapter(ArrayList<DatePlans> list) {
			listData = list;
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			if (listData != null) {
				return listData.size();
			} else {
				return 0;
			}
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			// 否则可能报空指针
			if (listData != null && listData.size() > 0) {
				return listData.get(groupPosition).plans.size();
			} else {
				return 0;
			}
		}

		@Override
		public DatePlans getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			// 取倒序值
			if (listData != null) {
				return listData.get(groupPosition);
			} else {
				return null;
			}
		}

		@Override
		public Plan getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			if (listData != null) {
				return listData.get(groupPosition).plans.get(childPosition);
			} else {
				return null;
			}
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			GroupViewHolder viewHolder = null;
			if (view == null) {
				viewHolder = new GroupViewHolder();
				view = LayoutInflater.from(myselfContext).inflate(
						R.layout.plans_group_item, null);
				viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				viewHolder.iv_group_indicator = (ImageView) view
						.findViewById(R.id.iv_group_indicator);
				view.setTag(viewHolder);
			} else {
				viewHolder = (GroupViewHolder) view.getTag();
			}

			DatePlans plans = getGroup(groupPosition);

			if (plans == null) {
				return view;
			}

			viewHolder.tv_name.setText(plans.date);
			if (!isExpanded) {
				viewHolder.iv_group_indicator
						.setImageResource(R.drawable.icon_arrow_collapsed);
			} else {
				viewHolder.iv_group_indicator
						.setImageResource(R.drawable.icon_arrow_expanded);
			}

			if (isExpanded) {
				expandPositions.add(groupPosition);
			} else {
				expandPositions.remove(groupPosition);
			}

			return view;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			ChildViewHolder viewHolder = null;

			if (view == null) {
				viewHolder = new ChildViewHolder();
				view = LayoutInflater.from(myselfContext).inflate(
						R.layout.plans_child_item, null);
				viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				viewHolder.iv_iscompleted = (ImageView) view
						.findViewById(R.id.iv_iscompleted);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ChildViewHolder) view.getTag();
			}

			final Plan plan = getChild(groupPosition, childPosition);

			viewHolder.tv_name.setText(plan.content);
			// if ("1".equals(plan.iscompleted)) {
			// viewHolder.iv_iscompleted.setVisibility(View.VISIBLE);
			// } else {
			// viewHolder.iv_iscompleted.setVisibility(View.GONE);
			// }

			if ("1".equals(plan.iscompleted)) {
				viewHolder.tv_name.getPaint().setFlags(
						Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
				viewHolder.tv_name.setTextColor(Color.parseColor("#909090"));

				// holder.iv_iscompleted.setVisibility(View.VISIBLE);
			} else {
				viewHolder.tv_name.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
				viewHolder.tv_name.setTextColor(Color.parseColor("#333333"));
				// holder.iv_iscompleted.setVisibility(View.GONE);
			}

			;

			return view;

		}

		final class ChildViewHolder {
			TextView tv_name;
			ImageView iv_iscompleted;
		}

		final class GroupViewHolder {
			TextView tv_name;
			ImageView iv_group_indicator;
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
			DatePlans category = getGroup(groupPosition);
			tv_name.setText(category.date);
			tv_name.setTextColor(Color.argb(alpha, 76, 76, 76)); // #767676

			ImageView iv_group_indicator = (ImageView) header
					.findViewById(R.id.iv_group_indicator);
			// iv_group_indicator.setImageAlpha(alpha);
			iv_group_indicator.setImageResource(R.drawable.icon_arrow_expanded);

			header.setTag(groupPosition);

		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	protected void initPopuptWindow(View view, final Plan plan) {
		// TODO Auto-generated method stub
		Log.d(TAG, "@@...myplan,...initPopuptWindow ---> ");
		Log.d(TAG, "@@...myplan,...content = " + plan.content);
		Log.d(TAG, "@@...myplan,...createtime = " + plan.createtime);

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
				// showDeleteWarningTips();
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
}