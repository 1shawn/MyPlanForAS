package com.andwho.myplan.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.andwho.myplan.model.UserInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import cn.bmob.v3.BmobUser;

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

	private static final String NICKNAME = "nickname";

	public String getNickname() {
		String nickName="";
		try{
			Object obj=readObject(context,getUserId());
			if(obj!=null) {
				UserInfo userInfo = (UserInfo) obj;
				if(userInfo!=null&&!TextUtils.isEmpty(userInfo.nickName)) {
					nickName = userInfo.nickName;
				}
			}
		}catch (Exception ex){

		}
		if(TextUtils.isEmpty(nickName)){//为了兼容1
			spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);

			nickName= spf.getString(NICKNAME, "");
		}
		return nickName;

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
		String lifeSpan="";
		try{
			Object obj=readObject(context,getUserId());
			if(obj!=null) {
				UserInfo userInfo = (UserInfo) obj;
				if(userInfo!=null&&!TextUtils.isEmpty(userInfo.lifespan)) {
					lifeSpan = userInfo.lifespan;
				}
			}
		}catch (Exception ex){

		}
		if(TextUtils.isEmpty(lifeSpan)){//为了兼容1
			spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);
			lifeSpan= spf.getString(LIFESPAN, "100");// 100岁
		}
		return lifeSpan;

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
	private static final String USERId = "userId";
	private static final String USERSETTINGId = "userSettingId";

	public String getUserId() {
		String userId="";
		try{
			BmobUser bmobUser = BmobUser.getCurrentUser(context);
			if(bmobUser != null){
				// 允许用户使用应用
				userId=bmobUser.getObjectId();
			}else{
				//缓存用户对象为空时， 可打开用户注册界面…
			}

		}catch (Exception ex){

		}
		if(TextUtils.isEmpty(userId)){//为了兼容1
			spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);
			userId=spf.getString(USERId, "");
		}
		return userId;
	}

	public void setUserId(String id) {
		/*spf = context.getSharedPreferences(MYPLAN_NAME, Context.MODE_PRIVATE);
		spf.edit().putString(USERId, id).commit();*/
		try{
			Object obj=readObject(context,getUserId());
			if(obj!=null) {
				UserInfo userInfo = (UserInfo) obj;
				if(userInfo!=null) {
					userInfo.userId = id;
					saveObject(context,getUserId(),obj);
				}
			}
		}catch (Exception ex){

		}
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



	/**
	 * desc:保存对象

	 * @param context
	 * @param key
	 * @param obj 要保存的对象，只能保存实现了serializable的对象
	 * modified:
	 */
	private void saveObject(Context context,String key ,Object obj){
		try {
			// 保存对象
			SharedPreferences.Editor sharedata = context.getSharedPreferences(MYPLAN_NAME, 0).edit();
			//先将序列化结果写到byte缓存中，其实就分配一个内存空间
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			ObjectOutputStream os=new ObjectOutputStream(bos);
			//将对象序列化写入byte缓存
			os.writeObject(obj);
			//将序列化的数据转为16进制保存
			String bytesToHexString = bytesToHexString(bos.toByteArray());
			//保存该16进制数组
			sharedata.putString(key, bytesToHexString);
			sharedata.commit();
		} catch (IOException e) {
			e.printStackTrace();
//			Log.e("", "保存obj失败");
		}
	}
	/**
	 * desc:将数组转为16进制
	 * @param bArray
	 * @return
	 * modified:
	 */
	private String bytesToHexString(byte[] bArray) {
		if(bArray == null){
			return null;
		}
		if(bArray.length == 0){
			return "";
		}
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}
	/**
	 * desc:获取保存的Object对象
	 * @param context
	 * @param key
	 * @return
	 * modified:
	 */
	private Object readObject(Context context,String key ){
		try {
			SharedPreferences sharedata = context.getSharedPreferences(MYPLAN_NAME, 0);
			if (sharedata.contains(key)) {
				String string = sharedata.getString(key, "");
				if(TextUtils.isEmpty(string)){
					return null;
				}else{
					//将16进制的数据转为数组，准备反序列化
					byte[] stringToBytes = StringToBytes(string);
					ByteArrayInputStream bis=new ByteArrayInputStream(stringToBytes);
					ObjectInputStream is=new ObjectInputStream(bis);
					//返回反序列化得到的对象
					Object readObject = is.readObject();
					return readObject;
				}
			}
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//所有异常返回null
		return null;

	}
	/**
	 * desc:将16进制的数据转为数组
	 * <p>创建人：聂旭阳 , 2014-5-25 上午11:08:33</p>
	 * @param data
	 * @return
	 * modified:
	 */
	private byte[] StringToBytes(String data){
		String hexString=data.toUpperCase().trim();
		if (hexString.length()%2!=0) {
			return null;
		}
		byte[] retData=new byte[hexString.length()/2];
		for(int i=0;i<hexString.length();i++)
		{
			int int_ch;  // 两位16进制数转化后的10进制数
			char hex_char1 = hexString.charAt(i); ////两位16进制数中的第一位(高位*16)
			int int_ch1;
			if(hex_char1 >= '0' && hex_char1 <='9')
				int_ch1 = (hex_char1-48)*16;   //// 0 的Ascll - 48
			else if(hex_char1 >= 'A' && hex_char1 <='F')
				int_ch1 = (hex_char1-55)*16; //// A 的Ascll - 65
			else
				return null;
			i++;
			char hex_char2 = hexString.charAt(i); ///两位16进制数中的第二位(低位)
			int int_ch2;
			if(hex_char2 >= '0' && hex_char2 <='9')
				int_ch2 = (hex_char2-48); //// 0 的Ascll - 48
			else if(hex_char2 >= 'A' && hex_char2 <='F')
				int_ch2 = hex_char2-55; //// A 的Ascll - 65
			else
				return null;
			int_ch = int_ch1+int_ch2;
			retData[i/2]=(byte) int_ch;//将转化后的数放入Byte里
		}
		return retData;
	}
}
