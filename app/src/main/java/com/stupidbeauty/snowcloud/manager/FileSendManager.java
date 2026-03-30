package com.stupidbeauty.snowcloud.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import android.os.ParcelFileDescriptor;
// import android.provider.MediaStore;
import android.util.Log;
// import com.stupidbeauty.codeposition.CodePosition;
import com.stupidbeauty.snowcloud.manager.FileSendManager;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import android.util.Log;
import java.util.Date;    
import java.time.format.DateTimeFormatter;
import java.io.File;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.datastore.RuntimeInformationStore;

// import com.stupidbeauty.hxlauncher.factory.MessageFactory;
// import com.stupidbeauty.hxlauncher.factory.MessageFactory;
import com.stupidbeauty.qtdocchinese.ArticleInfo;
// import org.apache.commons.io.FileUtils;
// import com.stupidbeauty.qtdocchinese.ArticleInfo;
import org.apache.commons.io.FileUtils;
// import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class FileSendManager
{
  // private InputStream reader=null; //!< File reader.
  private long fileSize=0; //!< file size.
  private String filePath; //!< file path.
  private FileInputStream content; //!< Content to send.
  private long partSize=1024; //!< 1KiB. part size.
  private long partAmount=0; //!< 计算分片个数
  // private long partCounter=0; //!< Part counter.
  private int fileId=0; //!< 获取可用的文件编号
  // private Timer timerObj = null; //!< 用于报告下载完毕的定时器。
  private static final String TAG="FileSendManager"; //!< 输出调试信息时使用的标记

  /**
  *
  */
  private int sendFileWithTimer(FileInputStream content, String filePath, long fileSize, InputStream reader)
  {
    // this.filePath=filePath;
    this.content=content;
    this.fileSize=fileSize;
    // this.reader=reader;

    partSize=1024; //1KiB

    int MaxPartSize=1024*1024*3; //3MiB


    long idealPartSize=fileSize /100; //Ideal part size.

    partSize=Math.max(partSize, idealPartSize); //Not less than least part size


    partSize=Math.min(partSize, MaxPartSize); //Not larget than max part size.

    if ((partSize*partAmount) < (fileSize)) //还有最后一片
    {
      partAmount++; //加上最后一片

    } //if ((partSize*partAmount) < (imageContent.length ())) //还有最后一片
    
    HxLauncherApplication hxlauncherApplication=HxLauncherApplication.getInstance(); //获取应用程序对象。

    return fileId;
  } // private void sendFileWithTimer(byte[] content, String filePath)
  
  /**
  * Send the file.
  */
  public int sendFile(FileInputStream content, String filePath, long fileSize, InputStream reader)
  {
    int result=sendFileWithTimer(content, filePath, fileSize, reader);
    
    return result;
  } // private void sendFile(byte[] content, String filePathWithoutSchema)
}
