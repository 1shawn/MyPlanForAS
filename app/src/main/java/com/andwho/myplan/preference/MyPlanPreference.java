package com.andwho.myplan.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPlanPreference {

	private static final String TAG = MyPlanPreference.class.getSimpleName();

	private Context context;
	private SharedPreferences spf = null;
	private static MyPlanPreference instance;

	private MyPlanPreference() {
	}

	private MyPlanPreference(Context ctx) {
		this.context = ctx;
	}

	public synchronized static MyPlanPreference getInstance(Context ctx) {
		if (instance == null) {
			instance = new MyPlanPreference(ctx);
		}
		return instance;
	}

	public final static String MYPLAN_NAME = "com.andwho.myplan.preference.myplanpreference";
	private static final String USERId = "userId";
	private static final String USERSETTINGId = "userSettingId";

	public String getUserId() {
		spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);

		return spf.getString(USERId, "");
	}

	public void setUserId(String id) {
		spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);
		spf.edit().putString(USERId, id).commit();
	}
	//设置表里面的id,用于提交小区
	public String getUserSettingId() {
		spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);

		return spf.getString(USERSETTINGId, "");
	}
	//设置表里面的id,用于提交小区
	public void setUserSettingId(String id) {
		spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);
		spf.edit().putString(USERSETTINGId, id).commit();
	}

	private static final String USERNAME = "userName";

	public String getUsername() {
		spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);

		return spf.getString(USERNAME, "");
	}

	public void setUsername(String name) {
		spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);
		spf.edit().putString(USERNAME, name).commit();
	}

	private static final String NICKNAME = "nickname";

	public String getNickname() {
		spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);

		return spf.getString(NICKNAME, "");
	}

	public void setNickname(String name) {
		spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);
		spf.edit().putString(NICKNAME, name).commit();
	}

	private static final String GENDER = "gender";

	public String getGender() {
		spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);

		return spf.getString(GENDER, "1");// 1男，0女
	}

	public void setGender(String gender) {
		spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);
		spf.edit().putString(GENDER, gender).commit();
	}

	private static final String BIRTHDAY = "birthday";

	public String getBirthday() {
		spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);

		return spf.getString(BIRTHDAY, "2000-01-01");
	}

	public void setBirthday(String birthday) {
		spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);
		spf.edit().putString(BIRTHDAY, birthday).commit();
	}

	private static final String LIFESPAN = "lifespan";

	public String getLifeSpan() {
		spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);
		return spf.getString(LIFESPAN, "100");// 100岁
	}

	public void setLifeSpan(String lifespan) {
		spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);
		spf.edit().putString(LIFESPAN, lifespan).commit();
	}

	private static final String TEMP_PIC_URL = "temp_pic_url";

	public String getTempPicUrl() {
		spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);
		return spf.getString(TEMP_PIC_URL, "");

	}

	public void setTempPicUrl(String tempPicUrl) {
		spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);
		spf.edit().putString(TEMP_PIC_URL, tempPicUrl).commit();
	}

	private static final String HEAD_PIC_URL = "head_pic_url";

	public String getHeadPicUrl() {
		spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);
		return spf.getString(HEAD_PIC_URL, "");

	}

	public void setHeadPicUrl(String headPicUrl) {
		spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);
		spf.edit().putString(HEAD_PIC_URL, headPicUrl).commit();
	}
}
