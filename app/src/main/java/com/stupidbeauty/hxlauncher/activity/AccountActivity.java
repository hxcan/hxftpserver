package com.stupidbeauty.hxlauncher.activity;

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

public class AccountActivity extends Activity
{
	private static final String TAG = "AccountActivity"; //!<输出调试信息时使用的标记。
	private static final int TIMEOUT = 30000;
	private static final int SHOW_PROGRESS_DIALOG = 0;
	private static final int DISMISS_PROGRESS_DIALOG = 1;
	private static final int SHOW_CUSTOM_404_VIEW = 2;
	
	@Bind(R.id.load_progress) ProgressBar mProgressBar; //!<载入网页时的进度条。
	
	private boolean mIsRequestError = false;
		
	/**
	 * handler处理消息机制
	 */
	protected Handler handler = new Handler() {
		public void handleMessage(Message message) {
			switch (message.what) {
			case SHOW_PROGRESS_DIALOG:
				mProgressBar.setVisibility(View.VISIBLE);
					
				break;
			case DISMISS_PROGRESS_DIALOG:
				mProgressBar.setVisibility(View.GONE);
				break;
			case SHOW_CUSTOM_404_VIEW:
				mProgressBar.setVisibility(View.GONE);
				mIsRequestError = true;
				
				break;
			}
		}
	};
	
	@Bind(R.id.forward_nav_imageView4) ImageView mForwardButton; //!<网页视图导航条的前进按钮。

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent resultData) 
  {
    Log.d(TAG, CodePosition.newInstance().toString()+  ", request code: " + requestCode + ", result code: " + resultCode + ", uri to use: " + resultData); // Debug.
    //       if (requestCode == your-request-code && resultCode == Activity.RESULT_OK) 
    if (resultCode == Activity.RESULT_OK) 
    {
      // The result data contains a URI for the document or directory that
      // the user selected.
      Uri uri = null;
      if (resultData != null) // There is result data
      {
        uri = resultData.getData();
        Log.d(TAG, CodePosition.newInstance().toString()+  ", request code: " + requestCode + ", result code: " + resultCode + ", uri to use: " + uri.toString()); // Debug.
        // Perform operations on the document using its URI.
        
        //           Chen xin.
        // builtinFtpServer.answerBrowseDocumentTreeReqeust(requestCode, uri); // Answ4er the browse docuembnt tree reqeust.
    HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance() ; // 获取应用程序实例。
  BuiltinFtpServer builtinFtpServer=null; //!< The builtin ftp server.
    builtinFtpServer=hxLauncherApplication.getBuiltinFtpServer(); // 获取FTP服务器实例对象。
        builtinFtpServer.mountVirtualPath("/" , uri); // Mount virtual path.
      } // if (resultData != null) // There is result data
    }
  }

	/**
	 * 跳转到登录界面。
	 */
	@OnClick(R.id.loginbutton)
	public void gotoLoginActivity()
	{
		Log.d(TAG, "gotoLoginActivity, 119."); //Debug.
//		Intent launchIntent=new Intent(this, EmailLoginActivity.class); //启动意图。
//		startActivity(launchIntent); //启动活动。

    StorageManager sm = (StorageManager) getSystemService(Context.STORAGE_SERVICE);

    Intent intent = sm.getPrimaryStorageVolume().createOpenDocumentTreeIntent();

      // Choose a directory using the system's file picker.
      // Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

//       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      // intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);            
      
      // Uri uriToLoad = Uri.parse("content://com.android.externalstorage.documents/document/primary");            

      // File androidDataFile=new File(Constants.FilePath.AndroidData); // Get the file object.
      File rootDirectory=Environment.getExternalStorageDirectory();
      
      // Uri uri = Uri.parse("content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata");            
      Uri uriToLoad=Uri.fromFile(rootDirectory); // Create Uri.

      
      // Optionally, specify a URI for the directory that should be opened in
      // the system file picker when it loads.
      // intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uriToLoad);

      // String packageNmae=context.getPackageName();
      // Log.d(TAG, "gotoFileManagerSettingsPage, package name: " + packageNmae); //Debug.

      // String url = "package:"+packageNmae;

      // Log.d(TAG, "gotoFileManagerSettingsPage, url: " + url); //Debug.

//       intent.setData(Uri.parse(url));

      int yourrequestcode=Constants.RequestCode.RootDirectoryPermissionRequestCode; // The request code
      
//       context.startActivityForResult(intent, yourrequestcode);

      startActivityForResult(intent, yourrequestcode);


		Log.d(TAG, "gotoLoginActivity, 122."); //Debug.
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

		String url = ""; //账户URL。
		
	} //protected void onCreate(Bundle savedInstanceState)
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	
}
