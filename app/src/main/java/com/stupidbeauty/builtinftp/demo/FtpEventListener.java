package com.stupidbeauty.builtinftp.demo;

import com.stupidbeauty.ftpserver.lib.EventListener;
import android.util.Log;
import java.util.Date;    
import java.time.format.DateTimeFormatter;
import java.io.File;
import com.koushikdutta.async.AsyncServerSocket;

public class FtpEventListener implements EventListener
{
  private static final String TAG="FtpEventListener"; //!< 输出调试信息时使用的标记
  private LauncherActivity launcherActivity=null; //!< 启动活动。

  @Override
  public void onEvent(String eventCode)
  {
  } // public void onEvent(String eventCode)
    
  @Override
  public void onEvent(String eventCode, Object eventContent)
  {
    Log.d(TAG, "onEvent, eventCode: " + eventCode);
    
    if (eventCode.equals(DELETE)) // 文件删除。
    {
      launcherActivity.refreshAvailableSpace(); // 刷新可用的空间。
    }
    else if (eventCode.equals(DOWNLOAD_FINISH)) // 文件下载完毕。
    {
      launcherActivity.notifyDownloadFinish(); // 告知文件下载完毕。
    }
    else if (eventCode.equals(UPLOAD_FINISH)) // file upload finish
    {
      launcherActivity.notifyUploadFinish(eventContent); // notify upload finish.
    } // else if (eventCode.equals(UP_FINISH)) // file upload finish
    else if (eventCode.equals(DOWNLOAD_START)) // 文件下载开始。
    {
      launcherActivity.notifyDownloadStart(); // 告知文件下载开始。
    }
    else if (eventCode.equals(IP_CHANGE)) // ip changed.
    {
      launcherActivity.notifyIpChange(); // 告知 ip change.
    }
  } // public void onEvent(String eventCode)
    
  public FtpEventListener(LauncherActivity launcherActivity)
  {
    this.launcherActivity=launcherActivity;
  }
}

