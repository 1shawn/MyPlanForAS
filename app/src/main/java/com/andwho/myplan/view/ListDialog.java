package com.andwho.myplan.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.realscloud.supercarstore.R;
import com.realscloud.supercarstore.activity.style.EvenStatuBarStyle;
import com.realscloud.supercarstore.utils.EvenDensityUtil;
import com.realscloud.supercarstore.utils.Log;

import java.util.ArrayList;

public class ListDialog extends Dialog {
	private final static int itemHeight = 56;
	private final static int maxHeight = 450;

	public static interface EvenListDialogListener {
		public void onItemClick(String desc, int position);
	}
	
	private ListView listView;
	private ArrayList<String> list;
	private LinearLayout parent;
	private Activity context;
	private EvenListDialogListener listener;
	private String title;
	private TextView titleView;

	public ListDialog(Activity context, String title,
					  ArrayList<String> list, EvenListDialogListener listener) {
		// 初始化
		super(context, R.style.Dialog);
		// 隐藏标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
		this.list = list;
		this.listener = listener;
		this.title = title;
		initView();
		initDialog();

	}
	public ListDialog(Activity context, String title,
					  EvenListDialogListener listener) {
		// 初始化
		super(context, R.style.Dialog);
		// 隐藏标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
		this.list = new ArrayList<String>();
		this.listener = listener;
		this.title = title;
		initView();
		initDialog();

	}
	public void setListener(EvenListDialogListener listener){
		this.listener = listener;
	}
	private void initDialog() {
		int height = (list.size() * itemHeight);
		if (height > maxHeight)
			height = EvenDensityUtil.dip2px(maxHeight);
		else
			height = LayoutParams.WRAP_CONTENT;
		LayoutParams parentParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				height);
		this.setContentView(parent, parentParams);

		// 配置窗口参数并设定
		Window window = this.getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// 设置背景变暗属性
		WindowManager.LayoutParams params = window.getAttributes();// 建立Layout参数表
		window.setGravity(Gravity.CENTER);// 位置
		params.dimAmount = 0.60f;// 背景变暗程度
		// params.width = DensityUtil.dip2px(200); // 宽度
		// params.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
		//params.alpha = 1f; // 透明度
		window.setAttributes(params);// 应用配置好的参数表
		this.setCanceledOnTouchOutside(true);

	}

	@SuppressLint("InflateParams")
	private void initView() {
		parent = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.even_list_dialog_layout, null);

		listView = (ListView) parent.findViewById(R.id.tipsListView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				ListDialog.this.dismiss();
				listener.onItemClick((String)adapter.getItem(position), position);

			}

		});
		titleView = (TextView) parent.findViewById(R.id.listDialogTitle);
		titleView.setText(title);

	}

	public void setStringList(ArrayList<String> list) {
		this.list = list;
		int height = (list.size() * itemHeight);
		if (height > maxHeight)
			height = EvenDensityUtil.dip2px(maxHeight);
		else
			height = LayoutParams.WRAP_CONTENT;
		LayoutParams params=parent.getLayoutParams();
		params.height=height;
		parent.setLayoutParams(params);
		adapter.notifyDataSetChanged();
	}

	private BaseAdapter adapter = new BaseAdapter() {

		public int getCount() {
			return list.size();
		}

		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		public View getView(final int position, View view, ViewGroup arg2) {
			ViewHolder viewHolder = null;

			if (view == null) {
				viewHolder = new ViewHolder();
				view = LayoutInflater.from(context).inflate(
						R.layout.even_list_dialog_list_item, null);
				AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						EvenDensityUtil.dip2px(itemHeight));
				view.setLayoutParams(layoutParams);
				viewHolder.itemDivider = (TextView) view
						.findViewById(R.id.itemDivider);
				viewHolder.itemBody = (TextView) view
						.findViewById(R.id.itemBody);

				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			if (position == 0)
				viewHolder.itemDivider.setVisibility(View.GONE);
			else
				viewHolder.itemDivider.setVisibility(View.VISIBLE);
			viewHolder.itemBody.setText(list.get(position));

			return view;

		}

		final class ViewHolder {
			TextView itemBody;
			TextView itemDivider;

		}

		@Override
		public String getItem(int position) {
			return list.get(position);
		}

	};
	public void setDialogTitle(String title){
		titleView.setText(title);
	}
	public void show() {
		Log.i("EvenTipsDialog.show", "1");
		try {
			if (!context.isFinishing()) {
				Log.i("EvenTipsDialog.show", "!context.isFinishing()");
				super.show();
				EvenStatuBarStyle.setStatusBarIconColor(context, true);
			}
		} catch (Exception e) {

			Log.i("屌，dialog又崩溃", e.getMessage());
		}
	}

	@Override
	public void dismiss() {
		EvenStatuBarStyle.setStatusBarIconColor(context, false);
		super.dismiss();
	}
}