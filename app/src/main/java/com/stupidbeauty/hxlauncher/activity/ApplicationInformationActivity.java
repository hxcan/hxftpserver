package com.stupidbeauty.hxlauncher.activity;

import android.provider.Settings;
import android.content.Intent;
import android.os.Environment;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.LocaleList;
import android.os.Vibrator;
import android.provider.Settings;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

// import com.bumptech.glide.Glide;
// import com.stupidbeauty.grebe.DownloadRequestor;
// import com.stupidbeauty.hxlauncher.ApplicationInformationAdapter;
// import com.stupidbeauty.hxlauncher.BuiltinShortcutsManager;
import com.stupidbeauty.hxlauncher.Constants;
// import com.stupidbeauty.hxlauncher.InstalledPackageLoadTaskInterface;
//import com.stupidbeauty.hxlauncher.PackageCountObject;
import com.stupidbeauty.builtinftp.demo.R;
// import com.stupidbeauty.hxlauncher.VoicePackageMapItemMessageProtos;
// import com.stupidbeauty.hxlauncher.VoicePackageMapMessageProtos;
// import com.stupidbeauty.hxlauncher.bean.VoiceCommandHitDataObject;
// import com.android.volley.RequestQueue;
// import com.google.gson.Gson;
// import com.google.protobuf.ByteString;
// import com.huiti.msclearnfootball.AnswerAvailableEvent;
// import com.huiti.msclearnfootball.VoiceRecognizeResult;
import com.stupidbeauty.farmingbookapp.PreferenceManagerUtil;
// import com.stupidbeauty.hxlauncher.asynctask.TranslateRequestSendTask;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static android.content.Intent.ACTION_PACKAGE_CHANGED;
import static android.content.Intent.ACTION_PACKAGE_REPLACED;
import static android.content.Intent.EXTRA_COMPONENT_NAME;
import static android.content.Intent.EXTRA_PACKAGE_NAME;
import static android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_DYNAMIC;
import static android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_MANIFEST;

public class ApplicationInformationActivity extends Activity
{
  // View name of the header title. Used for activity scene transitions
  public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";

  private     AnimationDrawable rocketAnimation; //!<录音按钮变暗

//   private Stack<VoiceCommandHitDataObject> voiceCommandHitDataStack=new Stack<>(); //!<语音命中数据记录栈

//   private BuiltinShortcutsManager builtinShortcutsManager=new BuiltinShortcutsManager(); //!<内置快捷方式管理器

  @BindView(R.id.hitApplicationIcon) ImageView hitApplicationIcon; //!<命中的应用的图标。
  @BindView(R.id.microphoneIcon) ImageView microphoneIcon; //!< 升级按钮图标。
  @BindView(R.id.applicationName2) TextView applicationName2; //!< Application name text view.
  @BindView(R.id.launcher_activity) RelativeLayout launcher_activity; //!<整个启动活动

  private HashMap<String, Long> packageItemLastLaunchTimestampMap=new HashMap<>(); //!<包名加类名的字符串与最后一次启动时间戳之间的映射。

  private HashMap<String, String> serverVoiceCommandResponseIgnoreMap=new HashMap<>(); //!<服务器的回复中，要忽略掉的关系映射

    private HashMap<String, ShortcutInfo> shortcutTitleInfoMap; //!<快捷方式的标题与快捷方式对象本身的映射。
    private HashMap<String, ShortcutInfo> shortcutIdInfoMap; //!<快捷方式的编号与快捷方式对象本身的映射
    private HashMap<String, Integer> packageNameItemNamePositionMap=new HashMap<>(); //!<包名加类名的字符串与图标位置之间的映射。
    private HashMap<String, Integer> packageNamePositionMap=new HashMap<>(); //!<包名字符串与图标位置之间的映射。

    private boolean sentVoiceShortcutAssociationData=false; //!<是否已经成功发送语音指令关联快捷方式的数据。
    private static final int PERMISSIONS_REQUEST = 1; //!<权限请求标识

    private static final String PERMISSION_FINE_LOCATIN = Manifest.permission.ACCESS_FINE_LOCATION; //!<位置权限

    @BindView(R.id.wallpaper) ImageView wallpaper; //!<墙纸视图。

    @BindView(R.id.progressBar) ProgressBar progressBar; //!<进度条。

    private String voiceRecognizeResultString; //!<语音识别结果。

    int ret = 0;

    @BindView(R.id.applicationIconrightimageView2) ImageView applicationIconrightimageView2; //!< 应用图标图片。陈欣。

    @BindView(R.id.statustextView) TextView statustextView; //!< 用来显示状态的文字标签。

    private int recognizeCounter=0; //!<识别计数器．

    private Vibrator vibrator;

    private boolean voiceEndDetected=false; //!<是否已经探测到用户声音结束。

    private int mPageNumber = 1;//{1, 1, 1};

    private final int MSG_LOAD_MORE = 2;

    private ImageView mHeaderImageView;
    private TextView mHeaderTitle;

    private String packagename; //!< 包名。陈欣。
    private String activityName; //!< 活动名字。

    private int mCurrMsg = -1;

    @OnClick(R.id.loveAnimation)
    public void deleteItem()
    {
        Uri uri = Uri.parse("package:" + packagename);
        Intent uIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, uri);
        startActivity(uIntent);
    }

    /**
     * 手动开始语音识别。
     */
    @OnClick(R.id.hitApplicationIcon)
    public void showInformationUrl()
    {
      HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。
    } //public void manualStartVoiceRecognize()

    /**
     * 手动开始语音识别。
     */
    @OnClick({R.id.voiceAssistantLayout, R.id.microphoneIcon})
    public void requestUpgradePackage()
    {
        Log.d(TAG, "manualStartVoiceRecognize."); //Debug.

        {
          HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。
        } //else //命中了。
    } //public void manualStartVoiceRecognize()

    /**
     * 报告结果，翻译请求的发送结果。
     * @param result 是否发送成功。
     */
    public void processApplicationInfoLoadResult(List<PackageInfo> result)
    {
//        showInstalledPackages(result);
    } //public void processApplicationInfoLoadResult(Boolean result)


//     private ApplicationInformationAdapter mAdapter; //!<适配器。

    private static final String TAG="ApplicationInformationB"; //!<输出调试信息时使用的标记。
    private final String categoryName="default"; //!<要显示的分类的名字。

    @Override
    /**
     * 活动被创建。
     */
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.application_information_activity); // 设置视图

        ButterKnife.bind(this); //视图注入。

        mHeaderImageView = findViewById(R.id.applicationIconrightimageView2);
        mHeaderTitle = findViewById(R.id.rightTextoperationMethodactTitletextView2);

        // BEGIN_INCLUDE(detail_set_view_name)
        /*
         * Set the name of the view's which will be transition to, using the static values above.
         * This could be done in the layout XML, but exposing it via static variables allows easy
         * querying from other Activities
         */
        mHeaderTitle.setTransitionName( VIEW_NAME_HEADER_TITLE);

        Intent intent=getIntent(); // 获取意图。

    } //protected void onCreate(Bundle savedInstanceState)

    /**
     * 隐藏升级按钮。
     */
    private void hideUpgradeIcon()
    {
        microphoneIcon.setVisibility(View.INVISIBLE); // 隐藏。
    } //private void hideUpgradeIcon()

    /**
     * 显示升级按钮
     */
    private void showUpgradeIcon()
    {
      microphoneIcon.setVisibility(View.VISIBLE); // 显示。陈欣。
    } //private void showUpgradeIcon()

    private void resetPreferredLauncherAndOpenChooser()
    {
      PackageManager packageManager = getPackageManager();
      ComponentName componentName = new ComponentName(getApplicationContext(), com.stupidbeauty.hxlauncher.activity.FakeLauncherActivity.class);
      packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

      Intent selector = new Intent(Intent.ACTION_MAIN);
      selector.addCategory(Intent.CATEGORY_HOME);
      selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(selector);

      packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
    }

    /**
     * 切换是否应用锁。
     */
    @OnClick(R.id.lock0)
    public void toggleApplicationLock()
    {
      HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance(); //获取应用程序对象。
      Log.d(TAG, "gotoFileManagerSettingsPage"); //Debug.

      Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);  // 跳转语言和输入设备

      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

      String packageNmae=getPackageName();
      Log.d(TAG, "gotoFileManagerSettingsPage, package name: " + packageNmae); //Debug.

      String url = "package:"+packageNmae;

      Log.d(TAG, "gotoFileManagerSettingsPage, url: " + url); //Debug.

      intent.setData(Uri.parse(url));

      startActivity(intent);
    } //public void toggleBuiltinShortcuts()
}
