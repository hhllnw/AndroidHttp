package com.inthub.baselibrary.common;

import android.util.Log;

public class Logger {

	public static final int LOG_LEVEL = 7;
	public static final int DEBUG = 2;
	public static final int INFO = 3;
	public static final int VERBSON = 4;
	public static final int WARN = 5;
	public static final int ERROR = 6;
	public static boolean islog = false;

	public static void D(String tag, String msg) {
		if (LOG_LEVEL > DEBUG && islog) {
			Log.d(tag, msg);
		}
	}

	public static void I(String tag, String msg) {
		if (LOG_LEVEL > INFO && islog) {
			Log.i(tag, msg);
		}
	}

	public static void E(String tag, String msg) {
		if (LOG_LEVEL > ERROR && islog) {
			Log.e(tag, msg);
		}
	}

	public static void V(String tag, String msg) {
		if (LOG_LEVEL > VERBSON && islog) {
			Log.v(tag, msg);
		}
	}

	public static void W(String tag, String msg) {
		if (LOG_LEVEL > WARN && islog) {
			Log.w(tag, msg);
		}
	}

}
