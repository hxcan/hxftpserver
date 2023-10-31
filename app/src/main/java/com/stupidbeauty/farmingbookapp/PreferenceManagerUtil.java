package com.stupidbeauty.farmingbookapp;

import java.lang.reflect.Type;
import java.util.Set;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
// import com.google.gson.Gson;
// import com.google.gson.reflect.TypeToken;
// import com.stupidbeauty.hxlauncher.BuildConfig;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;

/**
 * @ClassName: PreferenceManagerUtil
 * @Description: constant
 * @author Hxcan Cai
 * @date 2013-12-10
 */

public class PreferenceManagerUtil 
{
  private static final String TAG = "PreferenceManagerUtil"; //!<输出调试代码时使用的标记。

  /**
  * 获取历史数据的版本号
  * @return 历史数据的版本号。语音命中应用程序数据
  */
  public static int getVoicePackageNameMapVersion()
  {
    Context ct = HxLauncherApplication.getAppContext();
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ct);
    return sp.getInt(Constants.Common.VoicePackageMapVersion, 0);
  } //public static int getVoicePackageNameMapVersion()
  
  /**
  * 是否保存了随机端口号。
  * @return 是否保存了随机端口号。
  */
  public static boolean hasPortNumber()
  {
    Context ct = HxLauncherApplication.getAppContext();
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ct);
    return sp.contains(Constants.Common.PortNumber);
  } //public static int getVoiceShortCutMapVersion()

  /**
  * 获取保存了的随机端口号。
  * @return 获取保存了的随机端口号。
  */
  public static int getPortNumber()
  {
    Context ct = HxLauncherApplication.getAppContext();
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ct);
    return sp.getInt(Constants.Common.PortNumber, 17354);
  } //public static int getVoiceShortCutMapVersion()
  
  /**
  * 获取历史数据的版本号
  * @return 历史数据的版本号。语音命中快捷方式数据
  */
  public static int getVoiceShortCutMapVersion()
  {
    Context ct = HxLauncherApplication.getAppContext();
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ct);
    return sp.getInt(Constants.Common.VoiceShortCutMapVersion, 0);
  } //public static int getVoiceShortCutMapVersion()

	/**
	 * 设置语音命中快捷方式数据文件的版本号
	 * @param BuildConfigVERSION_CODE 版本号
	 */
	public static void setVoiceShortCutIdMapVersion(int BuildConfigVERSION_CODE)
	{
      Context ct = HxLauncherApplication.getAppContext(); //获取应用程序上下文。
      SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ct); //获取共享配置对象。
      sp.edit().putInt(Constants.Common.VoiceShortCutMapVersion, BuildConfigVERSION_CODE).apply(); //保存。
	} //public static void setVoiceShortCutIdMapVersion(int BuildConfigVERSION_CODE)

	/**
	 * 设置语音命中数据文件的版本号
	 * @param BuildConfigVERSION_CODE 版本号
	 */
	public static void setVoicePackageNameMapVersion(int BuildConfigVERSION_CODE)
	{
      Context ct = HxLauncherApplication.getAppContext(); //获取应用程序上下文。
      SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ct); //获取共享配置对象。
      sp.edit().putInt(Constants.Common.VoicePackageMapVersion, BuildConfigVERSION_CODE).apply(); //保存。
	} //public static void setVoicePackageNameMapVersion(int BuildConfigVERSION_CODE)

	/**
	 * 设置语音命中数据文件的版本号
	 * @param BuildConfigVERSION_CODE 版本号
	 */
	public static void setPortNumber(int BuildConfigVERSION_CODE)
	{
      Context ct = HxLauncherApplication.getAppContext(); //获取应用程序上下文。
      SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ct); //获取共享配置对象。
      sp.edit().putInt(Constants.Common.PortNumber, BuildConfigVERSION_CODE).apply(); //保存。
	} //public static void setVoicePackageNameMapVersion(int BuildConfigVERSION_CODE)

	/**
	 * 保存选项。是否要使用蜂窝布局
	 * @param isChecked 是否要使用蜂窝布局
	 */
	public static void setUseHiveLayout(Boolean isChecked)
	{
      Context ct = HxLauncherApplication.getAppContext(); //获取应用程序上下文。
      SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ct); //获取共享配置对象。
      sp.edit().putBoolean(Constants.Common.UseHiveLayout, isChecked).commit(); //保存。
	} //public static void setUseHiveLayout(Boolean isChecked)

	/**
	 * Save the settings. Whether to do external storage performance optimize.
	 * @param isChecked Do it or not.
	 */
	public static void setExternalStoragePerformanceOptimize(boolean isChecked)
	{
    Context ct = HxLauncherApplication.getAppContext(); //获取应用程序上下文。
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ct); //获取共享配置对象。
    sp.edit().putBoolean(Constants.Common.ExternalStoragePerformanceOptimize, isChecked).commit(); // save the settings.
	} //public static void saveHasFAQInit(Boolean hasInit)s

	/**
	 * 是否要使用蜂窝布局
	 * @return 是否要使用蜂窝布局
	 */
	public static boolean isHiveLayout()
	{
      Context ct = HxLauncherApplication.getAppContext();
      SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ct);
      return sp.getBoolean(Constants.Common.UseHiveLayout, false);
	} //public static boolean isHiveLayout()

	/**
	* Get settings, whether do external storage performance optimize.()
	*/
	public static boolean getExternalStoragePerformanceOptimize()
	{
    Context ct = HxLauncherApplication.getAppContext();
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ct);
    return sp.getBoolean(Constants.Common.ExternalStoragePerformanceOptimize, false);
	}

	/**
	 * Set whether to allow anonymous.
	 * @param hasInit Whether to allow anonymous.
	 */
	public static void setAllowAnonymous(Boolean hasInit)
	{
      Context ct = HxLauncherApplication.getAppContext(); //获取应用程序上下文。
      SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ct); //获取共享配置对象。
      sp.edit().putBoolean(Constants.Common.AllowAnonymous, hasInit).commit(); //保存。
	} //public static void saveHasFAQInit(Boolean hasInit)s

    /**
	* wHETHER to allow anonymous
	*/
	public static boolean getAllowAnonymous()
	{
      Context ct = HxLauncherApplication.getAppContext();
      SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ct);
      return sp.getBoolean(Constants.Common.AllowAnonymous, true);
	}
}
