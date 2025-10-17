package com.stupidbeauty.hxlauncher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import com.stupidbeauty.builtinftp.BuiltinFtpServer;
import butterknife.BindView;
import butterknife.ButterKnife;
import android.content.ClipboardManager;
import butterknife.OnClick;
import com.stupidbeauty.hxlauncher.activity.ApplicationInformationActivity;
import com.stupidbeauty.hxftpserver.activity.RootDirectorySettingActivity;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import com.stupidbeauty.builtinftp.demo.R;
import com.stupidbeauty.farmingbookapp.PreferenceManagerUtil;

public class SettingsActivity extends Activity
{
  private BuiltinFtpServer builtinFtpServer=null; //!< The builtin ftp server.
    private static String OptimizeRepairGooglePlayUrl = "https://play.google.com/store/apps/details?id=com.stupidbeauty.hxlauncher"; //!< 灵桌面应用程序的 Google Play 地址。
    private static final String TAG = "SettingsActivity"; //!< 调试日志标签

    @BindView(R.id.check_dolphinbug474238_placeholder)
    CheckBox checkDolphinBug474238; // 新增：用于绕过 Dolphin bug #474238 的复选框

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_activity); // 设置界面内容
        ButterKnife.bind(this); // 视图注入

        // 设置初始状态到核心库
        HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance() ; // 获取应用程序实例。
        builtinFtpServer = hxLauncherApplication.getBuiltinFtpServer(); // 获取FTP服务器实例对象。



        // 读取偏好设置中的状态并初始化复选框
        boolean isEnabled = PreferenceManagerUtil.getBooleanPreference(this, PREF_DOLPHIN_BUG_WORKAROUND, false);
        checkDolphinBug474238.setChecked(isEnabled);

        builtinFtpServer.setEnableDolphinBug474238Placeholder(isEnabled);
    }

    // 新增：当复选框状态变化时更新设置
    @OnCheckedChanged(R.id.check_dolphinbug474238_placeholder)
    public void onDolphinBugWorkaroundChecked(boolean isChecked)
    {
        builtinFtpServer.setEnableDolphinBug474238Placeholder(isChecked); // 使用抽象层
        PreferenceManagerUtil.setBooleanPreference(this, PREF_DOLPHIN_BUG_WORKAROUND, isChecked);
    }

    /**
     * 在 Google Play 上评分。
     */
    @OnClick(R.id.ratelanime_button1)
    public void rateApplicationOnGooglePlay() {
        openURL(OptimizeRepairGooglePlayUrl); // 打开网址。
    }

    private void openURL(String url) {
        // 标准化 scheme，确保小写
        if (url.startsWith("HTTP://")) {
            url = "http" + url.substring(4);
        } else if (url.startsWith("HTTPS://")) {
            url = "https" + url.substring(5);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            launchIntent(intent);
        } catch (ActivityNotFoundException ignored) {
            Log.w(TAG, "没有应用可以处理: " + intent);
        }
    }

    /**
     * 类似 {@link #launchIntent(Intent)}，但会在无法处理时弹出对话框提示用户。
     */
    private void launchIntent(Intent intent) {
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
     * 类似于 {@link #launchIntent(Intent)}，但如果无法处理则抛出异常。
     *
     * @throws ActivityNotFoundException
     */
    private void rawLaunchIntent(Intent intent) throws ActivityNotFoundException {
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            Log.d(TAG, "启动意图: " + intent + " 附加参数: " + intent.getExtras());
            startActivity(intent);
        } else {
            throw new ActivityNotFoundException("无效的意图");
        }
    }

    /**
     * 跳转到 Grant manage all files permission 活动界面。
     */
    @OnClick(R.id.lanime_button1)
    public void gotoAutoRunSettingsActivity() {
        Intent launchIntent = new Intent(this, ApplicationInformationActivity.class); // 启动意图。
        startActivity(launchIntent); // 启动活动。
    }

    /**
     * 跳转到账号信息界面。
     */
    @OnClick(R.id.myAccountbutton1)
    public void gotoAccountActivity() {
        Intent launchIntent = new Intent(this, RootDirectorySettingActivity.class); // 启动意图。
        startActivity(launchIntent); // 启动活动。
    }

    // 常量定义
    private static final String PREF_DOLPHIN_BUG_WORKAROUND = "enable_dolphinbug474238_placeholder";
}
