package com.stupidbeauty.hxlauncher;

import android.os.Environment;
import java.io.File;

/**
 * 一些常量的定义。
 * @author root 蔡火胜。
 *
 */
public class Constants 
{
  public static class Timing
  {
    public static final int VibrateDuration = 12; //!<振动时长，毫秒。
    public static final long PictureFailDuration = 20 * 1000; //!<20秒钟，认为拍照失败。
    public static final long LongTimeNoShotDuration = 5 * 60 * 1000; //!<5分钟，认为长时间未拍照，退出。
  }

  /**
  * 与尺寸相关的常量。
  */
  public static class Size
  {
    public static final long UsableSpaceBytesThreshold = 412*1024*1024; //!<剩余可用的空间字节数阈值。不足这个数时，停止拍照。
    public static final int ReportSendPictureAmountInterval=80; //!<每当拍摄这么多照片就要报告一次。
  }

  /**
  * 目录路径。
  * @author root 蔡火胜。
  *
  */
  public static class DirPath
  {
    public static final String FARMING_BOOK_APP_SD_CARD_PATH = Environment.getExternalStorageDirectory().getPath(); //!< 检查存储空间的路径。
  } //public static class DirPath
} //public class Constants
