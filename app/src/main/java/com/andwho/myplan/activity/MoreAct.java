package com.andwho.myplan.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andwho.myplan.R;
import com.andwho.myplan.preference.MyPlanPreference;
import com.andwho.myplan.utils.StringUtil;
import com.andwho.myplan.view.FVItemSmall;
import com.andwho.myplan.view.ScaleScrollView;

/**
 * @author ouyyx 更多
 */
public class MoreAct extends SlideAct implements OnClickListener {

	private static final String TAG = MoreAct.class.getSimpleName();

	private Activity myselfContext;

	private LinearLayout ll_leftIcon;
	private TextView tv_leftIcon;
	private TextView tv_title;
	private ImageView iv_rightIcon;


	private View mNotLoginView, mLoginedView;

	private TextView mNameTextView, mPhoneTextView;

	private ImageView mUserAvatarIV;

	private Bitmap mCurrentAvatar = null;

	/**
	 * 常见问题
	 */
	private FVItemSmall mProblemItem;

	/**
	 * 推荐分享
	 */
	private FVItemSmall mShareItem;
	/**
	 * 关于我们
	 */
	private FVItemSmall mAboutItem;
	/**
	 * 建议反馈
	 */
	private FVItemSmall mSuggestItem;
	/**
	 * 消息中心
	 */
	private FVItemSmall mNoticeItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_act);

		myselfContext = this;

		initHeader();
		findViews();
		setListener();
		init();
	}

	private void initHeader() {
		ll_leftIcon = (LinearLayout) this.findViewById(R.id.ll_leftIcon);
		tv_leftIcon = (TextView) this.findViewById(R.id.tv_leftIcon);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		iv_rightIcon = (ImageView) this.findViewById(R.id.iv_rightIcon);

		ll_leftIcon.setOnClickListener(this);

		tv_title.setText("更多");

		tv_leftIcon.setVisibility(View.INVISIBLE);
		ll_leftIcon.setVisibility(View.VISIBLE);
	}

	private void findViews() {
		initScaleView();
		mProblemItem = (FVItemSmall) this.findViewById(R.id.ll_problems_item);
		mProblemItem.setLeftIcon(R.drawable.icon_menu_help);
		mProblemItem.setLeftTv(getResources().getString(R.string.more_problem));
		mProblemItem.setOnClickListener(mOnClickListener);
//		divider = this.findViewById(R.id.main_divider_line);
//		divider55 = this.findViewById(R.id.main_divider_margin);
		mShareItem = (FVItemSmall) this.findViewById(R.id.ll_share_item);
		mShareItem.setLeftIcon(R.drawable.icon_share_circle);
		mShareItem.setLeftTv(getResources().getString(R.string.more_share));
		mShareItem.setOnClickListener(mOnClickListener);

		mAboutItem = (FVItemSmall) this.findViewById(R.id.ll_about_item);
		mAboutItem.setLeftIcon(R.drawable.icon_menu_about);
		mAboutItem.setLeftTv(getResources().getString(R.string.more_aboutus));
		mAboutItem.setOnClickListener(mOnClickListener);

		mSuggestItem = (FVItemSmall) this.findViewById(R.id.ll_suggest_item);
		mSuggestItem.setLeftIcon(R.drawable.icon_menu_feedback);
		mSuggestItem.setLeftTv(getResources().getString(R.string.more_suggest));
		mSuggestItem.setOnClickListener(mOnClickListener);

		mNoticeItem = (FVItemSmall) this.findViewById(R.id.ll_notice_item);
		mNoticeItem.setLeftIcon(R.drawable.icon_menu_messages);
		mNoticeItem.setLeftTv(getResources().getString(R.string.more_notice));
		mNoticeItem.setOnClickListener(mOnClickListener);


		mNotLoginView = this.findViewById(R.id.main_no_login_tv);
		mLoginedView = this.findViewById(R.id.main_logined_lyt);
		mUserAvatarIV = (ImageView) this.findViewById(R.id.main_user_icon_iv);
		mNameTextView = (TextView) this.findViewById(R.id.main_user_name_tv);
		mPhoneTextView = (TextView) this.findViewById(R.id.main_user_phone_tv);
		this.findViewById(R.id.main_background).setOnClickListener(mOnClickListener);
	}

	private void initScaleView() {
		ScaleScrollView scrollView = (ScaleScrollView) this.findViewById(R.id.main_page_area);
		View headView = LayoutInflater.from(this).inflate(R.layout.include_mainpage_head, null, false);
		View scaleView = LayoutInflater.from(this).inflate(R.layout.include_mainpage_head_scale, null, false);
		View contentView = LayoutInflater.from(this).inflate(R.layout.include_mainpage_content, null, false);
		scrollView.setHeaderView(headView);
		scrollView.setScaleView(scaleView);
		scrollView.setScrollContentView(contentView);
	}
	private void setListener() {

	}

	private void init() {
		/*BmobUser bmobUser = BmobUser.getCurrentUser(this);
		if(bmobUser != null){
			// 允许用户使用应用

		}else{
			//缓存用户对象为空时， 可打开用户注册界面…
//			IntentHelper.showLogin(this);
			mLoginedView.setVisibility(View.GONE);
			mUserAvatarIV.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_headicon));
			mNotLoginView.setVisibility(View.VISIBLE);
		}*/
		String userName= MyPlanPreference.getInstance(myselfContext).getUsername();
//		BmobUser bmobUser = BmobUser.getCurrentUser(this);
		if(!TextUtils.isEmpty(userName)){
			// 允许用户使用应用
			String nickname=MyPlanPreference.getInstance(myselfContext).getNickname();
			if(!TextUtils.isEmpty(nickname)){
				mNameTextView.setText(nickname);
			}else{
				mNameTextView.setText(StringUtil.starStrFormatChange(userName));
			}

		}else{
			//缓存用户对象为空时， 可打开用户注册界面…
			mLoginedView.setVisibility(View.GONE);
			mUserAvatarIV.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_headicon));
			mNotLoginView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int id = view.getId();
		switch (id) {
		case R.id.ll_leftIcon:
			finish();
			break;
	/*	case R.id.ll_personal_setting:
			IntentHelper.showPersonalSetting(myselfContext);
			break;
		case R.id.ll_problems:
			IntentHelper.showProblems(myselfContext);
			break;
		case R.id.ll_encourage:
			// switchGender();
			break;
		case R.id.ll_share:
			IntentHelper.share(myselfContext);
			// showDateDialog();
			break;
		case R.id.ll_about:
			IntentHelper.showAboutUs(myselfContext);
			break;
*/
		default:
			break;
		}
	}

	OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.main_background://个人设置
					IntentHelper.showPersonalSetting(myselfContext);
					break;
				case R.id.ll_problems_item://常见问题
					IntentHelper.showProblems(myselfContext);
					break;
				case R.id.ll_share_item: //分享
					IntentHelper.share(myselfContext);
					break;
				case R.id.ll_about_item:// 关于我们
					IntentHelper.showAboutUs(myselfContext);
					break;
				case R.id.ll_notice_item:// 消息中心

					break;
				case R.id.ll_suggest_item:// 建议

					break;
				default:
					break;
			}
		}
	};
}
