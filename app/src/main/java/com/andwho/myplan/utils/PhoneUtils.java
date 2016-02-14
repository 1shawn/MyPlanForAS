package com.andwho.myplan.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 
 * 需要上报的基本信息
 * 
 * @author ouyyx
 * 
 */
public class PhoneUtils {
	private static final String TAG = "PhoneUtil";

	public static Map<String, String> getBasicInfo(Context ctx) {
		Map<String, String> basicInfo = new HashMap<String, String>();
		basicInfo.put("udid", PhoneUtils.getDeviceId(ctx));// 获取设备ID，设备唯一识别
		basicInfo.put("model", PhoneUtils.getModel());// 机器型号
		// basicInfo.put("sourceid", SpPreference.getInstance(ctx)
		// .getChannelIdentification());// 渠道来源
//		basicInfo.put("sourceid", ConfigParam.CHANNEL);// 渠道来源
		basicInfo.put("clientversion", PhoneUtils.getSoftVersion(ctx));// 软件版本号

		basicInfo.put("imsi", PhoneUtils.getImsi(ctx));// IMSI
		basicInfo.put("macAddress", PhoneUtils.getLocalMacAddress(ctx));// mac地址
		basicInfo.put("brand", PhoneUtils.getBrand());// 手机品牌
		basicInfo.put("os", "android");// 系统
		basicInfo.put("cpu", Build.CPU_ABI);// 处理器
		basicInfo.put("imei", PhoneUtils.getEsn(ctx));// IMEI
		basicInfo.put("osVersion", PhoneUtils.getOsVersion());// 系统版本
		basicInfo.put("manufacture", Build.MANUFACTURER);// 硬件制造商
		basicInfo.put("product", Build.PRODUCT);// 厂商
		basicInfo.put("display", PhoneUtils.getDisplay());// 内部版本号
		basicInfo.put("resolution", PhoneUtils.getResolutionString(ctx));// 分辨率
		basicInfo.put("displayMetricsDensity", PhoneUtils.getDensity(ctx));// 屏幕密度
		basicInfo.put("baseNum", PhoneUtils.getBaseNum());// 基带版本
		basicInfo.put("kernelVersion", PhoneUtils.getKernelVersion());// 内核版本
		TelephonyManager telMgr = (TelephonyManager) ctx
				.getSystemService(ctx.TELEPHONY_SERVICE);
		basicInfo.put("simState", PhoneUtils.getSimState(telMgr));// SIM卡状态
		basicInfo.put("simSerialNumber", telMgr.getSimSerialNumber());// SIM卡卡号
		basicInfo.put("networkOperatorName", telMgr.getNetworkOperatorName());// 网络供应商名称
		basicInfo.put("simOperator", telMgr.getSimOperator());// SIM卡供货商号
		basicInfo.put("simOperatorName", telMgr.getSimOperatorName());// SIM卡供货商名称
		basicInfo.put("simCountryIso", telMgr.getSimCountryIso());// SIM卡国别
		basicInfo.put("phoneType", PhoneUtils.getPhoneType(telMgr));// 手机类型
		basicInfo.put("networkType", String.valueOf(telMgr.getNetworkType()));// 网络类型
		basicInfo.put("networkOperator", telMgr.getNetworkOperator());// 网络供应商号
		basicInfo.put("line1Number", telMgr.getLine1Number());// 手机号码
		basicInfo.put("isNetworkRoaming",
				String.valueOf(telMgr.isNetworkRoaming()));// 漫游状态
		basicInfo.put("dataState", PhoneUtils.getDataState(telMgr));// 数据连接状态
		basicInfo.put("netConectionType", PhoneUtils.getNetConectionType(ctx));// 2G,3G,4G
		basicInfo.put("netConectionSubtype",
				PhoneUtils.getNetConectionSubtype(ctx));// GPRS,CDMA,EDGE等等
		basicInfo.put("locale", PhoneUtils.getLocale(ctx));// 语言
		basicInfo.put("screenHeightDp", PhoneUtils.getScreenHeightDp(ctx));// 屏幕高度的dp值
		basicInfo.put("screenWidthDp", PhoneUtils.getScreenWidthDp(ctx));// 屏幕宽度的dp值

		return basicInfo;
	}

	/**
	 * 获取手机imsi码
	 * 
	 * 国际移动用户识别码（IMSI） International Mobile Subscriber Identity
	 * 国际上为唯一识别一个移动用户所分配的号码。
	 * 
	 * IMSI共有15位，其结构如下： MCC+MNC+MIN MCC：Mobile Country Code，移动国家码，共3位，中国为460;
	 * MNC:Mobile Network
	 * Code，移动网络码，共2位，联通CDMA系统使用03，一个典型的IMSI号码为460030912121001; MIN共有10位，其结构如下：
	 * 09+M0M1M2M3+ABCD 其中的M0M1M2M3和MDN号码中的H0H1H2H3可存在对应关系，ABCD四位为自由分配。
	 */
	public static String getImsi(Context ctx) {
		TelephonyManager telephonyManager = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = telephonyManager.getSubscriberId();
		if (TextUtils.isEmpty(imsi)) {
			imsi = "phone";
		}
		return imsi;
	}

	/**
	 * 获取MAC地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	/**
	 * 
	 * 取得手机生产商
	 */
	public static String getBrand() {
		String brand = Build.BRAND == null ? "" : Build.BRAND;
		return brand;
	}

	/**
	 * 获取设备ID
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getDeviceId(Context ctx) {
		// imeistring = telephonyManager.getDeviceId();

		String imei = getEsn(ctx);
		String macAddress = getLocalMacAddress(ctx);
		String serialnum = null;
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class, String.class);
			serialnum = (String) (get.invoke(c, "ro.serialno", "unknown"));
		} catch (Exception ignored) {
		}
		String androidId = Settings.Secure.getString(ctx.getContentResolver(),
				Settings.Secure.ANDROID_ID);
//		Log.d("getDeviceId", "@@...sp...imei = " + imei + " macAddress = "
//				+ macAddress + " serialnum=" + serialnum + " androidId = "
//				+ androidId);
		StringBuilder deviceId = new StringBuilder();
		if (!TextUtils.isEmpty(imei)) {
			deviceId.append(imei);
		}
		if (!TextUtils.isEmpty(macAddress)) {
			deviceId.append(macAddress);
		}
		if (!TextUtils.isEmpty(serialnum)) {
			deviceId.append(serialnum);
		}
		if (!TextUtils.isEmpty(androidId)) {
			deviceId.append(androidId);
		}

		final String md5DeviceId = Md5.getMD5(deviceId.toString());

		return md5DeviceId;
	}

	/**
	 * 获取软件版本
	 * 
	 * @param context
	 * @return
	 */
	public static String getSoftVersion(Context context) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 取得手机系统版本
	 * 
	 * @return
	 */
	public static String getOsVersion() {
		String clientOsVersion = "android " + Build.VERSION.RELEASE;
		return clientOsVersion;
	}

	/**
	 * 获取手机MODLE,手机型号
	 */
	public static String getModel() {
		String model = Build.MODEL;
		// 模拟器环境默认使用XT800
		if ("sdk".equals(model)) {
			model = "XT800";
		}
		return model;
	}

	/**
	 * 获取手机ROM
	 */
	public static String getDisplay() {
		// String PRODUCT = Build.PRODUCT; //如 titanium
		// String DEVICE = Build.DEVICE;//如 titanium
		String DISPLAY = Build.DISPLAY; // 如 titanium-userdebug 2.1
										// TITA_K29_00.13.01I 173018 test-keys
		// String ID = Build.ID;//如 TITA_K29_00.13.01I
		// String RELEASE = Build.VERSION.RELEASE; //如 2.1
		// String INCREMENTAL = Build.VERSION.INCREMENTAL; //如 173018
		return DISPLAY;
	}

	/**
	 * 获取屏幕分辨率字符种
	 */
	public static String getResolutionString(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.heightPixels + "x" + displayMetrics.widthPixels;
	}

	public static int[] getResolution(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		return new int[] { displayMetrics.widthPixels,
				displayMetrics.heightPixels };
	}

	/**
	 * 获取屏幕
	 * 
	 */
	public static String getDensity(Context ctx) {
		float density = ctx.getResources().getDisplayMetrics().density;
		return String.valueOf(density);
	}

	/**
	 * 获取手机ESN CDMA手机机身号简称ESN-Electronic Serial Number的缩写。
	 * 
	 * 它是一个32bits长度的参数，是手机的惟一标识。
	 * 
	 * GSM手机是IMEI码(International Mobile Equipment Identity)，
	 * 
	 * 国际移动身份码。
	 */
	public static String getEsn(Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		String esn = tm.getDeviceId();// DeviceId(IMEI)
		return esn;
	}

	public static String readMETAINFO(Context ctx) {
		ClassLoader classLoader = ctx.getClassLoader();
		URL url = classLoader.getResource("META-INF/channel");
		if (url == null) {
			return null;
		}
		InputStream is = classLoader.getResourceAsStream("META-INF/channel");
		return readFile(is);
	}

	public static String readFile(InputStream in) {
		InputStreamReader is = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(is);
		String line = null;
		StringBuilder sBuilder = new StringBuilder();
		try {
			while ((line = br.readLine()) != null) {
				sBuilder.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sBuilder.toString();
	}

	/**
	 * 
	 * 基带版本
	 * 
	 * @return
	 */
	public static String getBaseNum() {
		String baseNum = "";
		try {

			Class cl = Class.forName("android.os.SystemProperties");

			Object invoker = cl.newInstance();

			Method m = cl.getMethod("get", new Class[] { String.class,
					String.class });

			Object result = m.invoke(invoker, new Object[] {
					"gsm.version.baseband", "no message" });

			baseNum = (String) result;

		} catch (Exception e) {

		}

		return baseNum;

	}

	/**
	 * 
	 * 内核版本
	 * 
	 * @return
	 */
	public static String getKernelVersion() {
		String kernelVersion = "";
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream("/proc/version");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return kernelVersion;
		}
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream), 8 * 1024);
		String info = "";
		String line = "";
		try {
			while ((line = bufferedReader.readLine()) != null) {
				info += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			if (info != "") {
				final String keyword = "version ";
				int index = info.indexOf(keyword);
				line = info.substring(index + keyword.length());
				index = line.indexOf(" ");
				kernelVersion = line.substring(0, index);
			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}

		return kernelVersion;
	}

	public static String getDataState(TelephonyManager telMgr) {
		int stateInt = telMgr.getDataState();
		String state = "";
		if (TelephonyManager.DATA_SUSPENDED == stateInt) {
			state = "SUSPENDED";
		} else if (TelephonyManager.DATA_CONNECTED == stateInt) {
			state = "CONNECTED";
		} else if (TelephonyManager.DATA_CONNECTING == stateInt) {
			state = "CONNECTING";
		} else if (TelephonyManager.DATA_DISCONNECTED == stateInt) {
			state = "DISCONNECTED";
		} else {
			state = "SUSPENDED";
		}
		return state;
	}

	public static String getSimState(TelephonyManager telMgr) {
		int stateInt = telMgr.getSimState();
		String state = "";
		if (TelephonyManager.SIM_STATE_UNKNOWN == stateInt) {
			state = "UNKNOWN";
		} else if (TelephonyManager.SIM_STATE_ABSENT == stateInt) {
			state = "ABSENT";
		} else if (TelephonyManager.SIM_STATE_PIN_REQUIRED == stateInt) {
			state = "PIN_REQUIRED";
		} else if (TelephonyManager.SIM_STATE_PUK_REQUIRED == stateInt) {
			state = "PUK_REQUIRED";
		} else if (TelephonyManager.SIM_STATE_NETWORK_LOCKED == stateInt) {
			state = "LOCKED";
		} else if (TelephonyManager.SIM_STATE_READY == stateInt) {
			state = "READY";
		}
		return state;
	}

	public static String getPhoneType(TelephonyManager telMgr) {
		int stateInt = telMgr.getPhoneType();
		String state = "";
		if (TelephonyManager.PHONE_TYPE_NONE == stateInt) {
			state = "NONE";
		} else if (TelephonyManager.PHONE_TYPE_GSM == stateInt) {
			state = "GSM";
		} else if (TelephonyManager.PHONE_TYPE_CDMA == stateInt) {
			state = "CDMA";
		}
		return state;
	}

	/** Network type is unknown */
	public static final int NETWORK_TYPE_UNKNOWN = 0;
	/** Current network is GPRS */
	public static final int NETWORK_TYPE_GPRS = 1;
	/** Current network is EDGE */
	public static final int NETWORK_TYPE_EDGE = 2;
	/** Current network is UMTS */
	public static final int NETWORK_TYPE_UMTS = 3;
	/** Current network is CDMA: Either IS95A or IS95B */
	public static final int NETWORK_TYPE_CDMA = 4;
	/** Current network is EVDO revision 0 */
	public static final int NETWORK_TYPE_EVDO_0 = 5;
	/** Current network is EVDO revision A */
	public static final int NETWORK_TYPE_EVDO_A = 6;
	/** Current network is 1xRTT */
	public static final int NETWORK_TYPE_1xRTT = 7;
	/** Current network is HSDPA */
	public static final int NETWORK_TYPE_HSDPA = 8;
	/** Current network is HSUPA */
	public static final int NETWORK_TYPE_HSUPA = 9;
	/** Current network is HSPA */
	public static final int NETWORK_TYPE_HSPA = 10;
	/** Current network is iDen */
	public static final int NETWORK_TYPE_IDEN = 11;
	/** Current network is EVDO revision B */
	public static final int NETWORK_TYPE_EVDO_B = 12;
	/** Current network is LTE */
	public static final int NETWORK_TYPE_LTE = 13;
	/** Current network is eHRPD */
	public static final int NETWORK_TYPE_EHRPD = 14;
	/** Current network is HSPA+ */
	public static final int NETWORK_TYPE_HSPAP = 15;

	public static String getNetConectionSubtype(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		String netConectionType = "unknown";
		if (activeNetInfo != null && activeNetInfo.isConnected()) {
			if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				netConectionType = "wifi";
			} else if (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
				// if (ni != null && ni.isConnectedOrConnecting()) {
				switch (ni.getSubtype()) {
				case NETWORK_TYPE_GPRS:
					netConectionType = "GPRS";
					break; // 联通2g
				case NETWORK_TYPE_CDMA:
					netConectionType = "CDMA";
					break; // 电信2g
				case NETWORK_TYPE_EDGE:
					netConectionType = "EDGE";
					break;// 移动2g
				case NETWORK_TYPE_1xRTT:
					netConectionType = "1xRTT";
					break;
				case NETWORK_TYPE_IDEN:
					netConectionType = "IDEN";
					break;
				case NETWORK_TYPE_EVDO_A:
					netConectionType = "EVDO_A";
					break; // 电信3g
				case NETWORK_TYPE_UMTS:
					netConectionType = "UMTS";
					break;
				case NETWORK_TYPE_EVDO_0:
					netConectionType = "EVDO_0";
					break;
				case NETWORK_TYPE_HSDPA:
					netConectionType = "HSDPA";
					break;
				case NETWORK_TYPE_HSUPA:
					netConectionType = "HSUPA";
					break;
				case NETWORK_TYPE_HSPA:
					netConectionType = "HSPA";
					break;
				case NETWORK_TYPE_EVDO_B:
					netConectionType = "EVDO_B";
					break;
				case NETWORK_TYPE_EHRPD:
					netConectionType = "EHRPD";
					break;
				case NETWORK_TYPE_HSPAP:
					netConectionType = "HSPAP";
					break;
				case NETWORK_TYPE_LTE:
					netConectionType = "LTE";
					break;
				default:
					netConectionType = "unknown";
				}
			}
			// }
		}
		return netConectionType;
	}

	public static String getNetConectionType(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		String netConectionType = "unknown";
		if (activeNetInfo != null && activeNetInfo.isConnected()) {
			if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				netConectionType = "wifi";
			} else if (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
				if (ni != null && ni.isConnectedOrConnecting()) {
					switch (ni.getSubtype()) {
					case NETWORK_TYPE_GPRS: // 联通2g
					case NETWORK_TYPE_CDMA: // 电信2g
					case NETWORK_TYPE_EDGE: // 移动2g
					case NETWORK_TYPE_1xRTT:
					case NETWORK_TYPE_IDEN:
						netConectionType = "2G";
						break;
					case NETWORK_TYPE_EVDO_A: // 电信3g
					case NETWORK_TYPE_UMTS:
					case NETWORK_TYPE_EVDO_0:
					case NETWORK_TYPE_HSDPA:
					case NETWORK_TYPE_HSUPA:
					case NETWORK_TYPE_HSPA:
					case NETWORK_TYPE_EVDO_B:
					case NETWORK_TYPE_EHRPD:
					case NETWORK_TYPE_HSPAP:
						netConectionType = "3G";
						break;
					case NETWORK_TYPE_LTE:
						netConectionType = "4G";
						break;
					default:
						netConectionType = "unknown";
					}
				}
			}
		}
		return netConectionType;
	}

	public static String getLocale(Context ctx) {
		// zh,CN,中文 (中国),CHN,zho
		Locale locale = ctx.getResources().getConfiguration().locale;
		return locale.getLanguage() + "," + locale.getCountry() + ","
				+ locale.getDisplayName() + "," + locale.getISO3Country() + ","
				+ locale.getISO3Language();
	}

	public static String getScreenHeightDp(Context ctx) {
		return String
				.valueOf(ctx.getResources().getConfiguration().screenHeightDp);
	}

	public static String getScreenWidthDp(Context ctx) {
		return String
				.valueOf(ctx.getResources().getConfiguration().screenWidthDp);
	}

//	public static boolean checkSignature(Context context) {
//		boolean checkright = false;
//		PackageInfo packageInfo;
//		try {
//			final String packname = context.getPackageName();
//			packageInfo = context.getPackageManager().getPackageInfo(packname,
//					PackageManager.GET_SIGNATURES);
//			Signature[] signs = packageInfo.signatures;
//			Signature sign = signs[0];
//			int code = sign.hashCode();
//			if (code == ConfigParam.RELEASE_KEY_MD5) {
//				checkright = true;
//			}
//		} catch (NameNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return checkright;
//
//	}

	
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {

			return mNetworkInfo.isAvailable();
		}
		return false;
	}
	
}
