package com.andwho.myplan.upgrade;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.andwho.myplan.utils.Log;

import com.andwho.myplan.R;

//import org.android.tools.version.VersionPackage;

/**
 * 版本升级工具
 * 
 */
public class UpgradeUtils {

	private static final String TAG = UpgradeUtils.class.getSimpleName();
	private static ProgressDialog progressDialog;

	/**
	 * 检测新版本
	 * 
	 * @param ctx
	 */
	public static void checkNewVersion(final Activity ctx) {
		checkNewVersion(ctx, false);
	}

	/**
	 * 检测新版本
	 * 
	 * @param ctx
	 * @param isShowNoUpdateTip
	 *            是否显示更新提示
	 */
	public static void checkNewVersion(final Activity ctx,
			final boolean isShowNoUpdateTip) {

		try {

			PackageInfo info = ctx.getPackageManager().getPackageInfo(
					ctx.getPackageName(), 0);
			final String versionName = info.versionName;

			Bmob.initialize(ctx, "ed10fc9a05fd6fd5c37be211bc0726b2");

			BmobQuery<CheckUpdate> query = new BmobQuery<CheckUpdate>();
			query.findObjects(ctx, new FindListener<CheckUpdate>() {

				@Override
				public void onSuccess(List<CheckUpdate> arg0) {
					// TODO Auto-generated method stub

					Log.e("", "@@...smpp...CheckUpdate size = " + arg0);

					if (arg0 != null && arg0.size() > 0) {
						CheckUpdate versionInfo = arg0.get(0);

						// public String currentVersion;// 当前版本号
						// public String newVersion;// 新版本号
						// public String availableVersion;// 可用版本号
						// public String description;// 更新内容
						// public String isForced;// 是否强制升级 0否 1是
						// public String path;// 安装包地址
						// public String packageSize;// 安装包大小
						// versionInfo.newVersion = "2.0";
						// versionInfo.path =
						// "http://file-bak.liqucn.com/upload/2015/shipin/com.qiyi.video_6.7.1_liqucn.com.apk";
						// versionInfo.isForced = "0";
						 Log.e("", "@@...smpp...CheckUpdate currentVersion = "
						 + versionInfo.currentVersion);
						 Log.e("", "@@...smpp...CheckUpdate newVersion = "
						 + versionInfo.newVersion);
						 Log.e("",
						 "@@...smpp...CheckUpdate availableVersion = "
						 + versionInfo.availableVersion);
						 Log.e("", "@@...smpp...CheckUpdate description = "
						 + versionInfo.description);
						 Log.e("", "@@...smpp...CheckUpdate isForced = "
						 + versionInfo.isForced);
						 Log.e("", "@@...smpp...CheckUpdate path = "
						 + versionInfo.path);
						 Log.e("", "@@...smpp...CheckUpdate packageSize = "
						 + versionInfo.packageSize);

						// 当前版本小于服务器版本时才提示用户更新
						if (versionInfo != null
								&& versionInfo.newVersion != null
								&& versionName != null
								&& versionName
										.compareTo(versionInfo.newVersion) < 0) {
							versionInfo.newVersion = versionName;
							showUpgradeTipsDialog(ctx, versionInfo);
							UpgradeUtils.saveNewVersionInfo(ctx, versionInfo);
						}
					} else {
						if (isShowNoUpdateTip) {
							showNoUpdateTip(ctx);
						}
					}
				}

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub

				}
			});
		} catch (Exception e) {
			// Log.d(TAG, " " + e.getMessage());
		}

	}

	/**
	 * 将版本信息持久化
	 * 
	 * @param info
	 */
	private static void saveNewVersionInfo(Context ctx, CheckUpdate info) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(ctx.openFileOutput(
					"VersionInfo", Context.MODE_PRIVATE));
			out.writeObject(info);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送新版本更新通知
	 * 
	 * @param versionInfo
	 */
	private static void notifycation(final Activity ctx,
			final CheckUpdate versionInfo) {
		NotificationManager mNotificationManager = (NotificationManager) ctx
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent notificationIntent = new Intent(ctx, DownloadActivity.class);
		notificationIntent.putExtra("versionInfo", versionInfo);
		PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		String titleText = "发现有新版本"; // 通知标题文本()
		String contentText = "点击可下载最新版本升级";

		int iconId = ctx.getApplicationContext().getApplicationInfo().icon;

		Notification notification = new Notification(iconId, titleText,
				System.currentTimeMillis());
		notification.setLatestEventInfo(ctx, titleText, contentText,
				contentIntent);
		mNotificationManager.notify(iconId, notification);
	}

	/**
	 * 显示升级对话框
	 * 
	 * @param activity
	 * @param versionInfo
	 */
	private static void showUpgradeTipsDialog(final Activity activity,
			final CheckUpdate versionInfo) {
		if (activity instanceof Activity) {
			if (((Activity) activity).isFinishing()) {
				return;
			}
		}

		String msg = null;
		int iconId = android.R.drawable.ic_dialog_alert;
		// if ("1".equals(versionInfo.isoptional)) {
		// msg = "发现新版本:V" + versionInfo.version + "，确定更新？\n";
		msg = "超级经理 V" + versionInfo.newVersion + " 新特性：\n"
				+ "1.升级内容 \n2.升级内容 \n安装包大小：10M";
		// iconId = android.R.drawable.ic_menu_help;
		// } else {
		// msg = "发现新版本:V" + versionInfo.version + "，只有更新到最新版本才能使用!\n";
		// // iconId = android.R.drawable.ic_dialog_alert;
		// }
		String des = "";
		try {
			// des = new String((msg + versionInfo.desc).getBytes(), "UTF-8");
			des = new String((msg).getBytes(), "UTF-8");
			// des = new String((msg + "").getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		showUpgrandDialog(activity, versionInfo);
		// Builder builder = new AlertDialog.Builder(activity)
		// .setIcon(iconId)
		// .setTitle("更新")
		// .setMessage(des)
		// .setPositiveButton("下载安装",
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog,
		// int which) {
		//
		// Log.d(TAG,
		// "@@...sp..startActivity.DownloadView  ");
		//
		// try {
		// Intent it = new Intent(activity,
		// DownloadView.class);
		// it.putExtra("versionInfo", versionInfo);
		// activity.startActivity(it);
		// } catch (Exception e) {
		// Log.e(TAG,
		// "@@...sp..startActivity.exception : "
		// + e.getMessage());
		// }
		// /*
		// * [20130827] tianh 修复：
		// * 强制升级时，若用户下载完成后，不安装，此时用户仍可以使用旧版本的客户端
		// */
		// // if (!"1".equals(versionInfo.isoptional)) {
		// // activity.finish();
		// // }
		// }
		// });
		// // if ("1".equals(versionInfo.isoptional)) {
		// builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
		// {
		// public void onClick(DialogInterface dialog, int whichButton) {
		// // notifycation(activity, versionInfo);
		// }
		// });
		// // }
		// builder.setCancelable(false);
		// builder.show();
	}

	private static void showUpgrandDialog(final Activity ctx,
			final CheckUpdate versionInfo) {

		View view = ((LayoutInflater) ctx
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.upgrade_dialog, null);

		final PopupWindow popwindow = new PopupWindow(view,
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		popwindow.setFocusable(true);
		view.setFocusableInTouchMode(true);// 该属性能控制onkey是否生效
		view.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if ((keyCode == KeyEvent.KEYCODE_BACK)) {
					if ("1".equals(versionInfo.isForced)) {
						return false;
					} else {
						popwindow.dismiss();
						return true;
					}
				} else {
					return false;
				}
			}
		});
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (popwindow != null && popwindow.isShowing()) {
					if (!"1".equals(versionInfo.isForced)) {
						popwindow.dismiss();
					}
				}
				return false;
			}
		});

		// ProgressBar
		final LinearLayout ll_progressBar = (LinearLayout) view
				.findViewById(R.id.ll_progressBar);
		ll_progressBar.setVisibility(View.VISIBLE);
		// WebView
		// final WebView webview = (WebView) view.findViewById(R.id.webview);
		// webview.getSettings().setJavaScriptEnabled(true);
		// if (android.os.Build.VERSION.SDK_INT >= 8) {
		// webview.getSettings().setPluginState(PluginState.ON);
		// }
		// webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		// webview.getSettings().setDefaultTextEncodingName("UTF-8");
		// webview.setWebChromeClient(new WebChromeClient() {
		// @Override
		// public void onProgressChanged(WebView view, int newProgress) {
		// if (newProgress == 100) {
		// new Handler().postDelayed(new Runnable() {
		// public void run() {
		// ll_progressBar.setVisibility(View.GONE);
		// webview.setVisibility(View.VISIBLE);
		// }
		// }, 0);
		// }
		// }
		// });
		// // Log.d(TAG, "@@...loadUrl ...desc = " + versionInfo.desc);
		// if (!TextUtils.isEmpty(versionInfo.description)) {
		// webview.loadUrl(versionInfo.description);
		// }
		// webview.setVisibility(View.GONE);
		final TextView tv_desc = (TextView) view.findViewById(R.id.tv_desc);
		tv_desc.setText(versionInfo.description);
		ll_progressBar.setVisibility(View.GONE);
		// Button
		Button btn_left = (Button) view.findViewById(R.id.btn_left);
		if ("1".equals(versionInfo.isForced)) {
			btn_left.setVisibility(View.GONE);
		} else {
			btn_left.setVisibility(View.VISIBLE);
		}
		btn_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				popwindow.dismiss();
			}

		});

		Button btn_right = (Button) view.findViewById(R.id.btn_right);
		btn_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {
					Intent it = new Intent(ctx, DownloadActivity.class);
					it.putExtra("versionInfo", versionInfo);
					ctx.startActivity(it);

					popwindow.dismiss();

					if ("1".equals(versionInfo.isForced)) {
						ctx.finish();
					}
				} catch (Exception e) {
					// Log.e("",
					// "@@...sp..startActivity.exception : "
					// + e.getMessage());
				}
			}

		});

		popwindow.showAtLocation(ctx.findViewById(R.id.ll_root), Gravity.TOP,
				0, 0);
	}

	private static void showNoUpdateTip(final Activity activity) {
		if (activity instanceof Activity) {
			if (((Activity) activity).isFinishing()) {
				return;
			}
		}

		PackageInfo info;
		try {
			info = activity.getPackageManager().getPackageInfo(
					activity.getPackageName(), 0);
			final String versionName = info.versionName;

			// new AlertDialog.Builder(activity)
			// .setIcon(android.R.drawable.ic_dialog_alert)
			// .setTitle("升级提醒")
			// .setMessage("您的版本已是最新版本V" + versionName )
			// .setPositiveButton("确定", null).show();
			Toast.makeText(activity, "您的版本已是最新版本V" + versionName,
					Toast.LENGTH_LONG).show();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 下载自升级文件
	 * 
	 * @param context
	 * @param url
	 * @param file
	 * @param callback
	 * @return
	 */
	public static boolean downloadFile(final Context context, String url,
			final File file, final DownloadCallback callback) {

		// Log.d(TAG, "@@...sp..DownloadTask  downloadFile ");
		// Log.d(TAG, "@@...sp..DownloadTask  downloadFile url = " + url);
		// Log.d(TAG,
		// "@@...sp..DownloadTask  downloadFile file = " + file.toString());
		boolean isSccuess = false;
		try {
			RestTemplate restTemplate = new RestTemplate();
			isSccuess = restTemplate.execute(url, HttpMethod.GET, null,
					new ResponseExtractor<Boolean>() {

						@Override
						public Boolean extractData(ClientHttpResponse response) {// throws
																					// IOException
							DataInputStream in = null;
							DataOutputStream out = null;
							int downloadSize = 0;
							boolean isCancelled = false;
							boolean isSccuess = false;
							try {
								HttpHeaders headers = response.getHeaders();
								int total = 0;
								if (headers != null) {
									total = (int) headers.getContentLength();
								}
								if (response.getStatusCode() == HttpStatus.OK) {
									in = new DataInputStream(response.getBody());
								}

								out = new DataOutputStream(context
										.openFileOutput(file.getName(),
												Context.MODE_WORLD_READABLE));// 使用MODE_WORLD_READABLE才能正常安装
								byte[] buffer = new byte[1024];
								int count = 0;
								int loopCount = 0;// 循环次数
								while (!isCancelled
										&& (count = in.read(buffer)) > 0) {
									out.write(buffer, 0, count);
									downloadSize += count;
									int progress = (int) (downloadSize * 100 / total);
									if (++loopCount % 20 == 0
											|| progress == 100) {
										if (callback != null) {
											isCancelled = callback
													.onProgressChanged(
															progress,
															downloadSize, total);
										}
									}
								}
								if (downloadSize == total) {// 下载成功
									if (callback != null) {
										callback.onProgressChanged(100,
												downloadSize, total);
									}
									isSccuess = true;

									// [20130426 tianh] 保存下载时的安装包的大小(字节)
									// StorageFactory.getInstance()
									// .getEfilmPreference(context)
									// .setSoftLength(total);

								}
								// Log.d(TAG,
								// "@@...sp..DownloadTask  downloadFile isCancelled = "
								// + isCancelled);

								if (isCancelled) {
									file.delete();
								}
							} catch (Exception e) {
								// Log.e(TAG, "@@...下载apk" + e.getMessage());
							}

							return isSccuess;
						}
					});

		} catch (Exception e) {
			// Log.e(TAG, "@@...下载apk" + e.getMessage());
		}

		return isSccuess;
	}

	private static String getImsi(final Context mActivity) {
		TelephonyManager telephonyManager = (TelephonyManager) mActivity
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getSubscriberId();
		return imei;
	}

	private static String getEsn(final Activity mActivity) {
		TelephonyManager tm = (TelephonyManager) mActivity
				.getSystemService(Context.TELEPHONY_SERVICE);
		String esn = tm.getDeviceId();
		return esn;
	}

}
