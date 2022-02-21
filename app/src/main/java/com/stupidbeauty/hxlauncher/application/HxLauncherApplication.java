package com.stupidbeauty.hxlauncher.application;

import android.os.Debug;
import com.upokecenter.cbor.CBORException;
import com.upokecenter.cbor.CBORObject;
import java.io.File;
import android.app.Application;
// import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
// import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
// import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
//  import com.stupidbeauty.hxlauncher.Constants;
import java.io.File;
import java.io.IOException;
// import java.util.HashMap;
import java.util.List;
import java.util.Locale;
// import com.stupidbeauty.victoriafresh.VFile;

/**
 * 应用程序对象。
 * @author root 蔡火胜。
 */
public class HxLauncherApplication extends Application
{
	private boolean firstRunAfterBoot=false; //!<标志，是否是启动后初次运行。

	private static HxLauncherApplication mInstance = null;

	public static HxLauncherApplication getInstance() 
	{
		if (mInstance == null) 
		{
			mInstance = new HxLauncherApplication();
		}
		return mInstance;
	}

	private static Context mContext;
	private static final String TAG="HxLauncherApplication"; //!<输出调试信息时使用的标记。
	
	@Override
	/**
	 * 程序被创建。
	 */
	public void onCreate() 
	{
		super.onCreate(); //创建超类。

		mInstance = this;

		mContext = getApplicationContext(); //获取应用程序上下文。
	} //public void onCreate()

	/**
	 * 获取应用程序上下文。
	 * @return 应用程序上下文。
	 */
	public static Context getAppContext() 
	{ 
		return mContext; 
	}  //public static Context getAppContext()
} // public class HxLauncherApplication extends Application
