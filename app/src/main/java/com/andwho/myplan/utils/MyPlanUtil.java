package com.andwho.myplan.utils;

import java.math.BigDecimal;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.util.TypedValue;

public class MyPlanUtil {
	public static float getFinishRate(String strFinish, String strTarget) {
		if (TextUtils.isEmpty(strFinish) || "null".equals(strFinish)) {
			strFinish = "0.0";
		}
		BigDecimal finishAmount = new BigDecimal(strFinish);
		if (TextUtils.isEmpty(strTarget) || "null".equals(strTarget)
				|| "0.0".equals(strTarget)) {
			strTarget = "1";
		}
		BigDecimal targetAmount = new BigDecimal(strTarget);
		BigDecimal rate = finishAmount.divide(targetAmount, 2,
				BigDecimal.ROUND_HALF_UP);
		BigDecimal result = rate.multiply(new BigDecimal("100"));
		return Float.parseFloat(result.toString());
	}
	
	public static int dip2px(Activity act, float dpValue) {

		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpValue, act.getResources().getDisplayMetrics());
		// final float scale =
		// mcontext.getResources().getDisplayMetrics().density;
		// return (int) (dpValue * scale + 0.5f);
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
}
