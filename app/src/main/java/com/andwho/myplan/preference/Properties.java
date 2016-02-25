//package com.andwho.myplan.preference;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//
//import com.nostra13.universalimageloader.utils.ImageLoaderPreference;
//import com.realscloud.supercarstore.model.AdInfos;
//import com.realscloud.supercarstore.model.QueryAuthMarkResult;
//import com.realscloud.supercarstore.model.ShareResult;
//import com.realscloud.supercarstore.model.UserInfo;
//import com.realscloud.supercarstore.net.HostUrlRes;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InvalidClassException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.PrintStream;
//import java.io.Serializable;
//import java.util.HashSet;
//import java.util.Set;
//
///***
// * 缓存信息
// * **/
//public class Properties {
//	private static Context mContext;
//
//	public static void init(Context context) {
//		mContext = context;
//	}
//
//	/**
//	 * 会话id
//	 * **/
//
//	private final static String FILE_SID = "sid";
//	private static String sid;
//
//	public static synchronized String getSid() {
//		if (sid != null)
//			return sid;
//		return readString(FILE_SID);
//	}
//
//	public static synchronized boolean saveSid(String sidStr) {
//		sid = sidStr;
//		return saveString(FILE_SID, sidStr);
//	}
//
//	private static void cleanSid() {
//		sid = null;
//		if (isExistDataCache(FILE_SID)) {
//			mContext.deleteFile(FILE_SID);
//		}
//		// 清除图片缓存中的sid
//		ImageLoaderPreference.getInstance(mContext).clearSid();
//	}
//
//	/**
//	 * 用户信息
//	 * **/
//	private final static String FILE_USER_INFO = "userInfo";
//
//	public static synchronized void setUserInfo(UserInfo userInfo) {
//		saveObject(FILE_USER_INFO, userInfo);
//		saveObject(FILE_LOGIN_USER_INFO, userInfo);
//		// saveEncryptKey(userInfo.key);
//	}
//
//	public static synchronized UserInfo getUserInfo() {
//		return (UserInfo) readObject(FILE_USER_INFO);
//	}
//
//	public static void cleanUserInfo() {
//		if (isExistDataCache(FILE_USER_INFO)) {
//			mContext.deleteFile(FILE_USER_INFO);
//		}
//		cleanSid();
//		// cleanEncryptKey();
//	}
//
//	private final static String FILE_LOGIN_USER_INFO = "loginUserInfo";
//
//	// 这份登陆信息用来在重新登陆的时候“免输入账号”
//	// 所以不用删除
//	public static UserInfo getLoginUserInfo() {
//		return (UserInfo) readObject(FILE_LOGIN_USER_INFO);
//	}
//
//	// 用户信息中的"权限码"
//	public static synchronized Set<String> getAuthMarks() {
//		HashSet<String> acls = new HashSet<String>();
//		UserInfo userInfo = getUserInfo();
//		if (userInfo != null) {
//			QueryAuthMarkResult result = userInfo.authMark;
//			if (result != null) {
//				acls = result.acl;
//			}
//		}
//		return acls;
//	}
//
//	// 用户信息中的"是否为超级用户"
//	// public static synchronized boolean isSuperUser() {
//	// boolean isSuperUser = false;
//	//
//	// UserInfo userInfo = getUserInfo();
//	// if (userInfo != null) {
//	// QueryAuthMarkResult result = userInfo.authMark;
//	// if (result != null && Role.CHAO_JI_YONG_HU == result.type) {
//	// isSuperUser = true;
//	// }
//	// }
//	//
//	// return isSuperUser;
//	// }
//
//	/**
//	 * 访问主机地址
//	 * **/
//
//	private final static String FILE_HOST = "hostInfo";
//	private static String host;
//
//	public static String getHost() {
//		if (host == null) {
//			host = readString(FILE_HOST);
//		}
//
//		if (host == null) {
//			host = HostUrlRes.Produce.url;
//			Properties.saveHost(host);
//		}
//		return host;
//	}
//
//	public static boolean saveHost(String hostStr) {
//		host = hostStr;
//		return saveString(FILE_HOST, hostStr);
//	}
//
//	/**
//	 * 欢迎页url
//	 * **/
//
//	private final static String WELCOME_PAGE_URL = "welcome_page_url";
//
//	public static synchronized String getWelcomePage() {
//		return (String) readObject(WELCOME_PAGE_URL);
//	}
//
//	public static synchronized boolean saveWelcomePage(String url) {
//		return saveObject(WELCOME_PAGE_URL, url);
//	}
//
//	/**
//	 * 显示引导页
//	 * **/
//
//	private final static String SHOW_GUIDE_PAGE = "show_guide_page";
//
//	public static synchronized Boolean isShowGuidePage() {
//
//		Boolean isShowGuidePage = (Boolean) readObject(SHOW_GUIDE_PAGE);
//		if (isShowGuidePage == null) {
//			return true;
//		} else {
//			return isShowGuidePage;
//		}
//	}
//
//	public static synchronized boolean setShowGuidePage(Boolean show) {
//		return saveObject(SHOW_GUIDE_PAGE, show);
//	}
//
//	/**
//	 * 是否加密
//	 * **/
//
//	private final static String ENCRYPTION = "encryption";
//
//	public static synchronized Boolean isEncryption() {
//
//		Boolean encryption = (Boolean) readObject(ENCRYPTION);
//		if (encryption == null) {
//			return true;
//		} else {
//			return encryption;
//		}
//	}
//
//	public static synchronized boolean setEncryption(Boolean encryption) {
//		return saveObject(ENCRYPTION, encryption);
//	}
//
//	/**
//	 * 分享消息
//	 * **/
//
//	private final static String SHARE_MSG = "share_msg";
//
//	public static synchronized ShareResult getShareMsg() {
//		return (ShareResult) readObject(SHARE_MSG);
//	}
//
//	public static synchronized boolean setShareMsg(ShareResult shareResult) {
//		return saveObject(SHARE_MSG, shareResult);
//	}
//
//	/**
//	 * 开启推送
//	 * **/
//
//	private final static String PUSHABLE = "pushable";
//
//	public static synchronized Boolean isPushable() {
//		Boolean isPushable = (Boolean) readObject(PUSHABLE);
//		if (isPushable == null) {
//			return true;
//		} else {
//			return isPushable;
//		}
//	}
//
//	public static synchronized boolean setPushable(Boolean pushable) {
//		return saveObject(PUSHABLE, pushable);
//	}
//
//	/**
//	 * 上传头像地址
//	 * **/
//
//	private final static String PIC_URL = "pic_url";
//
//	public static synchronized String getPicUrl() {
//		return (String) readObject(PIC_URL);
//	}
//
//	public static synchronized boolean setPicUrl(String url) {
//		return saveObject(PIC_URL, url);
//	}
//
//	/**
//	 * 超级笔记广告
//	 * **/
//
//	private final static String ADVERTS1 = "adverts1";
//
//	public static synchronized AdInfos getAdverts1() {
//		return (AdInfos) readObject(ADVERTS1);
//	}
//
//	public static synchronized boolean setAdverts1(AdInfos infos) {
//		return saveObject(ADVERTS1, infos);
//	}
//
//	/**
//	 * 超级笔记广告
//	 * **/
//
//	private final static String ADVERTS2 = "adverts2";
//
//	public static synchronized AdInfos getAdverts2() {
//		return (AdInfos) readObject(ADVERTS2);
//	}
//
//	public static synchronized boolean setAdverts2(AdInfos infos) {
//		return saveObject(ADVERTS2, infos);
//	}
//
//	/**
//	 * 获取渠道标志
//	 * **/
//
//	private final static String IDENTIFICATION = "indentification";
//
//	public static synchronized String getChannelIdentification() {
//		return (String) readObject(IDENTIFICATION);
//	}
//
//	public static synchronized boolean saveChannelIdentification(String channel) {
//		return saveObject(IDENTIFICATION, channel);
//	}
//
//	// public static final String KEY_IDENTIFICATION = "key_indentification";
//	//
//	// // 获取渠道标志
//	// public String getChannelIdentification() {
//	// spf = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
//	//
//	// String channel = spf.getString(KEY_IDENTIFICATION, "");
//	//
//	// if (TextUtils.isEmpty(channel)) {
//	// channel = PhoneUtils.readMETAINFO(context);
//	// saveChannelIdentification(channel);
//	// }
//	//
//	// return channel;
//	// }
//	//
//	// // 保存渠道标志
//	// public void saveChannelIdentification(String channel) {
//	// spf = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
//	// spf.edit().putString(KEY_IDENTIFICATION, channel).commit();
//	// }
//
//	/**
//	 * 更新本地时间与服务器时间的差量（为下一次上报timestamp使用）
//	 * **/
//
//	private final static String SERVER_TIME_DELTA = "server_time_delta";
//
//	public static synchronized Long getServerTimeDelta() {
//		return (Long) readObject(SERVER_TIME_DELTA);
//	}
//
//	public static synchronized boolean setServerTimeDelta(Long serverTimeDelta) {
//		return saveObject(SERVER_TIME_DELTA, serverTimeDelta);
//	}
//
//	/**
//	 * 保存对象
//	 *
//	 * @param ser
//	 * @param fileName
//	 * @throws IOException
//	 */
//	private static boolean saveObject(String fileName, Serializable ser) {
//		if (isExistDataCache(fileName)) {
//			mContext.deleteFile(fileName);
//		}
//		FileOutputStream fos = null;
//		ObjectOutputStream oos = null;
//
//		try {
//			fos = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
//			oos = new ObjectOutputStream(fos);
//			oos.writeObject(ser);
//			oos.flush();
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		} finally {
//			try {
//				oos.close();
//			} catch (Exception e) {
//			}
//			try {
//				fos.close();
//			} catch (Exception e) {
//			}
//		}
//	}
//
//	/**
//	 * 读取对象
//	 *
//	 * @param fileName
//	 * @return
//	 * @throws IOException
//	 */
//	private static Serializable readObject(String fileName) {
//		if (!isExistDataCache(fileName))
//			return null;
//		FileInputStream fis = null;
//		ObjectInputStream ois = null;
//		try {
//			fis = mContext.openFileInput(fileName);
//			ois = new ObjectInputStream(fis);
//			return (Serializable) ois.readObject();
//		} catch (FileNotFoundException e) {
//		} catch (Exception e) {
//			e.printStackTrace();
//			// 反序列化失败 - 删除缓存文件
//			if (e instanceof InvalidClassException) {
//				File data = mContext.getFileStreamPath(fileName);
//				data.delete();
//			}
//		} finally {
//			try {
//				ois.close();
//			} catch (Exception e) {
//			}
//			try {
//				fis.close();
//			} catch (Exception e) {
//			}
//		}
//		return null;
//	}
//
//	/**
//	 * 判断缓存是否存在
//	 *
//	 * @param cachefile
//	 * @return
//	 */
//	private static boolean isExistDataCache(String cachefile) {
//		boolean exist = false;
//		File data = mContext.getFileStreamPath(cachefile);
//		if (data.exists())
//			exist = true;
//		return exist;
//	}
//
//	/**
//	 * 保存字符串
//	 *
//	 * @param fileName
//	 * @param str
//	 * @return
//	 */
//	private static boolean saveString(String fileName, String str) {
//
//		FileOutputStream fos = null;
//		PrintStream ps = null;
//		try {
//			fos = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
//			ps = new PrintStream(fos);
//			ps.print(str);
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		} finally {
//			try {
//				fos.close();
//				ps.close();
//			} catch (Exception e) {
//
//			}
//		}
//	}
//
//	/**
//	 * 读取文件为字符串
//	 *
//	 * @param fileName
//	 * @return
//	 */
//	private static String readString(String fileName) {
//		if (!isExistDataCache(fileName))
//			return null;
//		try {
//			FileInputStream fis = new FileInputStream(mContext.getFilesDir()
//					.getPath() + "/" + fileName);
//			byte[] buff = new byte[1024];
//			int hasRead = 0;
//			StringBuilder sb = new StringBuilder("");
//			while ((hasRead = fis.read(buff)) > 0) {
//				sb.append(new String(buff, 0, hasRead));
//			}
//			fis.close();
//			return sb.toString();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 *
//	 * 保存图片，用png格式保存
//	 *
//	 * @param bitmap
//	 *            要保存的图片
//	 * @param name
//	 *            要保存成的文件名
//	 * @param replace
//	 *            遇到已存在同名的图片，如何处理？true为替换旧的，false为重命名保存
//	 * @return 真正保存的文件名 当replace为false时，有可能发生重命名。如果返回null说明文件保存失败。
//	 */
//
//	public static String saveBitmapAsPNG(Bitmap bitmap, String name,
//			boolean replace) {
//		return saveBitmap(bitmap, name, replace, Bitmap.CompressFormat.PNG);
//
//	}
//
//	/**
//	 *
//	 * 保存图片，用JPEG格式保存
//	 *
//	 * @param bitmap
//	 *            要保存的图片
//	 * @param name
//	 *            要保存成的文件名
//	 * @param replace
//	 *            遇到已存在同名的图片，如何处理？true为替换旧的，false为重命名保存
//	 * @return 真正保存的文件名 当replace为false时，有可能发生重命名。如果返回null说明文件保存失败。
//	 */
//	public static String saveBitmapAsJPEG(Bitmap bitmap, String name,
//			boolean replace) {
//		return saveBitmap(bitmap, name, replace, Bitmap.CompressFormat.JPEG);
//
//	}
//
//
//	public static final String SD_CARD_SUPER_MANAGER_FOLDER_PATH = android.os.Environment
//			.getExternalStorageDirectory() + "/我有计划";
//	public static final String SD_CARD_IMAGE_FOLDER_PATH = SD_CARD_SUPER_MANAGER_FOLDER_PATH
//			+ "/image";
//
//	private static String saveBitmap(Bitmap bitmap, String name,
//			boolean replace, Bitmap.CompressFormat format) {
//		File f;
//		while (true) {
//			f = new File(SD_CARD_IMAGE_FOLDER_PATH, name);
//			if (f.exists()) {
//				if (replace) {
//					f.delete();
//					break;
//				} else {
//					name = name + "_NEW";
//				}
//			} else {
//				break;
//			}
//		}
//		try {
//			FileOutputStream out = new FileOutputStream(f);
//			bitmap.compress(format, 100, out);
//			out.flush();
//			out.close();
//			return name;
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//
//	}
//}
