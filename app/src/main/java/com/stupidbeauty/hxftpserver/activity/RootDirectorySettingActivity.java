package com.stupidbeauty.hxftpserver.activity;

import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import com.stupidbeauty.builtinftp.BuiltinFtpServer;
import butterknife.Bind;
import butterknife.ButterKnife;
import android.content.ClipboardManager;
import butterknife.OnClick;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.ftpserver.lib.DocumentTreeBrowseRequest;
import butterknife.Bind;
import butterknife.ButterKnife;
import android.os.Debug;
import com.stupidbeauty.codeposition.CodePosition;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.BufferedReader;
import android.media.MediaScannerConnection;
import android.content.Context;
import java.util.Date;    
import java.time.format.DateTimeFormatter;
import java.io.File;
import android.os.storage.StorageManager;
import java.io.File;
import com.stupidbeauty.builtinftp.demo.Constants;
import android.net.Uri;
import android.provider.Settings;
import android.content.Intent;
import android.os.Environment;
import 	android.provider.DocumentsContract;
import java.util.Locale;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;
import com.stupidbeauty.builtinftp.demo.R;
import android.widget.TextView;
import android.widget.CheckBox;
import com.stupidbeauty.farmingbookapp.PreferenceManagerUtil;
// import com.stupidbeauty.hxlauncher.activity.AccountActivity;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import butterknife.Bind;

public class RootDirectorySettingActivity extends Activity
{
	private static final String TAG = "RootDirectorySettingActivity"; //!< Tag used in debug code.
	private static final int TIMEOUT = 30000;

	@Bind(R.id.paidCreditPrompttextView6) TextView paidCreditPrompttextView6; //!< Root directory path.
  @Bind(R.id.paidCredittextView7) CheckBox paidCredittextView7; //!< Whether to do external storage performance optimize.

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent resultData) 
  {
    Log.d(TAG, CodePosition.newInstance().toString()+  ", request code: " + requestCode + ", result code: " + resultCode + ", uri to use: " + resultData); // Debug.
    //       if (requestCode == your-request-code && resultCode == Activity.RESULT_OK) 
    if (resultCode == Activity.RESULT_OK)  // Success. -1
    {
      Uri uri = null;
      if (resultData != null) // There is result data
      {
        uri = resultData.getData();
        Log.d(TAG, CodePosition.newInstance().toString()+  ", request code: " + requestCode + ", result code: " + resultCode + ", uri to use: " + uri.toString()); // Debug.
        
        HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance() ; // 获取应用程序实例。
        BuiltinFtpServer builtinFtpServer=null; //!< The builtin ftp server.
        builtinFtpServer=hxLauncherApplication.getBuiltinFtpServer(); // 获取FTP服务器实例对象。
        builtinFtpServer.mountVirtualPath("/" , uri); // Mount virtual path.
        
        queryRootDirectory(); // Show root directory.
      } // if (resultData != null) // There is result data
    } // if (resultCode == Activity.RESULT_OK)  // Success. -1
  }

  /**
    * 切换是否要使用蜂窝布局
    * @param isChecked 是否被选中
    */
  @OnCheckedChanged(R.id.paidCredittextView7)
  public void toggleUseHiveLayout(boolean isChecked)
  {
    PreferenceManagerUtil.setExternalStoragePerformanceOptimize(isChecked); // Save the settings.

    HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance() ; // 获取应用程序实例。
    BuiltinFtpServer builtinFtpServer=null; //!< The builtin ftp server.
    builtinFtpServer=hxLauncherApplication.getBuiltinFtpServer(); // 获取FTP服务器实例对象。
    builtinFtpServer.setExternalStoragePerformanceOptimize(isChecked); // Set option.
  } //public void toggleUseHiveLayout(boolean isChecked)
  
  @OnClick(R.id.resetRootDirectoryss)
  public void resetRootDirectoryss()
  {
    HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance() ; // 获取应用程序实例。
    BuiltinFtpServer builtinFtpServer=null; //!< The builtin ftp server.
    builtinFtpServer=hxLauncherApplication.getBuiltinFtpServer(); // 获取FTP服务器实例对象。
    builtinFtpServer.unmountVirtualPath("/"); // un Mount virtual path.
    
    queryRootDirectory(); // Show root directory.
  } // public void resetRootDirectoryss()

	/**
	 * 跳转到登录界面。
	 */
	@OnClick(R.id.loginbutton)
	public void chooseRootDirectory()
	{
    StorageManager sm = (StorageManager) getSystemService(Context.STORAGE_SERVICE);

    Intent intent = sm.getPrimaryStorageVolume().createOpenDocumentTreeIntent();

    File rootDirectory=Environment.getExternalStorageDirectory();
    
    // Uri uri = Uri.parse("content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata");            
    Uri uriToLoad=Uri.fromFile(rootDirectory); // Create Uri.

    int yourrequestcode=Constants.RequestCode.RootDirectoryPermissionRequestCode; // The request code
    
    startActivityForResult(intent, yourrequestcode);
	} //public void gotoLoginActivity()

	@Override
	/**
	 * 活动被创建。
	 */
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState); //创建超类。
		
		setContentView(R.layout.root_directory_setting_activity); //设置显示内容。

		ButterKnife.bind(this); //依赖注入。

		queryRootDirectory(); // Query and show root directory.
    
    showExternalStoragePerformanceOptimize(); // Show the option of external storage performance optimize.
	} //protected void onCreate(Bundle savedInstanceState)
  
  /**
  *  Show the option of external storage performance optimize.
  */
  private void showExternalStoragePerformanceOptimize()
  {
    boolean externalStoragePerformanceOPtimize=PreferenceManagerUtil.getExternalStoragePerformanceOptimize(); // Get settings, whether do external storage performance optimize.
    
    paidCredittextView7.setChecked(externalStoragePerformanceOPtimize); // Show status.
  } // private void showExternalStoragePerformanceOptimize()
	
	/**
	*  Query and show root directory.
	*/
	private void queryRootDirectory()
	{
    HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance() ; // 获取应用程序实例。
    BuiltinFtpServer builtinFtpServer=null; //!< The builtin ftp server.
    builtinFtpServer=hxLauncherApplication.getBuiltinFtpServer(); // 获取FTP服务器实例对象。
    Uri uriForRootDirectory=builtinFtpServer.getVirtualPath("/"); // Get the virtual path of /.
    String uriStringRoot=null; // Uri string for root directory.
    
    if (uriForRootDirectory!=null) // It exists
    {
      
    } // if (uriForRootDirectory!=null) // It exists
    else // Not exist.
    {
      File rootDirectory=Environment.getExternalStorageDirectory();
      
      // Uri uri = Uri.parse("content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata");            
      Uri uriToLoad=Uri.fromFile(rootDirectory); // Create Uri.

      uriForRootDirectory=uriToLoad;
    } // else // Not exist.
    
    uriStringRoot=uriForRootDirectory.toString();
    
    paidCreditPrompttextView6.setText(uriStringRoot); // Show root directory.
	} // private void queryRootDirectory()
}
