package com.stupidbeauty.hxlauncher.application;

import com.stupidbeauty.farmingbookapp.PreferenceManagerUtil;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import butterknife.Bind;
import butterknife.ButterKnife;
import android.os.Debug;
import com.upokecenter.cbor.CBORException;
import com.upokecenter.cbor.CBORObject;
import java.io.File;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import com.stupidbeauty.builtinftp.BuiltinFtpServer;
import butterknife.Bind;
import butterknife.ButterKnife;
import android.content.ClipboardManager;
import butterknife.OnClick;
import java.util.Random;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;
import com.stupidbeauty.builtinftp.BuiltinFtpServer;

/**
 * 应用程序对象。
 * @author root 蔡火胜。
 */
public class HxLauncherApplication extends Application
{
  private boolean firstRunAfterBoot=false; //!<标志，是否是启动后初次运行。
  private BuiltinFtpServer builtinFtpServer=new BuiltinFtpServer(this); //!< The builtin ftp server.

  private static HxLauncherApplication mInstance = null;
  
  /**
  *  返回 The builtin ftp server.
  */
  public BuiltinFtpServer getBuiltinFtpServer()
  {
    return builtinFtpServer;
  } // public BuiltinFtpServer getBuiltinFtpServer()

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
	
  /**
  * Choose a random port.
  */
  private int chooseRandomPort() 
  {
    Random random=new Random(); // Get the random.

    int randomIndex=random.nextInt(65535-1025)+1025; // Choose a random port.

    boolean builtinShortcutsVisible = PreferenceManagerUtil.hasPortNumber(); // 保存了随机端口号。
    
    if (builtinShortcutsVisible) // 有保存 随机端口号。
    {
      randomIndex=PreferenceManagerUtil.getPortNumber(); // 获取保存了的随机端口号。
    } // if (builtinShortcutsVisible) // 有保存 随机端口号。
    else // 未保存随机端口号。
    {
      PreferenceManagerUtil.setPortNumber(randomIndex); // 保存随机端口号。
    } // else // 未保存随机端口号。

    return randomIndex;
  } //private int chooseRandomPort()

  @Override
  /**
  * 程序被创建。
  */
  public void onCreate() 
  {
    super.onCreate(); //创建超类。

    mInstance = this;

    mContext = getApplicationContext(); //获取应用程序上下文。
    
    int actualPort=chooseRandomPort(); // Choose a random port.
    builtinFtpServer.setPort(actualPort); // Set the port.
    builtinFtpServer.setAllowActiveMode(false); // Do not allow active mode.
    builtinFtpServer.start(); // Start the builtin ftp server.
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
