package com.stupidbeauty.builtinftp.demo;

import com.stupidbeauty.feedback.Feedback;
import androidx.documentfile.provider.DocumentFile;
import java.io.File;
import com.stupidbeauty.hxlauncher.activity.ApplicationInformationActivity;
import com.stupidbeauty.hxlauncher.SettingsActivity;
// import com.google.gson.Gson;
import com.stupidbeauty.codeposition.CodePosition;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.BufferedReader;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import com.stupidbeauty.farmingbookapp.PreferenceManagerUtil;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.ftpserver.lib.DocumentTreeBrowseRequest;
import android.widget.Button;
import butterknife.ButterKnife;
import android.os.Debug;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import android.util.Log;
import java.util.Date;    
import java.time.format.DateTimeFormatter;
import java.io.File;
// import com.koushikdutta.async.AsyncServerSocket;
import com.stupidbeauty.voiceui.VoiceUi;
import com.stupidbeauty.hxlauncher.service.DownloadNotificationService; 
import com.stupidbeauty.ftpserver.lib.EventListener;
import com.stupidbeauty.ftpserver.lib.RenameInformationObject;
import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import java.util.Random;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;
import com.stupidbeauty.builtinftp.BuiltinFtpServer;
import butterknife.BindView;
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
import android.widget.ImageView;
import com.stupidbeauty.farmingbookapp.PreferenceManagerUtil;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import butterknife.BindView;
import android.widget.RelativeLayout;

public class LauncherActivity extends Activity 
{
  private static final String TAG="LauncherActivity"; //!< 输出调试信息时使用的标记
  private VoiceUi voiceUi=null; //!< 语音交互对象。
  private Timer timerObj = null; //!< 用于报告下载完毕的定时器。
  private ActiveUserReportManager activeUserReportManager=null; //!< 活跃用户统计管理器。陈欣。
  private BuiltinFtpServer builtinFtpServer=null; //!< The builtin ftp server.
  @BindView(R.id.stopServerlButton) Button stopServerlButton; //!< the stop server button.
  @BindView(R.id.startServerlButton) Button startServerlButton; //!< the start server button.
  @BindView(R.id.statustextView) TextView statustextView; //!< Label to show status text.
  @BindView(R.id.maskUrlLineImagecon) ImageView maskUrlLineImagecon; //!< The mask on the url line.
  @BindView(R.id.availableSpaceView) TextView availableSpaceView; //!< 可用空间。
  @BindView(R.id.allowAnonymousetei) CheckBox allowAnonymousetei; //!< Allow anonymous check box.
  @BindView(R.id.userNamePassWordayout) RelativeLayout userNamePassWordayout; //!< User name pass word layout.
    
  @OnClick(R.id.shareIcon)
  public void shareViaText()
  {
    Intent launchIntent=new Intent(this, SettingsActivity.class); // 启动意图。

    startActivity(launchIntent); //启动活动。
  } // public void shareViaText()

  @OnClick(R.id.feedbackIcon)
  public void showFeedbackUit()
  {
    Feedback feedback = new Feedback(this, "caihuosheng@gmail.com");
    
    feedback.showFeedbackUi(); // Show feedback ui.
  } // public void shareViaText()
  
  @OnClick(R.id.stopServerlButton)
  public void stopServerlButton()
  {
    builtinFtpServer.stop(); // Stop the ftp server.
    
    stopServerlButton.setVisibility(View.INVISIBLE); // Hide the stop server button.
    startServerlButton.setVisibility(View.VISIBLE); // Show the start server button.
    maskUrlLineImagecon.setVisibility(View.VISIBLE); // Mask the url line.
    
    stopPersistantNotification(); // Stop the persistent notification.
  } // public void stopServerlButton()

  @OnClick(R.id.startServerlButton)
  public void startServerlButton()
  {
    builtinFtpServer.start(); // Start the ftp server.
    
    stopServerlButton.setVisibility(View.VISIBLE); // Hide the stop server button.
    startServerlButton.setVisibility(View.INVISIBLE); // Show the start server button.
    maskUrlLineImagecon.setVisibility(View.INVISIBLE); // Mask the url line.

    startTimeCheckService(); // Show persistent notification.
  } // public void stopServerlButton()

  @OnClick(R.id.copyUrlButton)
  public void copyUrlButton()
  {
    String stringNodeCopied= statustextView.getText().toString();

    ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData clip = android.content.ClipData.newPlainText("Copied", stringNodeCopied);

    clipboard.setPrimaryClip(clip);
    
    String downloadFinished = getResources().getString(R.string.urlCopiedged); // Load the text content.

    voiceUi.say(downloadFinished); // 发声。
  } // public void copyUrlButton()
  
  /**
  * Show ftp url
  */
  private void showFtpUrl() 
  {
    int actualPort=builtinFtpServer.getActualPort(); // 获取实际的端口。
    String actualIp=builtinFtpServer.getIp(); // Get the actual ip.

    String ftpUrl="ftp://"+ actualIp + ":"+ actualPort +"/"; // Construct the ftp server url.

    statustextView.setText(ftpUrl); // Show the FTP url
  } // private void showFtpUrl() // Show ftp url

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

    HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance() ; // 获取应用程序实例。
    builtinFtpServer = hxLauncherApplication.getBuiltinFtpServer(); // 获取FTP服务器实例对象。
    
    showFtpUrl(); // Show ftp url.
    
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
  
  /**
  * Stop the persistent notification.
  */
  private void stopPersistantNotification()
  {
    Intent serviceIntent = new Intent(this, DownloadNotificationService.class); //创建意图。
		
    stopService(serviceIntent); // Stop the service.
  } // private void stopPersistantNotification()

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
    *  告知 ip change.
    */
    public void notifyIpChange()
    {
      showFtpUrl(); // Show ftp url

      String downloadFinished = getResources().getString(R.string.ipChanged); // 读取 说明 字符串。

      Log.d(TAG, "notifyDownloadFinish, text: " + downloadFinished); // Debug.

      voiceUi.say(downloadFinished); // 发声。
    } // public void notifyIpChange()
    
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
    * browse document tree.
    */
    public void browseDocumentTree(Object eventContent)
    {
      DocumentTreeBrowseRequest requestObject=(DocumentTreeBrowseRequest)(eventContent); // Get the request object.
      Intent intent=requestObject.getIntent(); // Get the intent.
      int yourrequestcode=requestObject.getRequestCode(); // Get the request code.
      startActivityForResult(intent, yourrequestcode);
    } // public void browseDocumentTree(Object eventContent)

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) 
    {
      if (resultCode == Activity.RESULT_OK) // Select success
      {
        // The result data contains a URI for the document or directory that // the user selected.
        Uri uri = null;
        if (resultData != null) // There is result data
        {
          uri = resultData.getData();
          
          builtinFtpServer.answerBrowseDocumentTreeReqeust(requestCode, uri); // Answ4er the browse docuembnt tree reqeust.
        } // if (resultData != null) // There is result data
      } // if (resultCode == Activity.RESULT_OK) // Select success
    } // public void onActivityResult(int requestCode, int resultCode, Intent resultData) 
    
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
    
    /**
    * notify file delete.
    */
    public void notifyDelete(Object eventContent)
    {
      scanDocumentFile(eventContent);
    } // public void notifyDelete(Object eventContent)
    
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
    * Notify upload finish.
    */
    public void notifyUploadFinish(Object eventContent)
    {
      scanDocumentFile(eventContent);
    } // notifyDownloadFinish(); // 告知文件下载完毕。
    
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
    
    /**
    * 刷新可用空间数量。
    */
    public void refreshAvailableSpace() 
    {
      // File file = new File(Constants.DirPath.FARMING_BOOK_APP_SD_CARD_PATH); //保存的图片文件。
      File file = getFilesDir(); // The files dir.

      long usableSpaceBytes=file.getUsableSpace(); //获取可用的字节数。

      double usableSpaceMiB=((double)(usableSpaceBytes))/1024.0/1024.0; // 获取可用的 MiB 数量。
      double roundedSpaceMiB=Math.round(usableSpaceMiB*10)/10.0;
        
      availableSpaceView.setText(""+roundedSpaceMiB+"MB"); // 显示数量。
    } //private void refreshAvailableSpace()
}
