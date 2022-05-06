package com.stupidbeauty.builtinftp.demo;

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

public class LauncherActivity extends Activity 
{
  private ActiveUserReportManager activeUserReportManager=null; //!< 活跃用户统计管理器。陈欣。
  private BuiltinFtpServer builtinFtpServer=null; //!< The builtin ftp server.

  @Bind(R.id.statustextView) TextView statustextView; //!< Label to show status text.
  @Bind(R.id.availableSpaceView) TextView availableSpaceView; //!< 可用空间。
    
  @OnClick(R.id.copyUrlButton)
  public void copyUrlButton()
  {
    //     陈欣

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

    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
    String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
      
    HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance() ; // 获取应用程序实例。
    builtinFtpServer=hxLauncherApplication.getBuiltinFtpServer(); // 获取FTP服务器实例对象。
    
    int actualPort=builtinFtpServer.getActualPort(); // 获取实际的端口。

    String ftpUrl="ftp://"+ ipAddress + ":"+ actualPort +"/"; // Construct the ftp server url.

    statustextView.setText(ftpUrl); // Show the FTP url
        
    initializeEventListener(); // 初始化事件监听器。
        
    startTimeCheckService(); // 启动下载通知服务。陈欣。
  } //protected void onCreate(Bundle savedInstanceState)

    /**
	 * 启动时间检查服务。
	 */
	private void startTimeCheckService()
	{
      Intent serviceIntent = new Intent(this, DownloadNotificationService.class); //创建意图。
		
//       serviceIntent.putExtra("applicationName", applicationName);

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
