package com.stupidbeauty.hxlauncher.service;

import com.stupidbeauty.builtinftp.demo.LauncherActivity;
import com.stupidbeauty.builtinftp.demo.R;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.NotificationChannel;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.stupidbeauty.builtinftp.BuiltinFtpServer;
// import butterknife.Bind;
import butterknife.ButterKnife;
import android.content.ClipboardManager;
import butterknife.OnClick;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;

public class DownloadNotificationService extends Service
{
  private Notification continiusNotification=null; //!<记录的通知
  private BuiltinFtpServer builtinFtpServer=null; //!< The builtin ftp server.

  private int NOTIFICATION = 163731;

  private NotificationManager mNM;

  private long lastPublishTimestamp=0; //!<记录的上次发布局域网服务的时间戳。
  private String callbackIp="127.0.0.1"; //!<回调的IP。

  private static final String TAG = "DownloadNotificationService"; //!< 输出调试信息时使用的标记。
  private static final String LanServiceName = "com.stupidbeauty.shutdownat2100.android"; //!<局域网服务名字。
  private static final int LanServicePort = 9521; //!<局域网服务的端口号。

  @Override
  public IBinder onBind(Intent intent) 
  {
    return null;
  }
	
  @Override
  /**
  * 服务被启动。也可能是重新启动。
  */
  public int onStartCommand(Intent intent, int flags, int startId) 
  {
    Log.d(TAG,"onStartCommand,180"); //Debug.

    String contentText = getString(R.string.app_name);

    showNotification(contentText);

    startForeground(NOTIFICATION, continiusNotification); //显示在前台
    
    HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance() ; // 获取应用程序实例。
    builtinFtpServer=hxLauncherApplication.getBuiltinFtpServer(); // 获取FTP服务器实例对象。
		
    return START_STICKY; //被杀死时，自动重启。
  } //public int onStartCommand(Intent intent, int flags, int startId)

  private void showNotification(String contentText)
  {
    // In this sample, we'll use the same text for the ticker and the expanded notification
    CharSequence text = getText(R.string.app_name);

    // The PendingIntent to launch our activity if the user selects this notification
    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, LauncherActivity.class), 0);
		
    String downloadingText="Running " + contentText; // 构造字符串，正在下载。陈欣。
		
    NotificationChannel chan = new NotificationChannel( "#include", "My Foreground Service", NotificationManager.IMPORTANCE_LOW);
            
    mNM.createNotificationChannel(chan);

    // Set the info for the views that show in the notification panel.
    Notification notification = new Notification.Builder(this)
      .setSmallIcon(R.drawable.ic_launcher)  // the status icon
      .setTicker(text)  // the status text
      .setWhen(System.currentTimeMillis())  // the time stamp
      .setContentTitle(getText(R.string.app_name))  // the label of the entry
      .setContentText(downloadingText)  // the contents of the entry
      .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
      .setPriority(Notification.PRIORITY_HIGH)   // heads-up
      .setChannelId("#include")
      .build();

    continiusNotification=notification; //记录通知

    // Send the notification.
    mNM.notify(NOTIFICATION, notification);
  }

  /**
  * Main initialization of the input method component.  Be sure to call
  * to super class.
  */
  @Override
  public void onCreate()
  {
    super.onCreate(); //创建超类。

		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        String contentText = getString(R.string.app_name);

// 		showNotification(contentText);

		startHttpServer(); //启动HTTP服务器
	} //public void onCreate()

	/**
	 * 启动HTTP服务器，用于对同一个局域网内其它平板的请求进行响应.
	 **/
	private void startHttpServer()
	{
      AsyncHttpServer server=new AsyncHttpServer(); //Create the async server.

      HttpServerRequestCallback callback=new HttpServerRequestCallback()
      {
        @Override
        public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response)
        {
          response.send("Hello!!!"); //是啊，今天休息。等下出去晒太阳。
        } //public void onRequest(AsyncHttpServerRequest request,AsyncHttpServerResponse response)
      };
      server.get("/", callback); //设置路径对应的回调对象.
	} //private void startHttpServer()
}
