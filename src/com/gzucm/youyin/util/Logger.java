package com.gzucm.youyin.util;

import android.util.Log;

/**
 * 日志
 * 日志级别从高到低为ERROR, WARN, INFO, DEBUG, VERBOSE
  1、Log.v 的输出颜色为黑色的，输出大于或等于VERBOSE日志级别的信息
　2、Log.d的输出颜色是蓝色的，输出大于或等于DEBUG日志级别的信息
　3、Log.i的输出为绿色，输出大于或等于INFO日志级别的信息
　4、Log.w的输出为橙色, 输出大于或等于WARN日志级别的信息
　5、Log.e的输出为红色，仅输出ERROR日志级别的信息.
 * @author 李先华
 *2015年5月2日上午9:44:48
 */
public final class Logger {

	/** 日志级别 显示级别参考 android.util.Log的级别 配置0全部显示，配置大于7全不显示 */
	public static final int LEVLE = 2;

	public static void v(String tag, String msg) {
		if (LEVLE <= Log.VERBOSE)
			Log.v(tag, msg);
	}

	public static void v(String tag, String msg, Throwable tr) {
		if (LEVLE <= Log.VERBOSE)
			Log.v(tag, msg, tr);
	}

	public static void d(String tag, String msg) {
		if (LEVLE <= Log.DEBUG)
			Log.d(tag, msg);
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (LEVLE <= Log.DEBUG)
			Log.d(tag, msg, tr);
	}

	public static void i(String tag, String msg) {
		if (LEVLE <= Log.INFO)
			Logger.d(tag, msg);
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (LEVLE <= Log.INFO)
			Logger.d(tag, msg, tr);
	}

	public static void w(String tag, String msg) {
		if (LEVLE <= Log.WARN)
			Log.w(tag, msg);
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (LEVLE <= Log.WARN)
			Log.w(tag, msg, tr);
	}

	public static void w(String tag, Throwable tr) {
		if (LEVLE <= Log.WARN)
			Log.w(tag, tr.getMessage(), tr);
	}

	public static void e(String tag, String msg) {
		if (LEVLE <= Log.ERROR)
			Log.e(tag, msg);
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (LEVLE <= Log.ERROR)
			Log.e(tag, msg, tr);
	}

	public static void e(String tag, Throwable tr) {
		if (LEVLE <= Log.ERROR)
			Log.e(tag, tr.getMessage(), tr);
	}

	public static void wtf(String tag, String msg) {
		if (LEVLE <= Log.ASSERT)
			Log.wtf(tag, msg);
	}

	public static void wtf(String tag, Throwable tr) {
		if (LEVLE <= Log.ASSERT)
			Log.wtf(tag, tr);
	}

	public static void wtf(String tag, String msg, Throwable tr) {
		if (LEVLE <= Log.ASSERT)
			Logger.wtf(tag, msg, tr);
	}

}
