package com.andwho.myplan.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andwho.myplan.R;
import com.andwho.myplan.model.MyPlanUser;
import com.andwho.myplan.preference.MyPlanPreference;
import com.andwho.myplan.utils.BmobAgent;
import com.andwho.myplan.utils.DateUtil;
import com.andwho.myplan.utils.StringUtil;
import com.andwho.myplan.view.MpDatePickerDialog;
import com.andwho.myplan.view.RoundedImageView;
import com.bmob.btp.callback.UploadListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author ouyyx 个人设置
 */
public class PersonalSettingAct extends BaseAct implements OnClickListener {

	private static final String TAG = PersonalSettingAct.class.getSimpleName();

	private Activity myselfContext;

	private LinearLayout ll_leftIcon;
	private TextView tv_leftIcon;
	private TextView tv_title;
	private ImageView iv_rightIcon;

	private LinearLayout ll_headicon, ll_nickname, ll_gender, ll_birthday,
			ll_life,ll_email,ll_changePsw;
	private RoundedImageView iv_headicon;
	private ImageView iv_male, iv_female;
	private TextView tv_nickname, tv_birthday, tv_lifespan;

	private Button btn_login;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_setting_act);

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

		tv_leftIcon.setText("个人设置");

		tv_title.setVisibility(View.INVISIBLE);
		ll_leftIcon.setVisibility(View.VISIBLE);
	}

	private void findViews() {
		ll_headicon = (LinearLayout) this.findViewById(R.id.ll_headicon);
		ll_nickname = (LinearLayout) this.findViewById(R.id.ll_nickname);
		ll_gender = (LinearLayout) this.findViewById(R.id.ll_gender);
		ll_birthday = (LinearLayout) this.findViewById(R.id.ll_birthday);
		ll_life = (LinearLayout) this.findViewById(R.id.ll_life);

		iv_headicon = (RoundedImageView) this.findViewById(R.id.iv_headicon);
		iv_male = (ImageView) this.findViewById(R.id.iv_male);
		iv_female = (ImageView) this.findViewById(R.id.iv_female);

		tv_nickname = (TextView) this.findViewById(R.id.tv_nickname);
		tv_birthday = (TextView) this.findViewById(R.id.tv_birthday);
		tv_lifespan = (TextView) this.findViewById(R.id.tv_lifespan);

		btn_login = (Button)this.findViewById(R.id.btn_login);

		ll_email = (LinearLayout) this.findViewById(R.id.ll_email);
		ll_changePsw = (LinearLayout) this.findViewById(R.id.ll_changePsw);

	}

	private void setListener() {
		ll_headicon.setOnClickListener(this);
		ll_nickname.setOnClickListener(this);
		ll_gender.setOnClickListener(this);
		ll_birthday.setOnClickListener(this);
		ll_life.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		this.findViewById(R.id.img_changePsw).setOnClickListener(this);
        this.findViewById(R.id.img_synchroData).setOnClickListener(this);
	}

	private void init() {
		String userName=MyPlanPreference.getInstance(myselfContext).getUsername();
//		BmobUser bmobUser = BmobUser.getCurrentUser(this);
		if(!TextUtils.isEmpty(userName)){
			// 允许用户使用应用
			String nickname=MyPlanPreference.getInstance(myselfContext).getNickname();
			if(!TextUtils.isEmpty(nickname)){
			     tv_nickname.setText(nickname);
			}else{
				tv_nickname.setText(StringUtil.starStrFormatChange(userName));
			}
			ll_changePsw.setVisibility(View.VISIBLE);
		}else{
			//缓存用户对象为空时， 可打开用户注册界面…
//			IntentHelper.showLogin(this);
		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initData();
	}

	public void initData() {
		new LoadImageAsyncTask().execute();
		/*String nickname = MyPlanPreference.getInstance(myselfContext)
				.getNickname();
		tv_nickname.setText(nickname);*/
		String userName=MyPlanPreference.getInstance(myselfContext).getUsername();
		if(!TextUtils.isEmpty(userName)){
			// 允许用户使用应用
			String nickname=MyPlanPreference.getInstance(myselfContext).getNickname();
			if(!TextUtils.isEmpty(nickname)){
				tv_nickname.setText(nickname);
			}else{
				tv_nickname.setText(StringUtil.starStrFormatChange(userName));
			}
			btn_login.setText("注销登录");
		}else{
			//缓存用户对象为空时， 可打开用户注册界面…
//			IntentHelper.showLogin(this);
		}

		initGender();
		initBirthday();
		String lifeSpan = MyPlanPreference.getInstance(myselfContext)
				.getLifeSpan();
		tv_lifespan.setText(lifeSpan);
	}

	private class LoadImageAsyncTask extends AsyncTask<Void, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String picUrl = MyPlanPreference.getInstance(myselfContext)
						.getHeadPicUrl();
				if (TextUtils.isEmpty(picUrl)) {
					return null;
				}

				Uri uri = Uri.parse(picUrl);
				ContentResolver contentProvider = myselfContext
						.getContentResolver();
				Bitmap bmp = BitmapFactory.decodeStream(contentProvider
						.openInputStream(uri));
				return Bitmap.createScaledBitmap(bmp, 200, 200, true);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				return null;
			}

		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result == null) {
				iv_headicon.setImageResource(R.drawable.default_headicon);
			} else {
				iv_headicon.setImageBitmap(result);
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

	};

	private void initGender() {
		String gender = MyPlanPreference.getInstance(myselfContext).getGender();
		if ("1".equals(gender)) {
			iv_male.setImageResource(R.drawable.icon_male_press);
			iv_female.setImageResource(R.drawable.icon_female_unpress);
		} else {
			iv_male.setImageResource(R.drawable.icon_male_unpress);
			iv_female.setImageResource(R.drawable.icon_female_press);
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int id = view.getId();
		switch (id) {
		case R.id.ll_leftIcon:
			finish();
			break;
		case R.id.ll_headicon:
			showIconSelectDialog();
			break;
		case R.id.ll_nickname:
			IntentHelper.showModifyInfo(myselfContext, "nickname");
			break;
		case R.id.ll_gender:
			switchGender();
			break;
		case R.id.ll_birthday:
			showDateDialog();
			break;
		case R.id.ll_life:
			IntentHelper.showModifyInfo(myselfContext, "lifespan");
			break;
		case R.id.btn_login:
			String userName=MyPlanPreference.getInstance(myselfContext).getUsername();
			if(!TextUtils.isEmpty(userName)) {
				BmobAgent.loginOut(myselfContext);
				btn_login.setText("登录");
			}else{
				IntentHelper.showLogin(myselfContext);
			}

		case R.id.img_changePsw://忘记密码
			IntentHelper.showFindPsw(myselfContext);
		case R.id.img_synchroData://同步数据
			IntentHelper.showLogin(myselfContext);
		default:
			break;
		}
	}

	private void switchGender() {
		String gender = MyPlanPreference.getInstance(myselfContext).getGender();
		if ("1".equals(gender)) {
			MyPlanPreference.getInstance(myselfContext).setGender("0");
			iv_male.setImageResource(R.drawable.icon_male_unpress);
			iv_female.setImageResource(R.drawable.icon_female_press);
		} else {
			MyPlanPreference.getInstance(myselfContext).setGender("1");
			iv_male.setImageResource(R.drawable.icon_male_press);
			iv_female.setImageResource(R.drawable.icon_female_unpress);
		}
	}

	private int startYear, startMonth, startDay;

	private void initBirthday() {
		String birthday = MyPlanPreference.getInstance(myselfContext)
				.getBirthday();
		if (!TextUtils.isEmpty(birthday)) {
			HashMap<String, Integer> startTimeMap = DateUtil
					.getYearMonthDayMap2(birthday);
			startYear = startTimeMap.get("year");
			startMonth = startTimeMap.get("month") + 1;
			startDay = startTimeMap.get("day");

			tv_birthday.setText(startYear + "年"
					+ DateUtil.formatNumber(startMonth) + "月"
					+ DateUtil.formatNumber(startDay) + "日");
		} else {
			startYear = Calendar.getInstance().get(Calendar.YEAR);
			startMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
			startDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

			tv_birthday.setText(startYear + "年"
					+ DateUtil.formatNumber(startMonth) + "月"
					+ DateUtil.formatNumber(startDay) + "日");
		}

	}

	private void showDateDialog() {
		Locale.setDefault(Locale.CHINA);// 设置当前环境为中文

		final MpDatePickerDialog spDateDialog = new MpDatePickerDialog(
				myselfContext, null, startYear, startMonth - 1, startDay);

		spDateDialog.setCancelable(true);
		spDateDialog.setCanceledOnTouchOutside(true);

		spDateDialog.setButton(-2, (CharSequence) "取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						spDateDialog.dismiss();
					}

				});
		//
		spDateDialog.setButton(-1, (CharSequence) "确定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String year = spDateDialog.getYear();
						String month = spDateDialog.getMonth();
						String dayOfmonth = spDateDialog.getDayOfMonth();
						if (TextUtils.isEmpty(year) || TextUtils.isEmpty(month)
								|| TextUtils.isEmpty(dayOfmonth)) {
							return;
						}

						String startDate = spDateDialog.getYear()
								+ "-"
								+ String.valueOf(Integer.parseInt(spDateDialog
										.getMonth()) + 1) + "-"
								+ spDateDialog.getDayOfMonth();

						Calendar calendar = Calendar.getInstance();
						String endDate = calendar.get(Calendar.YEAR) + "-"
								+ (calendar.get(Calendar.MONTH) + 1) + "-"
								+ calendar.get(Calendar.DAY_OF_MONTH);
						// 如果满足条件
						if (!DateUtil.isDate1Earlier(startDate, endDate)) {
							Toast.makeText(myselfContext, "您的生日必须早于当前日期",
									Toast.LENGTH_SHORT).show();
							return;
						}

						String birthday = spDateDialog.getYear()
								+ "-"
								+ DateUtil.formatNumber(Integer
										.parseInt(spDateDialog.getMonth()) + 1)
								+ "-"
								+ DateUtil.formatNumber(Integer
										.parseInt(spDateDialog.getDayOfMonth()));

						MyPlanPreference.getInstance(myselfContext)
								.setBirthday(birthday);
						initBirthday();
						spDateDialog.dismiss();
					}

				});
		spDateDialog.show();

	}

	private void showIconSelectDialog() {

		final String[] strArray = getResources().getStringArray(
				R.array.mine_icon_sel_array);

		new AlertDialog.Builder(myselfContext)
				.setTitle("请选择")
				.setSingleChoiceItems(strArray, 2,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									final int which) {
								dialog.cancel();
								switch (which) {
								case 0: // 拍照
									takePicture();
									break;
								case 1: // 相册
									openAlbum();
									break;
								}
							}
						}).show();
	}

	private void takePicture() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)
				&& Environment.getExternalStorageDirectory().exists()) {
			String fileName = String.valueOf(System.currentTimeMillis())
					+ ".jpg";
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			Uri pictureUri = Uri.fromFile(new File(Environment
					.getExternalStorageDirectory(), fileName));
			MyPlanPreference.getInstance(myselfContext).setTempPicUrl(
					pictureUri.toString());
			BmobAgent.uploadFile(myselfContext, pictureUri.toString(),new UploadListener() {

				@Override
				public void onSuccess(String fileName,String url, final BmobFile file) {
//					Log.i("bmob","文件上传成功："+fileName+",可访问的文件地址："+file.getUrl());
					// TODO Auto-generated method stub
					// fileName ：文件名（带后缀），这个文件名是唯一的，开发者需要记录下该文件名，方便后续下载或者进行缩略图的处理
					// url        ：文件地址
					// file        :BmobFile文件类型，`V3.4.1版本`开始提供，用于兼容新旧文件服务。
//					注：若上传的是图片，url地址并不能直接在浏览器查看（会出现404错误），需要经过`URL签名`得到真正的可访问的URL地址,当然，`V3.4.1`的版本可直接从'file.getUrl()'中获得可访问的文件地址。
				BmobAgent.checkUserSettingInfo(PersonalSettingAct.this,"",new FindListener<MyPlanUser>(){
					@Override
					public void onSuccess(List<MyPlanUser> object) {
						// TODO Auto-generated method stub
//						toast("查询成功：共" + object.size() + "条数据。");
						for (MyPlanUser userInfo : object) {
							userInfo.setAvatarURL(file.getUrl());
							BmobAgent.updateUserInfo(PersonalSettingAct.this, userInfo, new UpdateListener(){
								@Override
								public void onSuccess() {

								}

								@Override
								public void onFailure(int i, String s) {

								}
							});
						}
					}
					@Override
					public void onError(int code, String msg) {
						// TODO Auto-generated method stub
//						toast("查询失败：" + msg);
					}
				});
				}

				@Override
				public void onProgress(int progress) {
					// TODO Auto-generated method stub
//					Log.i("bmob","onProgress :"+progress);
				}

				@Override
				public void onError(int statuscode, String errormsg) {
					// TODO Auto-generated method stub
//					Log.i("bmob","文件上传失败："+errormsg);
				}
			});
			intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
			myselfContext.startActivityForResult(intent, REQUEST_CODE_CROP);

		} else {
			Toast.makeText(myselfContext, R.string.take_photo_msg_no_sdcard,
					Toast.LENGTH_SHORT).show();
		}
	}

	private void openAlbum() {
		Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
		openAlbumIntent.setDataAndType(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		myselfContext
				.startActivityForResult(openAlbumIntent, REQUEST_CODE_CROP);
	}

	private static final int REQUEST_CODE_CROP = 1245;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQUEST_CODE_CROP:
			if (resultCode == Activity.RESULT_OK) {
				// 拍照
				Uri uri = Uri.parse(MyPlanPreference.getInstance(myselfContext)
						.getTempPicUrl());

				// 相册取图片
				if (data != null) {
					uri = data.getData();
				}

				MyPlanPreference.getInstance(myselfContext).setHeadPicUrl(
						uri.toString());
				new LoadImageAsyncTask().execute();
			}
			break;

		default:
			break;
		}

	}

}
