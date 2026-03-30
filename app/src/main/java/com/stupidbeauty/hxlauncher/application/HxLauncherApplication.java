package com.stupidbeauty.hxlauncher.application;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import androidx.documentfile.provider.DocumentFile;
import java.io.File;
import com.stupidbeauty.hxlauncher.activity.ApplicationInformationActivity;
import com.stupidbeauty.hxlauncher.SettingsActivity;
import com.stupidbeauty.hxlauncher.activity.ApplicationInformationActivity;
import com.stupidbeauty.hxlauncher.SettingsActivity;
import com.stupidbeauty.codeposition.CodePosition;
import java.io.FileDescriptor;
import java.util.Timer;
import java.util.TimerTask;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import com.stupidbeauty.builtinftp.demo.R;
import com.stupidbeauty.voiceui.VoiceUi;
import com.stupidbeauty.hxlauncher.service.DownloadNotificationService; 
import com.stupidbeauty.ftpserver.lib.EventListener;
import com.stupidbeauty.ftpserver.lib.RenameInformationObject;
import android.app.Activity;
import com.stupidbeauty.builtinftp.demo.FtpEventListenerInterface;
import com.stupidbeauty.hxftpserver.ErrorReporter;
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
import com.stupidbeauty.upgrademanager.UpgradeManager;
import com.stupidbeauty.builtinftp.demo.FtpEventListener;
import com.stupidbeauty.hxlauncher.factory.MessageFactory;
import android.os.Process;
import com.stupidbeauty.builtinftp.BuiltinFtpServer;
import java.util.TimerTask;
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
import butterknife.ButterKnife;
import android.content.ClipboardManager;
import butterknife.OnClick;
import java.util.Random;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;
import com.stupidbeauty.builtinftp.BuiltinFtpServer;
import com.stupidbeauty.hxlauncher.factory.MessageFactory;
import com.stupidbeauty.hxlauncher.bean.ApplicationNameInternationalizationData;
import com.stupidbeauty.hxlauncher.bean.WakeLockPackageNameSetData;
import com.stupidbeauty.hxlauncher.datastore.RuntimeInformationStore;
import java.io.File;
import java.io.IOException;

/**
 * 应用程序对象。
 * @author root 蔡火胜。
 */
public class HxLauncherApplication extends Application implements FtpEventListenerInterface
{
	private MessageFactory messageFactory=new MessageFactory(); //!<消息工厂。
  private boolean firstRunAfterBoot=false; //!<标志，是否是启动后初次运行。
  private BuiltinFtpServer builtinFtpServer=new BuiltinFtpServer(this); //!< The builtin ftp server.
  private UpgradeManager upgradeManager=null; //!< upgrade manager.
  private static HxLauncherApplication mInstance = null;
  private VoiceUi voiceUi=null; //!< 语音交互对象。
	private ApplicationNameInternationalizationData applicationNameInternationalizationData ; //!<应用程序可读名字国际化数据。
  private Timer timerObj = null; //!< 用于报告下载完毕的定时器。
  
	public ApplicationNameInternationalizationData getApplicationNameInternationalizationData() {
		return applicationNameInternationalizationData;
	}

	public MessageFactory getMessageFactory() {
		return messageFactory;
	}

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
  private int chooseRandomPort(boolean forceNewPort) 
  {
    Random random=new Random(); // Get the random.

    int newPortNumber = random.nextInt(65535-1025)+1025; // Choose a random port.

    boolean hasExistingPortNumberle = PreferenceManagerUtil.hasPortNumber(); // 保存了随机端口号。
    
    if (hasExistingPortNumberle) // 有保存 随机端口号。
    {
      if (forceNewPort) // Forced to use new port.
      {
        PreferenceManagerUtil.setPortNumber(newPortNumber); // 保存随机端口号。
      } // if (forceNewPort) // Forced to use new port.
      else // Not forced to use new port.
      {
        newPortNumber = PreferenceManagerUtil.getPortNumber(); // 获取保存了的随机端口号。
      } // else // Not forced to use new port.
    } // if (builtinShortcutsVisible) // 有保存 随机端口号。
    else // 未保存随机端口号。
    {
      PreferenceManagerUtil.setPortNumber(newPortNumber); // 保存随机端口号。
    } // else // 未保存随机端口号。

    return newPortNumber;
  } //private int chooseRandomPort()

  /**
  * Detect the ip.
  */
  private String detectIp()
  {
    String ipAddress = null;

    WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());

    Log.d(TAG, "109, detectIp, ipAddress: " + ipAddress); // Debug.

    if (ipAddress.equals("0.0.0.0")) // hotspot
    {
      ipAddress= getHotspotIPAddress(); // Get hotspot ip addrss
      Log.d(TAG, "114, detectIp, ipAddress: " + ipAddress); // Debug.

      ipAddress= getIpAddress(); // Get hotspot ip addrss
      Log.d(TAG, "120, detectIp, ipAddress: " + ipAddress); // Debug.
    } // if (ipAddress.equals("0.0.0.0")) // hotspot

    return ipAddress;
  } // private String detectIp()

  private String getIpAddress()
  {
    String ip = "";
    boolean found=false;
    try
    {
      Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
      while (enumNetworkInterfaces.hasMoreElements())
      {
        NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
        Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();
        while (enumInetAddress.hasMoreElements())
        {
          InetAddress inetAddress = enumInetAddress.nextElement();

          if (inetAddress.isSiteLocalAddress())
          {
            ip = inetAddress.getHostAddress();
            Log.d(TAG, "164, getIpAddress, ipAddress: " + ip); // Debug.

            if (ip.startsWith("192.168."))
            {
              found=true;
              break;
            }
          }
        }
        if (found)
        {
          break;
        }
      }
    }
    catch (SocketException e)
    {
      e.printStackTrace();
      ip += "Something Wrong! " + e.toString() + "\n";
    }
    return ip;
  }

  private String getHotspotIPAddress()
  {
    WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

//     int ipAddress = wifiManager.getDhcpInfo().serverAddress;
    int ipAddress = wifiManager.getDhcpInfo().gateway;

    Log.d(TAG, "114, getHotspotIPAddress, ipAddress: " + ipAddress); // Debug.

    if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN))
    {
      ipAddress = Integer.reverseBytes(ipAddress);
      Log.d(TAG, "152, getHotspotIPAddress, ipAddress: " + ipAddress); // Debug.
    }

    byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

    Log.d(TAG, "157, getHotspotIPAddress, ipByteArray: " + ipByteArray.toString()); // Debug.

    String ipAddressString;
    try
    {
      ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
      Log.d(TAG, "163, getHotspotIPAddress, ipAddressString: " + ipAddressString); // Debug.
    }
    catch (UnknownHostException ex)
    {
      ipAddressString = "";
      Log.d(TAG, "168, getHotspotIPAddress, ipAddressString: " + ipAddressString); // Debug.
    }

    return ipAddressString;
  }

    /**
    * 告知文件下载完毕。
    */
    public void notifyDownloadFinish()
    {
      Log.d(TAG, "notifyDownloadFinish"); // Debug.
      
      // 陈欣。启动一个定时器。
      
      cancelNotifyDownloadFinish(); // 取消通知，文件下载完毕。
      
      timerObj = new Timer();
      TimerTask timerTaskObj = new TimerTask() 
      {
        public void run() 
        {
//           startBultinFtpServer(); // 启动内置 FTP 服务器。
          String downloadFinished = getResources().getString(R.string.downloadFinished); // 读取 说明 字符串。

          Log.d(TAG, "notifyDownloadFinish, text: " + downloadFinished); // Debug.

          voiceUi.say(downloadFinished); // 发声。
          Log.d(TAG, "notifyDownloadFinish, said: " + downloadFinished); // Debug.
        }
      };
      timerObj.schedule(timerTaskObj, 18000); // 延时启动。
    } // notifyDownloadFinish(); // 告知文件下载完毕。
    
    /**
    * Notify upload finish.
    */
    public void notifyUploadFinish(Object eventContent)
    {
      scanDocumentFile(eventContent);
    } // notifyDownloadFinish(); // 告知文件下载完毕。
    
    /**
    * browse document tree.
    */
    public void browseDocumentTree(Object eventContent)
    {
    } // public void browseDocumentTree(Object eventContent)

    /**
    * 取消通知，文件下载完毕。
    */
    private void cancelNotifyDownloadFinish() 
    {
      if (timerObj!=null) // 定时器存在
      {
        timerObj.cancel(); // 取消。
      } // if (timerObj!=null) // 定时器存在
    } // private void cancelNotifyDownloadFinish()
    
    /**
    * 告知文件下载开始。
    */
    public void notifyDownloadStart()
    {
      cancelNotifyDownloadFinish(); // 取消通知。
    } // notifyDownloadStart(); // 告知文件下载开始。
    
    /**
    * notify file delete.
    */
    public void notifyDelete(Object eventContent)
    {
      scanDocumentFile(eventContent);
    } // public void notifyDelete(Object eventContent)
    
    /**
    * notify file rename.
    */
    public void notifyRename(Object eventContent)
    {
      RenameInformationObject uploadedFile=(RenameInformationObject)(eventContent);
      
      DocumentFile fileObject = uploadedFile.getFile(); // Gett he file object.

      scanDocumentFile(fileObject);
      
      // Scan the original file name to make the system forget it:
      String oroiginalName = uploadedFile.getOriginalName(); // Get the original name.
      
      Uri uri=fileObject.getUri();
      
      String scheme=uri.getScheme();
      
      if (scheme.equals("file")) // It is a file
      {
        String path=uri.getPath();

        File rawFile=new File(path);

        File parentVirtualFile=rawFile.getParentFile();
          
        String currentTryingPath=parentVirtualFile.getPath();

        // File parentDirectory = 
        String oroiginalFilePath = currentTryingPath + "/" + oroiginalName; // Construct the original ifle path.
        
        File OroiginalFile = new File(oroiginalFilePath);
        
        requestScanFile(OroiginalFile); // Request scan file.
      } // if (scheme.equals("file")) // It is a file

    } // public void notifyRename(Object eventContent)
    
  @Override
  /**
  *  告知 ip change.
  */
  public void notifyIpChange()
  {
    String downloadFinished = getResources().getString(R.string.ipChanged); // 读取 说明 字符串。

    Log.d(TAG, "notifyDownloadFinish, text: " + downloadFinished); // Debug.

    voiceUi.say(downloadFinished); // 发声。
  } // public void notifyIpChange()
    
  @Override
  /**
  * 程序被创建。
  */
  public void onCreate() 
  {
    super.onCreate(); // craete supoer.

    mInstance = this;

    mContext = getApplicationContext(); //获取应用程序上下文。
    
    boolean allowAnonymous=PreferenceManagerUtil.getAllowAnonymous(); // Get settings, whether allow anonymous.
    boolean externalStoragePerformanceOPtimize=PreferenceManagerUtil.getExternalStoragePerformanceOptimize(); // Get settings, whether do external storage performance optimize.
    
    ErrorReporter errorReporter=new ErrorReporter(mContext); // CreATE THE error reporter.

    selectPort(); // Select a port.

    voiceUi=new VoiceUi(this); // 创建语音交互对象。

    String actualIp = detectIp(); // Detect the ip.
    builtinFtpServer.setIp(actualIp); // Set the ip.

    builtinFtpServer.setErrorListener(errorReporter); // Set the error reporter.
    builtinFtpServer.setAllowActiveMode(true); // allow active mode.
    builtinFtpServer.setAllowAnonymous(allowAnonymous); // Whether allow anonymous.
    builtinFtpServer.setExternalStoragePerformanceOptimize(externalStoragePerformanceOPtimize); // Set option.
    builtinFtpServer.setFileNameTolerant(true); // File name tolerant. For example: /Android/data/com.client.xrxs.com.xrxsapp/files/XrxsSignRecordLog/Zw40VlOyfctCQCiKL_63sg==, with a trailing <LF> (%0A).
    builtinFtpServer.start(); // Start the builtin ftp server.

    initializeEventListener(); // 初始化事件监听器。

		startCheckUpgrade(); // Start check upgrade.
  } //public void onCreate()

    /**
    * guide, external storage manager permission.
    */
    public void guideExternalStorageManagerPermission(Object eventContent)
    {
      Log.d(TAG, "gotoLoginActivity, 119."); //Debug.
      Intent launchIntent=new Intent(this, ApplicationInformationActivity.class); //启动意图。

      startActivity(launchIntent); //启动活动。

      Log.d(TAG, "gotoLoginActivity, 122."); //Debug.
    } // public void guideExternalStorageManagerPermission(Object eventContent)
    
  /**
  * 初始化事件监听器。
  */
  private void initializeEventListener()
  {
    FtpEventListener eventListener=new FtpEventListener(); // 创建事件监听器。
    eventListener.registerCallback(this); // Register callback.

    builtinFtpServer.setEventListener(eventListener); // 设置事件监听器。
  } //private void initializeEventListener()

    /**
    * 刷新可用空间数量。
    */
    public void refreshAvailableSpace() 
    {
    } //private void refreshAvailableSpace()

    /**
    * Request scan file.
    */
    private void requestScanFile(File uploadedFile) 
    {
      if (uploadedFile!=null) // The file exists
      {
        scanFile(uploadedFile.getAbsolutePath()); // scan this file.
      } // if (uploadedFile!=null) // The file exists
    } // private void requestScanFile(File uploadedFile)
    
  private void scanDocumentFile(Object eventContent)
  {
    // chen xin . notify upload finish
    
    DocumentFile uploadedFile=(DocumentFile)(eventContent);
    
    Uri uri=uploadedFile.getUri();
    
    String scheme=uri.getScheme();
    
    if (scheme.equals("file")) // It is a file
    {
      String path=uri.getPath();
      
      File rawFile=new File(path);
      requestScanFile(rawFile); // Request scan file.
    } // if (scheme.equals("file")) // It is a file
  } // private void scanDocumentFile(Object eventContent)

    /**
     * 要求扫描照片。
     * @param path 照片文件的路径。
     */
    private void scanFile(String path)
    {
        MediaScannerConnection.scanFile(this,
          new String[] { path }, null,
          new MediaScannerConnection.OnScanCompletedListener() 
          {
            public void onScanCompleted(String path, Uri uri) 
            {
              Log.i("TAG", "Finished scanning " + path);
            }
          });
    } //private void scanFile(String path)
    
  /**
  * Select another port.
  */
  public void selectPort()
  {
    selectPort(false); // Use exisintg port if possible.
  } // public void selectPort()

  /**
  * Select another port.
  */
  public void selectPort(boolean forceuseNewPort)
  {
    int actualPort = chooseRandomPort(forceuseNewPort); // Choose a random port.
    builtinFtpServer.setPort(actualPort); // Set the port.
  } // public void selectPort()

	/**
	* Start check upgrade.
	*/
	public void startCheckUpgrade() 
	{
    if (upgradeManager==null) // Upgrade manager does not exist
    {
      upgradeManager=new UpgradeManager(this); // Create upgrade manager.
    } // if (upgradeManager==null) // Upgrade manager does not exist
      
		// upgradeManager.setPackageNameUrlMapDataListener(this);
      
		upgradeManager.checkUpgrade(); // Check upgrade.
	} // private void startCheckUpgrade()

  /**
  * 获取应用程序上下文。
  * @return 应用程序上下文。
  */
  public static Context getAppContext() 
  { 
    return mContext; 
  }  //public static Context getAppContext()
} // public class HxLauncherApplication extends Application
