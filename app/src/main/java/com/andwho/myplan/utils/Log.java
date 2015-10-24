package com.andwho.myplan.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;


import android.os.Environment;

public final class Log {

	private static final boolean ENABLE = false;
	
	
	private static final boolean LOG_SDCARD_ENABLE = false;
	private static final String LOG_FILE_NAME = "myPlan/log.txt";

	private static final boolean LOGD_ENABLE = ENABLE;
	private static final boolean LOGE_ENABLE = ENABLE;
	private static final boolean LOGI_ENABLE = ENABLE;
	private static final boolean LOGV_ENABLE = ENABLE;
	private static final boolean LOGW_ENABLE = ENABLE;

	/**
	 * Priority constant for the println method; use Log.v.
	 */
	public static final int VERBOSE = android.util.Log.VERBOSE;

	/**
	 * Priority constant for the println method; use Log.d.
	 */
	public static final int DEBUG = android.util.Log.DEBUG;

	/**
	 * Priority constant for the println method; use Log.i.
	 */
	public static final int INFO = android.util.Log.INFO;

	/**
	 * Priority constant for the println method; use Log.w.
	 */
	public static final int WARN = android.util.Log.WARN;

	/**
	 * Priority constant for the println method; use Log.e.
	 */
	public static final int ERROR = android.util.Log.ERROR;

	/**
	 * Priority constant for the println method.
	 */
	public static final int ASSERT = android.util.Log.ASSERT;

	private Log() {
	}

	public static boolean isLoggable(String tag, int level) {
		return android.util.Log.isLoggable(tag, level);
	}

	/**
	 * Send a {@link #VERBOSE} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int v(String tag, String msg) {
		if (!LOGV_ENABLE)
			return 0;
		if (LOG_SDCARD_ENABLE)
			fileLog(tag, msg);
		return android.util.Log.v(tag, msg);
	}

	/**
	 * Send a {@link #VERBOSE} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int v(String tag, String msg, Throwable tr) {
		if (!LOGV_ENABLE)
			return 0;
		if (LOG_SDCARD_ENABLE)
			fileLog(tag, msg);
		return android.util.Log.v(tag, msg, tr);
	}

	/**
	 * Send a {@link #DEBUG} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int d(String tag, String msg) {
		if (!LOGD_ENABLE)
			return 0;
		if (LOG_SDCARD_ENABLE)
			fileLog(tag, msg);
		return android.util.Log.d(tag, msg);
	}

	/**
	 * Send a {@link #DEBUG} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int d(String tag, String msg, Throwable tr) {
		if (!LOGD_ENABLE)
			return 0;
		if (LOG_SDCARD_ENABLE)
			fileLog(tag, msg);
		return android.util.Log.d(tag, msg, tr);
	}

	/**
	 * Send an {@link #INFO} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int i(String tag, String msg) {
		if (!LOGI_ENABLE)
			return 0;
		if (LOG_SDCARD_ENABLE)
			fileLog(tag, msg);
		return android.util.Log.i(tag, msg);
	}

	/**
	 * Send a {@link #INFO} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int i(String tag, String msg, Throwable tr) {
		if (!LOGI_ENABLE)
			return 0;
		if (LOG_SDCARD_ENABLE)
			fileLog(tag, msg);
		return android.util.Log.i(tag, msg, tr);
	}

	/**
	 * Send a {@link #WARN} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int w(String tag, String msg) {
		if (!LOGW_ENABLE)
			return 0;
		if (LOG_SDCARD_ENABLE)
			fileLog(tag, msg);
		return android.util.Log.w(tag, msg);
	}

	/**
	 * Send a {@link #WARN} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int w(String tag, String msg, Throwable tr) {
		if (!LOGW_ENABLE)
			return 0;
		if (LOG_SDCARD_ENABLE)
			fileLog(tag, msg);
		return android.util.Log.w(tag, msg, tr);
	}

	/*
	 * Send a {@link #WARN} log message and log the exception.
	 * 
	 * @param tag Used to identify the source of a log message. It usually
	 * identifies the class or activity where the log call occurs.
	 * 
	 * @param tr An exception to log
	 */
	public static int w(String tag, Throwable tr) {
		if (!LOGW_ENABLE)
			return 0;
		if (LOG_SDCARD_ENABLE)
			fileLog(tag, tr.getMessage());
		return android.util.Log.w(tag, tr);
	}

	/**
	 * Send an {@link #ERROR} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int e(String tag, String msg) {
		if (!LOGE_ENABLE)
			return 0;
		if (LOG_SDCARD_ENABLE)
			fileLog(tag, msg);
		return android.util.Log.e(tag, msg);
	}

	/**
	 * Send a {@link #ERROR} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int e(String tag, String msg, Throwable tr) {
		if (!LOGE_ENABLE)
			return 0;
		if (LOG_SDCARD_ENABLE)
			fileLog(tag, msg);
		return android.util.Log.e(tag, msg, tr);
	}

	/**
	 * Handy function to get a loggable stack trace from a Throwable
	 * 
	 * @param tr
	 *            An exception to log
	 */
	public static String getStackTraceString(Throwable tr) {
		return android.util.Log.getStackTraceString(tr);
	}

	/**
	 * Low-level logging call.
	 * 
	 * @param priority
	 *            The priority/type of this log message
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @return The number of bytes written.
	 */
	public static int println(int priority, String tag, String msg) {
		return android.util.Log.println(priority, tag, msg);
	}

	/**
	 * 打印日志到文件
	 * 
	 * @param tag
	 * @param msg
	 * @param logFileName
	 */
	public static void fileLog(String tag, String msg) {
		if (!Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			return;
		}
		try {
			File logFile = new File(Environment.getExternalStorageDirectory(),
					LOG_FILE_NAME);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(
					new FileWriter(logFile, true));
			bw.append(new Timestamp(System.currentTimeMillis()).toString())
					.append("----").append(tag).append("----").append(msg)
					.append("\n");
			bw.close();
		} catch (Exception e) {
		}
	}
}
