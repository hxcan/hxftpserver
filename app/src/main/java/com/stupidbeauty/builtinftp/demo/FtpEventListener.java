package com.stupidbeauty.builtinftp.demo;

import com.stupidbeauty.ftpserver.lib.EventListener;

public class FtpEventListener implements EventListener
{
  private LauncherActivity launcherActivity=null; //!< 启动活动。

  @Override
  public void onEvent(String eventCode)
  {
    if (eventCode.equals(DELETE)) // 文件删除。
    {
      launcherActivity.refreshAvailableSpace(); // 刷新可用的空间。
    }
    else if (eventCode.equals(DOWNLOAD_FINISH)) // 文件下载完毕。
    {
      launcherActivity.notifyDownloadFinish(); // 告知文件下载完毕。
    }
    else if (eventCode.equals(DOWNLOAD_START)) // 文件下载开始。
    {
      launcherActivity.notifyDownloadStart(); // 告知文件下载开始。
    }
  }
    
  public FtpEventListener(LauncherActivity launcherActivity)
  {
    this.launcherActivity=launcherActivity;
  }
}

