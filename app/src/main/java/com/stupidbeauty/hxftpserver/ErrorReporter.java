package com.stupidbeauty.hxftpserver;

import com.stupidbeauty.ftpserver.lib.Constants;
import com.stupidbeauty.builtinftp.demo.R;
import com.stupidbeauty.voiceui.VoiceUi;
import com.stupidbeauty.hxlauncher.service.DownloadNotificationService; 
import com.stupidbeauty.ftpserver.lib.EventListener;
import android.app.Activity;
import android.net.wifi.WifiManager;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.net.SocketException;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.net.ConnectivityManager;
import java.nio.ByteOrder;
import java.math.BigInteger;
import android.net.wifi.WifiManager;
import java.util.Random;
import java.net.InetAddress;
import java.net.UnknownHostException;
import android.net.Uri;
import com.stupidbeauty.farmingbookapp.PreferenceManagerUtil;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
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
// import butterknife.Bind;
import butterknife.ButterKnife;
import android.content.ClipboardManager;
import butterknife.OnClick;
import java.util.Random;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;
import com.stupidbeauty.builtinftp.BuiltinFtpServer;
import com.stupidbeauty.builtinftp.ErrorListener;

/**
 * 应用程序对象。
 * @author root 蔡火胜。
 */
public class ErrorReporter implements ErrorListener
{
  private VoiceUi voiceUi=null; //!< 语音交互对象。
  private boolean firstRunAfterBoot=false; //!<标志，是否是启动后初次运行。
  private static HxLauncherApplication mInstance = null;
  
  public ErrorReporter(Context context)
  {
    voiceUi=new VoiceUi(context); // Create voice ui object.

    this.mContext=context; 
  } // public ErrorReporter(Context context)
  
  /**
  * Error occured. Chen xin.
  */
  public void onError(Integer errorCode)
  {
    if (errorCode==Constants.ErrorCode.ControlConnectionEndedUnexpectedly) // Connection ended unexpectedly
    {
      // Chen xin.
      String downloadFinished = mContext.getResources().getString(R.string.controlConnectionEndedUnexpectedlyged); // Load the text content.

      Log.d(TAG, "notifyDownloadFinish, text: " + downloadFinished); // Debug.

      voiceUi.say(downloadFinished); // 发声。
    } // if (errorCode==Constants.ErrorCode.ControlConnectionEndedUnexpectedly) // Connection ended unexpectedly
    else if (errorCode==Constants.ErrorCode.ADDRESS_ALREADY_IN_USE) // Address already in use
    {
      HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance() ; // 获取应用程序实例。
      hxLauncherApplication.selectPort(); // Select another port.
      
      BuiltinFtpServer builtinFtpServer=null; // The builtin ftp server.
      builtinFtpServer = hxLauncherApplication.getBuiltinFtpServer(); // 获取FTP服务器实例对象。
      builtinFtpServer.start(); // Start again.
    } // else if (errorCode==Constants.ErrorCode.ADDRESS_ALREADY_IN_USE) // Address already in use
  } // public void onError(Integer errorCode)
  
  private static Context mContext;
  private static final String TAG="ErrorReporter"; //!< The tag used for debug.
	
  /**
  * 获取应用程序上下文。
  * @return 应用程序上下文。
  */
  public static Context getAppContext() 
  { 
    return mContext; 
  }  //public static Context getAppContext()
} // public class HxLauncherApplication extends Application
