package com.stupidbeauty.builtinftp.demo;

import com.stupidbeauty.farmingbookapp.PreferenceManagerUtil;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import butterknife.Bind;
import butterknife.ButterKnife;
import android.os.Debug;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import android.util.Log;
import java.util.Date;    
import java.time.format.DateTimeFormatter;
import java.io.File;
import com.koushikdutta.async.AsyncServerSocket;
import com.stupidbeauty.voiceui.VoiceUi;
import com.stupidbeauty.hxlauncher.service.DownloadNotificationService; 
import com.stupidbeauty.ftpserver.lib.EventListener;
import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import java.util.Random;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;
import com.stupidbeauty.builtinftp.BuiltinFtpServer;
import butterknife.Bind;
import butterknife.ButterKnife;
import android.content.ClipboardManager;
import butterknife.OnClick;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import com.stupidbeauty.hxlauncher.Constants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.stupidbeauty.hxlauncher.manager.ActiveUserReportManager;
import android.os.Debug;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import java.util.Timer;
import java.util.TimerTask;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.CheckBox;
import com.stupidbeauty.farmingbookapp.PreferenceManagerUtil;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import butterknife.Bind;
import android.widget.RelativeLayout;

public class LauncherActivity extends Activity 
{
  private static final String TAG="LauncherActivity"; //!< 输出调试信息时使用的标记
  private VoiceUi voiceUi=null; //!< 语音交互对象。
  private Timer timerObj = null; //!< 用于报告下载完毕的定时器。

  private ActiveUserReportManager activeUserReportManager=null; //!< 活跃用户统计管理器。陈欣。
  private BuiltinFtpServer builtinFtpServer=null; //!< The builtin ftp server.

  @Bind(R.id.statustextView) TextView statustextView; //!< Label to show status text.
  @Bind(R.id.availableSpaceView) TextView availableSpaceView; //!< 可用空间。
  @Bind(R.id.allowAnonymousetei) CheckBox allowAnonymousetei; //!< Allow anonymous check box.
  @Bind(R.id.userNamePassWordayout) RelativeLayout userNamePassWordayout; //!< User name pass word layout.
    
  @OnClick(R.id.copyUrlButton)
  public void copyUrlButton()
  {
    String stringNodeCopied= statustextView.getText().toString();

    ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData clip = android.content.ClipData.newPlainText("Copied", stringNodeCopied);

    clipboard.setPrimaryClip(clip);
  }

  @Override
  /**
  * The activity is being created.
  */
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.launcher_activity);

    ButterKnife.bind(this); // Inject view.
    
    voiceUi=new VoiceUi(this); // 创建语音交互对象。

//     WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
//     String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
      
    HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance() ; // 获取应用程序实例。
    builtinFtpServer=hxLauncherApplication.getBuiltinFtpServer(); // 获取FTP服务器实例对象。
    
    int actualPort=builtinFtpServer.getActualPort(); // 获取实际的端口。
    String actualIp=builtinFtpServer.getIp(); // Get the actual ip.

    String ftpUrl="ftp://"+ actualIp + ":"+ actualPort +"/"; // Construct the ftp server url.

    statustextView.setText(ftpUrl); // Show the FTP url
        
    initializeEventListener(); // 初始化事件监听器。
        
    startTimeCheckService(); // 启动下载通知服务。陈欣。
    
    loadSettings(); // Load settings.
  } //protected void onCreate(Bundle savedInstanceState)
  
  /**
    * 载入选项。
    */
  private void loadSettings()
  {
    boolean builtinShortcutsVisible= PreferenceManagerUtil.getAllowAnonymous(); //内置快捷方式是否可见。

    allowAnonymousetei.setChecked(builtinShortcutsVisible); //切换是否选中。
    
    toggleUserNamePassWordVisibility(builtinShortcutsVisible); // Toggle user name pass word visibility.
  } //private void loadPreference()
  
  /** 
  *  Toggle user name pass word visibility.
  */
  private void toggleUserNamePassWordVisibility(boolean isChecked)
  {
    if (isChecked) // Allow anonymous
    {
      userNamePassWordayout.setVisibility(View.INVISIBLE); // Set the visibility of user name pass word layout.
    } //isChecked
    else // Not allow anonymous
    {
      userNamePassWordayout.setVisibility(View.VISIBLE); // Set the visibility of user name pass word layout.
    }
  } // private void toggleUserNamePassWordVisibility(boolean isChecked)

  /**
  * Toggle, whether allow anonymouse.
  */
  @OnCheckedChanged(R.id.allowAnonymousetei)
  public void toggleAllowAnonymouse(boolean isChecked)
  {
    PreferenceManagerUtil.setAllowAnonymous(isChecked); //保存选项。

    builtinFtpServer.setAllowAnonymous(isChecked); // SEt whether aloow anonymous.
    
    toggleUserNamePassWordVisibility(isChecked); // Toggle user name pass word visibility.
    
  } //public void toggleBuiltinShortcuts()

  /**
  * 启动时间检查服务。
  */
  private void startTimeCheckService()
  {
    Intent serviceIntent = new Intent(this, DownloadNotificationService.class); //创建意图。
		
    startService(serviceIntent); //启动服务。
  } //private void startTimeCheckService()

  @Override
  /**
  * 活动重新处于活跃状态。
  */
  protected void onResume()
  {
    long startTimestamp=System.currentTimeMillis(); // 记录开始时间戳。
    super.onResume(); //超类继续工作。

      refreshAvailableSpace(); // 刷新可用空间数量。

      createActiveUserReportManager(); // 创建管理器，活跃用户统计。陈欣
    } //protected void onResume()

    /**
    * 创建管理器，活跃用户统计。陈欣
    */
    private void createActiveUserReportManager()
    {
      if (activeUserReportManager==null) // 还不存在管理器。
      {
        activeUserReportManager=new ActiveUserReportManager(); // 创建管理器。
            
        activeUserReportManager.startReportActiveUser(); // 开始报告活跃用户。
      } //if (activeUserReportManager==null)
    } //private void createActiveUserReportManager()
    
    /**
    * 初始化事件监听器。
    */
    private void initializeEventListener()
    {
      EventListener eventListener=new FtpEventListener(this); // 创建事件监听器。
        
      builtinFtpServer.setEventListener(eventListener); // 设置事件监听器。
    } //private void initializeEventListener()
    
    /**
    * 告知文件下载开始。
    */
    public void notifyDownloadStart()
    {
      cancelNotifyDownloadFinish(); // 取消通知。
    } // notifyDownloadStart(); // 告知文件下载开始。
    
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
    * 刷新可用空间数量。
    */
    public void refreshAvailableSpace() 
    {
      File file = new File(Constants.DirPath.FARMING_BOOK_APP_SD_CARD_PATH); //保存的图片文件。

      long usableSpaceBytes=file.getUsableSpace(); //获取可用的字节数。

      double usableSpaceMiB=((double)(usableSpaceBytes))/1024.0/1024.0; // 获取可用的 MiB 数量。
      double roundedSpaceMiB=Math.round(usableSpaceMiB*10)/10.0;
        
      availableSpaceView.setText(""+roundedSpaceMiB+"MB"); // 显示数量。
    } //private void refreshAvailableSpace()
}
