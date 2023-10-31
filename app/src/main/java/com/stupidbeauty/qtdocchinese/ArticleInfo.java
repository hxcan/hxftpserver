package com.stupidbeauty.qtdocchinese;

import android.content.Intent;

/**
 * @author root Hxcan <caihuosheng@gmail.com>
 *
 */
public class ArticleInfo
{
  private long fileId=0; //!< File id.
  private String filePath=null; // !< The file path.
  
  public void setFileId(long fileId)
  {
    this.fileId=fileId;
  } // public void setFileId(int fileId)
  
  public void setFilePath(String filePath)
  {
    this.filePath=filePath;
  }
  
  public String getFilePath()
  {
    return filePath;
  }
  
  public void setFunctionName(String functionName) 
  {
    this.functionName = functionName;
  }

  public long getFileLength() 
  {
    return fileLength;
  }

  public String getFunctionName() 
  {
    return functionName;
  }

  private String functionName="TextMessage"; //!<Function name
  public int getReceivedLength() 
  {
    return receivedLength;
  }

  public void setReceivedLength(int receivedLength) 
  {
    this.receivedLength = receivedLength;
  }

  private int receivedLength=0; //!<File received length
  private long fileLength=0; //!< File whole length
  
  public boolean isAutoRun() 
  {
    return autoRun;
  }

  public void setFileLength(long fileLength) 
  {
    this.fileLength = fileLength;
  }

  public void setAutoRun(boolean autoRun) 
  {
    this.autoRun = autoRun;
  }

  public String getActivityName() 
  {
    return activityName;
  }

  public void setActivityName(String activityName) 
  {
    this.activityName = activityName;
  }

  private boolean autoRun=false; //!<是否自动启动。

  private String packageName; //!<应用程序包名。
  private String activityName; //!<活动名字。

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  private Intent launchIntent; //!<启动意图。

  public Intent getLaunchIntent() {
    return launchIntent;
  }

  public void setLaunchIntent(Intent launchIntent) {
    this.launchIntent = launchIntent;
  }

  private CharSequence applicationLabel; //!<应用程序名字标签。

  public CharSequence getApplicationLabel() {
    return applicationLabel;
  }

  public void setApplicationLabel(CharSequence applicationLabel) {
    this.applicationLabel = applicationLabel;
  }

}
