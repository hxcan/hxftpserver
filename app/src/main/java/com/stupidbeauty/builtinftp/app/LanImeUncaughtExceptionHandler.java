package com.stupidbeauty.builtinftp.app;

import android.util.Log;

import java.io.FileWriter;
// import java.io.IOException;
// import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;

/**
 * Handler for uncaught exception.
 * @author root Hxcan caihuosheng@gmail.com
 */
public class LanImeUncaughtExceptionHandler implements UncaughtExceptionHandler 
{
	private static final String TAG = "LanImeUncaughtException"; //!< Tag for debug.
	private final UncaughtExceptionHandler mOldHandler;

	/**
	 * 构造函数。
	 */
	public LanImeUncaughtExceptionHandler()
	{
		mOldHandler = Thread.getDefaultUncaughtExceptionHandler(); // Remember the old exception handler.
	}

	@SuppressWarnings("DanglingJavadoc")
	@Override
	/**
	 * Process the uncaught exception.
	 */
	public void uncaughtException(Thread thread, Throwable ex) 
	{
		Log.d(TAG,"uncaughtException, caught uncaught exception"); //Debug.

		Log.d(TAG,"uncaughtException, original exception: "); //Debug.

		ex.printStackTrace(); // Report the exception.

		mOldHandler.uncaughtException(thread, ex); // Process with the old handler. 
	} //public void uncaughtException(Thread thread, Throwable ex)
}
