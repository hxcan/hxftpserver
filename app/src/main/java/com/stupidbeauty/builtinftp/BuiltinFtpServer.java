package com.stupidbeauty.builtinftp;

import com.stupidbeauty.builtinftp.demo.FtpEventListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import com.stupidbeauty.farmingbookapp.PreferenceManagerUtil;
import android.content.Context;
import android.os.AsyncTask;
import com.stupidbeauty.ftpserver.lib.FtpServer;
import com.stupidbeauty.ftpserver.lib.UserManager;
import java.net.BindException;
import android.os.Environment;
import android.os.LocaleList;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import java.util.Date;    
import java.time.format.DateTimeFormatter;
import java.io.File;
import com.stupidbeauty.ftpserver.lib.EventListener;

public class BuiltinFtpServer
{
  private boolean allowAnonymous=true; //!< Whether to allow anonymous.
  private static final String TAG="BuiltinFtpServer"; //!< 输出调试信息时使用的标记
  private ErrorListener errorListener=null; //!< Error listener.
  private FtpEventListener eventListener = null; //!< Event listener.
  private FtpServerErrorListener ftpServerErrorListener=null; //!< The ftp server error listner. Chen xin.
  private int port=1421; //!< Port.
  private String ip=null; //!< ip.
  private FtpServer ftpServer=null; //!< Ftp server object.
  private boolean allowActiveMode=true; //!<  Whether to allow active mode.
  private boolean externalStoragePerformanceOptimize = true; //!< If we should enable external storage performance optimization.
  private boolean fileNameTolerant = true; //!< if we should tolerate invalid file name.

  /**
    * 设置是否启用 Dolphin bug #474238 的绕过选项。
    */
  public void setEnableDolphinBug474238Placeholder(boolean enable)
  {
    FtpServer.setEnableDolphinBug474238Placeholder(enable);
  }

  /**
    * 获取当前是否启用了 Dolphin bug #474238 的绕过选项。
    */
  public boolean isEnableDolphinBug474238Placeholder()
  {
    return FtpServer.isDolphinBug474238PlaceholderEnabled();
  }
    
  public FtpEventListener getEventListener() 
  {
    return eventListener;
  }

  public void setEventListener(FtpEventListener eventListener)
  {
    this.eventListener=eventListener;
        
    ftpServer.setEventListener(eventListener);
  } //public void setEventListener(EventListener eventListener)
    
  public void setErrorListener(ErrorListener errorListener)    
  {
    this.errorListener = errorListener;
  } //public void setErrorListener(ErrorListener errorListener)    
    
  public void onError(Integer errorCode) 
  {
    if (errorListener!=null)
    {
      errorListener.onError(errorCode); // Report error.
    }
    else // Not listener
    {
      //             throw new BindException();
      Exception ex = new BindException();
      throw new RuntimeException(ex);
    }
  } //public void onError(Integer errorCode)
    
  /**
  * Set to allow or not allow active mode.
  */
  public void setAllowActiveMode(boolean allowActiveMode)
  {
    this.allowActiveMode=allowActiveMode;
  } //private void setAllowActiveMode(allowActiveMode)
  
  /**
  * Set whether to allow anonymous.
  */
  public void setAllowAnonymous(boolean allowAnonymous)
  {
    this.allowAnonymous=allowAnonymous;
    
    assessSetUserManager(); // Set user mnager.
  } // public void setAllowAnonymous(bool allowAnonymous)
    
  /**
  * Query the actual port.
  */
  public int getActualPort()
  {
    return port;
  }
    
  public void setPort(int port)
  {
    this.port=port;
  } //public void setPort(int port)

  public String getIp()
  {
    String result=ip;
    
    if (ftpServer!=null)
    {
      result=ftpServer.getIp();
    } // if (ftpserver!=null)
    
    return result;
  }
        
  public void setIp(String ip)
  {
    this.ip=ip;
  } //public void setPort(int port)

  private BuiltinFtpServer()
  {
  }

  public BuiltinFtpServer(Context context) 
  {
    this.context = context;
  }

  private Context context; //!< Context.

  /**
  * Stop the ftp server.
  */
  public void stop()
  {
    ftpServer.stop();
    
    ftpServer = null;
    ftpServerErrorListener = null;
  } // public void stop()
  
  public void start()
  {
    ftpServerErrorListener = new FtpServerErrorListener(this);
    
    ftpServer = new FtpServer("0.0.0.0", port, context, allowActiveMode, ftpServerErrorListener, ip);

    ftpServer.setRootDirectory(Environment.getExternalStorageDirectory()); // Set the root directory.
    ftpServer.setAutoDetectIp(true); // Auto detect ip.

    ftpServer.setExternalStoragePerformanceOptimize(externalStoragePerformanceOptimize);
    ftpServer.setFileNameTolerant(fileNameTolerant);

    
    assessSetUserManager(); // Assess set user manager.
  }
  
  /**
  *  Get the uri of specified virtual path.
  */
  public Uri getVirtualPath(String path)
  {
    return ftpServer.getVirtualPath(path);
  } // public Uri getVirtualPath(String path)

  /**
  *  un Mount virtual path.
  */
  public void unmountVirtualPath(String path)
  {
    ftpServer.unmountVirtualPath(path);
  } // public void unmountVirtualPath(String path)
  
  /**
  * Mount virtual path.
  */
  public void mountVirtualPath(String path , Uri uri)
  {
    boolean takePermission = true; // Take permsion by default.

    mountVirtualPath(path, uri, takePermission);
  } // public void mountVirtualPath(String path , Uri uri)
  
  /**
  * Mount virtual path.
  */
  public void mountVirtualPath(String path , Uri uri, boolean takePermission)
  {
    ftpServer.mountVirtualPath(path, uri, takePermission);
  } // public void mountVirtualPath(String path , Uri uri)
  
  /**
  * File name tolerant. For example: /Android/data/com.client.xrxs.com.xrxsapp/files/XrxsSignRecordLog/Zw40VlOyfctCQCiKL_63sg==, with a trailing <LF> (%0A).
  */
  public void setFileNameTolerant(boolean toleranttrue)
  {
    if (ftpServer!=null) // The ftp server object exists
    {
      fileNameTolerant = toleranttrue; // Remember it.
      ftpServer.setFileNameTolerant(toleranttrue);
    } // if (ftpServer!=null) // The ftp server object exists
  } // public void setFileNameTolerant(boolean toleranttrue)
  
  /**
  * Set option. Whether to do external storage perforamnce optimize.
  */
  public void setExternalStoragePerformanceOptimize(boolean isChecked)
  {
    if (ftpServer!=null) // The ftp server object exists
    {
      externalStoragePerformanceOptimize = isChecked; // Remember it.
      ftpServer.setExternalStoragePerformanceOptimize(isChecked);
    } // if (ftpServer!=null) // The ftp server object exists
  } // public void setExternalStoragePerformanceOptimize(boolean isChecked)
  
  /**
  * Answ4er the browse docuembnt tree reqeust.
  */
  public void answerBrowseDocumentTreeReqeust(int requestCode, Uri uri) 
  {
    ftpServer.answerBrowseDocumentTreeReqeust(requestCode, uri);
  } // public void answerBrowseDocumentTreeReqeust(int requestCode, Uri uri)
  
  /**
  * Assess set user manager.
  */
  private void assessSetUserManager()
  {
    UserManager userManager=null; // Create user manager.

    if (!allowAnonymous) // Not allow anonymous
    {
      userManager=new UserManager(); // Create user manager.
      
      userManager.addUser("stupidbeauty", "ftpserver");
    } // if (!allowAnonymous) // Not allow anonymous
    
    if (ftpServer!=null)
    {
      ftpServer.setUserManager(userManager); // Set user manager.
    } // if (ftpServer!=null)
  } // private void assessSetUserManager()
}
