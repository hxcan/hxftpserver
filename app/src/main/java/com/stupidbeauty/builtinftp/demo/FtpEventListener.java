package com.stupidbeauty.builtinftp.demo;

import com.stupidbeauty.codeposition.CodePosition;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import android.app.usage.UsageStats;
import android.content.pm.ApplicationInfo;
import com.stupidbeauty.ftpserver.lib.EventListener;
import android.util.Log;
import java.util.Date;    
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FtpEventListener implements EventListener
{
  private static final String TAG="FtpEventListener"; //!< 输出调试信息时使用的标记
  // private LauncherActivity launcherActivity=null; //!< 启动活动。
  private List<FtpEventListenerInterface> callbackList = new ArrayList<>();
    
    // 添加 registerCallback 方法
    public void registerCallback(FtpEventListenerInterface callback) {
        callbackList.add(callback);
    }

  @Override
  public void onEvent(String eventCode)
  {
  } // public void onEvent(String eventCode)
    
    @Override
    public void onEvent(String eventCode, Object eventContent) {
        Log.d(TAG, "onEvent, eventCode: " + eventCode);

        for (FtpEventListenerInterface callback : callbackList) {
            if (eventCode.equals(DELETE)) { // File deleted
                callback.refreshAvailableSpace(); // 刷新可用的空间

                if (eventContent != null) { // The event content exists
                    callback.notifyDelete(eventContent); // notify file delete
                }
            } else if (eventCode.equals(RENAME)) { // File renamed
                callback.notifyRename(eventContent); // notify file rename
            } else if (eventCode.equals(DOWNLOAD_FINISH)) { // 文件下载完毕
                callback.notifyDownloadFinish(); // 告知文件下载完毕
            } else if (eventCode.equals(UPLOAD_FINISH)) { // file upload finish
                if (eventContent != null) { // The event content exists
                    callback.notifyUploadFinish(eventContent); // notify upload finish
                }
            } else if (eventCode.equals(NEED_BROWSE_DOCUMENT_TREE)) { // need browse document tree
                callback.browseDocumentTree(eventContent); // browse document tree
            } else if (eventCode.equals(NEED_EXTERNAL_STORAGE_MANAGER_PERMISSION)) { // notify need external storage manager permission
                callback.guideExternalStorageManagerPermission(eventContent); // guide, external storage manager permission
            } else if (eventCode.equals(DOWNLOAD_START)) { // 文件下载开始
                callback.notifyDownloadStart(); // 告知文件下载开始
            } else if (eventCode.equals(IP_CHANGE)) { // ip changed
                callback.notifyIpChange(); // 告知 ip change
            }
        }
    }
    
  public FtpEventListener()
  {
  }
}

