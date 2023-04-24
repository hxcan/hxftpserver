package com.stupidbeauty.hxlauncher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.stupidbeauty.hxlauncher.activity.ApplicationInformationActivity;
import android.util.Log;
import android.widget.CheckBox;
import com.stupidbeauty.farmingbookapp.PreferenceManagerUtil;
import com.stupidbeauty.hxftpserver.activity.RootDirectorySettingActivity;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import com.stupidbeauty.builtinftp.demo.R;

public class SettingsActivity extends Activity
{
  private static String OptimizeRepairGooglePlayUrl="https://play.google.com/store/apps/details?id=com.stupidbeauty.hxlauncher"; //!<灵桌面应用程序的google play地址。

  private static final String TAG="SettingsActivity"; //!<输出调试信息时使用的标记。

  @Override
  /**
    * 活动被创建。
    */
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.settings_activity); //设置界面内容。

    ButterKnife.bind(this); //视图注入。
  } //protected void onCreate(Bundle savedInstanceState)

    @OnClick(R.id.ratelanime_button1)
    /**
     * 在GooglePlay上评分。
     */
    public void rateApplicationOnGooglePlay() 
    {
      openURL(OptimizeRepairGooglePlayUrl); //打开网址。
    } //public void rateApplicationOnGooglePlay()

    private void openURL(String url)
    {
      // Strangely, some Android browsers don't seem to register to handle HTTP:// or HTTPS://.
      // Lower-case these as it should always be OK to lower-case these schemes.
      if (url.startsWith("HTTP://")) 
      {
        url = "http" + url.substring(4);
      }
      else if (url.startsWith("HTTPS://")) 
      {
        url = "https" + url.substring(5);
      }
      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
      try 
      {
        launchIntent(intent);
      }
      catch (ActivityNotFoundException ignored) 
      {
        Log.w(TAG, "Nothing available to handle " + intent);
      }
    }

    /**
     * Like {@link #rawLaunchIntent(Intent)} but will show a user dialog if nothing is available to handle.
     */
    private void launchIntent(Intent intent)
    {
        try {
            rawLaunchIntent(intent);
        } catch (ActivityNotFoundException ignored) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name);
            builder.setMessage(R.string.msg_intent_failed);
            builder.setPositiveButton(R.string.button_ok, null);
            builder.show();
        }
    }

    /**
     * Like {@link #launchIntent(Intent)} but will tell you if it is not handle-able
     * via {@link ActivityNotFoundException}.
     *
     * @throws ActivityNotFoundException
     */
    private void rawLaunchIntent(Intent intent)
    {
      if (intent != null) 
      {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        Log.d(TAG, "Launching intent: " + intent + " with extras: " + intent.getExtras());
        startActivity(intent);
      }
    }

    /**
     * 跳转到 Grant manage all files permission 活动界面。
     */
    @OnClick(R.id.lanime_button1)
    public void gotoAutoRunSettingsActivity()
    {
      Intent launchIntent=new Intent(this, ApplicationInformationActivity.class); // 启动意图。
      startActivity(launchIntent); //启动活动。
    }

    /**
     * 跳转到账号信息界面。
     */
    @OnClick(R.id.myAccountbutton1)
    public void gotoAccountActivity()
    {
      Intent launchIntent=new Intent(this, RootDirectorySettingActivity.class); // The intent to launch.
      startActivity(launchIntent); //启动活动。
    } //public void gotoAccountActivity()
}




