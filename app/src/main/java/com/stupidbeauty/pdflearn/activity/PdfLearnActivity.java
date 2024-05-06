package com.stupidbeauty.pdflearn.activity;

import androidx.documentfile.provider.DocumentFile;
import java.io.File;
// import com.koushikdutta.async.callback.CompletedCallback;
// import com.koushikdutta.async.callback.ListenCallback;
import com.stupidbeauty.hxlauncher.activity.ApplicationInformationActivity;
import com.stupidbeauty.builtinftp.BuiltinFtpServer;
import butterknife.BindView;
import butterknife.ButterKnife;
import android.content.ClipboardManager;
import butterknife.OnClick;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import com.stupidbeauty.hxlauncher.manager.ActiveUserReportManager;
import android.os.Debug;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.util.Objects;
import com.stupidbeauty.codeposition.CodePosition;
import com.stupidbeauty.snowcloud.manager.FileSendManager;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import com.google.android.material.textfield.TextInputEditText;
import com.simplify.ink.InkView;
import com.stupidbeauty.builtinftp.demo.R;
import com.stupidbeauty.hxlauncher.ApplicationAliasApplicationInformationAdapter;

import com.stupidbeauty.hxlauncher.asynctask.TranslateRequestSendTask;
import com.stupidbeauty.hxlauncher.datastore.LauncherIconType;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.datastore.RuntimeInformationStore;
import com.stupidbeauty.qtdocchinese.ArticleInfo;
import org.apache.commons.io.FileUtils;
import butterknife.OnClick;
import static com.stupidbeauty.hxlauncher.datastore.LauncherIconType.ActivityIconType;

/**
 * 启动界面。
 * @author root 蔡火胜
 *
 */
public class PdfLearnActivity extends Activity 
{
  private ActiveUserReportManager activeUserReportManager=null; //!< 活跃用户统计管理器。陈欣。
  private String wallPaperFilePath; //!<记录壁纸的文件路径。
  private Intent shareIntent; //!< 分享意图。

  @BindView(R.id.articleListmy_recycler_view) RecyclerView mRecyclerView; //!<回收视图。
  @BindView(R.id.splashRelativeLayout1) RelativeLayout splashRelativeLayout1; //!< Whole layout.
  private ApplicationAliasApplicationInformationAdapter mAdapter; //!<适配器。
  ArrayList<ArticleInfo> articleInfoArrayList = new ArrayList<>(); //!<文字消息列表
  private Timer mLOGMoniterTimer=null; //!<用于检查日志文件尺寸的定时器。
  private static final int PERMISSIONS_REQUEST = 1; //!<权限请求标识
  private static final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
  private static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO; //!<录音权限。
  private static final String PERMISSION_FINE_LOCATIN = Manifest.permission.ACCESS_FINE_LOCATION; //!<位置权限
  private String outputFile = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"gongye.png"; //!<输出文件路径。
  @BindView (android.R.id.content) View contentView; //!<内容视图。

  private static final String TAG = "PdfLearnActivity"; //!<输出调试信息时使用的标记。

  /**
  * 报告语音识别命中应用的数据。
  * @param voiceRecognizeResultString 语音识别结果字符串。
  * @param packageName 命中的包名。
  */
  private void reportVoiceCommandHitData(String voiceRecognizeResultString, String packageName, String activityName, String recordSoundFilePath, LauncherIconType iconType, String iconTitle)
  {
    Log.d(TAG, "reportVoiceCommandHitData, result: " + voiceRecognizeResultString + ", title: " + iconTitle); //Debug.

    TranslateRequestSendTask translateRequestSendTask =new TranslateRequestSendTask(); //创建异步任务。

    translateRequestSendTask.execute(voiceRecognizeResultString, packageName, activityName, recordSoundFilePath, iconType, iconTitle); //执行任务。
  } //private void reportVoiceCommandHitData(String voiceRecognizeResultString, String packageName)

	/**
	 * 要求扫描照片。
	 * @param path 照片文件的路径。
	 */
	private void scanFile(String path)
	{

		MediaScannerConnection.scanFile(this,
				new String[] { path }, null,
				new MediaScannerConnection.OnScanCompletedListener() {

					public void onScanCompleted(String path, Uri uri) {
						Log.i("TAG", "Finished scanning " + path);
					}
				});
	} //private void scanFile(String path)

    @Override
	/**
	 * 活动被创建。
	 */
	public void onCreate(Bundle savedInstanceState) 
	{
      super.onCreate(savedInstanceState); //创建超类。

      requestWindowFeature(Window.FEATURE_NO_TITLE); //无标题栏。

      setContentView(R.layout.pdf_learn_write_layout); //设置显示内容。
		
      ButterKnife.bind(this); //视图注入。

      bindAdapter(); //绑定适配器。

      checkPermission(); //检查权限。

      createPictureDirectory(); //创建照片目录。

      startActivityByApiLevel(); //根据应用编程接口级别，启动对应的活动。

      createActiveUserReportManager(); // 创建管理器，活跃用户统计。陈欣
      
      addRecyclerViewOnTouchListerner(); // Add recyclerview on touch listenter.
	} //public void onCreate(Bundle savedInstanceState)
	
	/**
	* Add recyclerview on touch listener.
	*/
	private void addRecyclerViewOnTouchListerner()
	{  
      View.OnTouchListener hideKeyboardListner=new View.OnTouchListener()
      {
        public boolean onTouch(View v, MotionEvent event)
        {
          hideKeyboard(); // Hide key board.
        
          return false;
        }
      };
	
      mRecyclerView.setOnTouchListener(hideKeyboardListner);
	} // private void addRecyclerViewOnTouchListerner()
	
    /**
    * 创建管理器，活跃用户统计。陈欣
    */
    private void createActiveUserReportManager()
    {
      if (activeUserReportManager==null) // 还不存在管理器。
      {
        activeUserReportManager=new ActiveUserReportManager(); // 创建管理器。
            
        activeUserReportManager.startReportActiveUser(); // 开始报告活跃用户。
      } //if (activeUserReportManager==null)
    } //private void createActiveUserReportManager()

	/**
	 * Adds the image specified by the Uri IMAGEURI to the current view.
	 */
	private void addImage(Uri imageURI)
	{
    Log.d(TAG, "addImage." + CodePosition.newInstance().toString()); //Debug.
    String[] filePathColumn = { MediaStore.Images.Media.DATA, MediaStore.Images.Media.DESCRIPTION };

    String filePath; //文件路径。

    Log.d(TAG, "addImage." + CodePosition.newInstance().toString()); //Debug.
    byte[] content=new byte[1]; //文件内容。
    FileInputStream fileInputStream=null; // File input stream.

    long fileSize=0; //获取文件尺寸。
      
    InputStream reader = null;

    if (imageURI.getScheme().equals("file")) //是标准的文件型路径。
    {
      Log.d(TAG, "addImage." + CodePosition.newInstance().toString()); //Debug.
      filePath = imageURI.getPath(); //获取文件路径。

      wallPaperFilePath=filePath; //记录壁纸的文件路径。

      Log.d(TAG, "addImage." + CodePosition.newInstance().toString()); //Debug.
      File photoFile=new File(filePath);

        try
        {
          fileInputStream=new FileInputStream(photoFile); // Create file input stream.
          Log.d(TAG, "addImage." + CodePosition.newInstance().toString()); //Debug.
//           content=FileUtils.readFileToByteArray(photoFile); //将照片文件内容全部读取。
        }
        catch (IOException e)
        {
          Log.d(TAG, "addImage." + CodePosition.newInstance().toString()); //Debug.
          e.printStackTrace();
        }
      } //if (imageURI.getScheme()=="file"); //是标准的文件型路径。
      else //是内容型路径。
      {
        Log.d(TAG, "addImage." + CodePosition.newInstance().toString()); //Debug.
        try
        {
          ParcelFileDescriptor inputPFD = getContentResolver().openFileDescriptor(imageURI, "r");

          Log.d(TAG, "addImage." + CodePosition.newInstance().toString()); //Debug.
          FileDescriptor fd = inputPFD.getFileDescriptor();

          // fileInputStream = new FileInputStream(fd);

          Log.d(TAG, "addImage." + CodePosition.newInstance().toString()); //Debug.
          fileSize=inputPFD.getStatSize(); //获取文件尺寸。
          
          reader =  getContentResolver().openInputStream(imageURI);
            
//           reader = new InputStreamReader(Objects.requireNonNull(inputStream));

//           content=new byte[(int)fileSize]; //改变大小。

          Log.d(TAG, "addImage." + CodePosition.newInstance().toString()); //Debug.
//           fileInputStream.read(content);
        }
        catch (FileNotFoundException e)
        {
          Log.d(TAG, "addImage." + CodePosition.newInstance().toString()); //Debug.
          e.printStackTrace();
        } // catch (FileNotFoundException e)
        catch (SecurityException e) //权限受限。一般是某些手机系统有问题。
        {
          Log.d(TAG, "addImage." + CodePosition.newInstance().toString()); //Debug.
          e.printStackTrace();
        } //catch (SecurityException e) //权限受限。一般是某些手机系统有问题。
      } //else //是内容型路径。
      
      Log.d(TAG, "addImage." + CodePosition.newInstance().toString()); //Debug.
      String filePathWithoutSchema=imageURI.getPath(); //提取出不带模式的路径。

      int fileId=sendFile(fileInputStream, filePathWithoutSchema, fileSize, reader); // Send the file.
      
      Uri uri = null;
      uri = imageURI; // Get the shared file uri.

      DocumentFile sharedFilet=DocumentFile.fromSingleUri(this, uri); // 04-08 18:22:04.279 15010 15045 W System.err: java.lang.IllegalArgumentException: Invalid URI: file:///storage/emulated/0/DCIM/GoddessCamera
      Log.d(TAG, "addImage." + CodePosition.newInstance().toString() + ", shared file : " + sharedFilet + ", uri: " + uri); //Debug.
      
      // 05-06 19:47:11.015  7395  7395 W DocumentFile: Failed query: java.lang.NullPointerException: Attempt to invoke interface method 'boolean android.database.Cursor.moveToFirst()' on a null object reference
      String fileNaturalName = sharedFilet.getName(); // Gte the natural name.
      
      Log.d(TAG, "addImage." + CodePosition.newInstance().toString() + ", file natural name: " + fileNaturalName); //Debug.
      if (fileNaturalName == null) // Failed with DocumentFile interface. Old platform, file:// scehma.
      {
        fileNaturalName = uri.getLastPathSegment(); // Get directly from uri.
      } // if (fileNaturalName == null) // Failed with DocumentFile interface. Old platform, file:// scehma.

      HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance() ; // 获取应用程序实例。
      BuiltinFtpServer builtinFtpServer=null; //!< The builtin ftp server.
      builtinFtpServer=hxLauncherApplication.getBuiltinFtpServer(); // 获取FTP服务器实例对象。
      
      boolean noTakePermission = false; // Do not take the permission.
      
      Log.d(TAG, "addImage." + CodePosition.newInstance().toString() + ", file natural name: " + fileNaturalName); //Debug.
      builtinFtpServer.mountVirtualPath("/hxftpserver/" + fileNaturalName , uri, noTakePermission); // Mount virtual path.
      
      createPlaceHolderFile(fileNaturalName); // Create the place holder file.

      Log.d(TAG, "addImage." + CodePosition.newInstance().toString()+", file id: "+fileId); //Debug.

      int actualPort=builtinFtpServer.getActualPort(); // 获取实际的端口。
      String actualIp=builtinFtpServer.getIp(); // Get the actual ip.

      String ftpServerUrl="ftp://"+ actualIp + ":"+ actualPort +"/"; // Construct the ftp server url.


      filePathWithoutSchema = ftpServerUrl + "hxftpserver/" + fileNaturalName; // Construct the whole url.
      
      showFileMessage(filePathWithoutSchema, filePathWithoutSchema, fileId, fileSize); // Show file message.
      
      int targetSideLength=312;
      Log.d(TAG, "addImage." + CodePosition.newInstance().toString()); //Debug.
	} //private void addImage(Uri imageURI)
	
    @Override
    protected void onNewIntent(Intent intent)
    {
      Log.d(TAG, "onNewIntent, intent: " + intent); //Debug.
      super.onNewIntent(intent);

      processIntent(intent); // Process intent.
    }

    /**
    * Process intent.
    */
    private void processIntent(Intent Itnt) 
    {
      shareIntent=Itnt; // 记录分享意图。
      Log.d(TAG, CodePosition.newInstance().toString()+  ", intent: " + Itnt); // Debug.

      Bundle bundle=Itnt.getExtras(); //获取额外数据。
      Log.d(TAG, CodePosition.newInstance().toString()+  ", bundle: " + bundle); // Debug.

      Uri imageURI=null;

      if (Itnt.getAction() != null && Itnt.getAction().equals(Intent.ACTION_SEND)) //要查看图片。
      {
        String type = Itnt.getType(); // Get the type.
        
        // imageURI = Itnt.getData();
        // Log.d(TAG, CodePosition.newInstance().toString()+  ", image uri: " + imageURI); // Debug.

        imageURI=Itnt.getParcelableExtra(Intent.EXTRA_STREAM); //获取图片地址。
        Log.d(TAG, CodePosition.newInstance().toString()+  ", image uri: " + imageURI); // Debug.

        addImage(imageURI); //显示图片。
      } //if (Itnt.getAction() != null && Itnt.getAction().equals(Intent.ACTION_VIEW)) //要查看图片。
    } // private void processIntent(Intent Itnt)
	
	/**
	 * 根据应用编程接口级别，启动对应的活动。
	 */
	private void startActivityByApiLevel()
	{
    Intent Itnt=getIntent(); //获取用于启动此窗口的意图。
    
    processIntent(Itnt); // Process intent.
	} //private void startActivityByApiLevel()
	
	/**
	* Create the place holder file.
	*/
	private void createPlaceHolderFile(String fileNaturalName)
	{
    File rootDirectory = Environment.getExternalStorageDirectory();
    
    String rootDirectoryPath = rootDirectory.getPath(); // Get the root directory path.

    File goddessCameraDirectory=new File( rootDirectoryPath + File.separator + "hxftpserver" + File.separator + fileNaturalName); // the file path.
    // File goddessCameraDirectory=new File(DOWNLOAD_SD_CARD_PATH+File.separator+"SnowCloud"); //女神相机目录。

    try
    {
      goddessCameraDirectory.createNewFile(); // Create the file.
    }
    catch (IOException e)
    {
    }; 
	} // private void createPlaceHolderFile(String fileNaturalName)

	/**
	 * 创建照片目录。
	 */
	private void createPictureDirectory()
	{
    File rootDirectory = Environment.getExternalStorageDirectory();
    
    String rootDirectoryPath = rootDirectory.getPath(); // Get the root directory path.

    File goddessCameraDirectory=new File( rootDirectoryPath + File.separator + "hxftpserver"); // the hxftpserver directory.
    // File goddessCameraDirectory=new File(DOWNLOAD_SD_CARD_PATH+File.separator+"SnowCloud"); //女神相机目录。

    goddessCameraDirectory.mkdirs(); //创建目录。
	} //private void createPictureDirectory()

	/**
	 * 绑定适配器。
	 */
	private void bindAdapter()
	{
      int columnsPerRow= getResources().getInteger(  R.integer.columnsPerRow); //每行的列数。

      RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this); //布局管理器。
      mRecyclerView.setLayoutManager(mLayoutManager);

      mAdapter=new ApplicationAliasApplicationInformationAdapter(this); //应用程序信息适配器。
      mRecyclerView.setAdapter(mAdapter);
	} //private void bindAdapter()

	/**
	 * 显示 file message。
	 * @param uid File name
	 * @param fileId file id
	 * @param fileLength file whole length
	 */
	private void showFileMessage(String uid, long fileId, int fileLength, int contentPaertLength, String filePath)
	{
      int fileReceivedLength=contentPaertLength;
      ArticleInfo currentApplication=null;

      if (currentApplication==null) //Does not exist
      {
        currentApplication=new ArticleInfo(); //创建应用程序信息对象。

        Log.d(TAG, CodePosition.newInstance().toString()); //Debug.
        currentApplication.setFunctionName("FileMessage"); //Set function name as file message
        currentApplication.setFileLength(fileLength); //Set file length
        currentApplication.setFilePath(filePath);
        currentApplication.setApplicationLabel(uid); //设置应用程序文字。

//         currentApplication.setReceivedLength(contentPaertLength); //Sset received length

        Log.d(TAG, CodePosition.newInstance().toString()); //Debug.
        articleInfoArrayList.add(currentApplication); //添加应用。
      } //if (currentApplication==null) //Does not exist

      currentApplication.setReceivedLength(fileReceivedLength);

      mAdapter.setArticleInfoArrayList(articleInfoArrayList); //设置文章信息列表。
      mAdapter.notifyDataSetChanged(); //通知数据变更。
      Log.d(TAG, CodePosition.newInstance().toString()); //Debug.
	} //private void showFileMessage(String uid, int fileId, int fileLength)

	/**
	 * 显示 file message。
	 * @param uid file name
	 */
	private void showFileMessage(String uid, String filePath, long fileId, long fileLength)
	{
      Log.d(TAG, CodePosition.newInstance().toString()+", file id: "+fileId); //Debug.
      ArticleInfo currentApplication=new ArticleInfo(); //创建应用程序信息对象。

      currentApplication.setApplicationLabel(uid); //设置应用程序文字。
      currentApplication.setFilePath(filePath); // SEt file path.
      Log.d(TAG, CodePosition.newInstance().toString()+", file id: "+fileId); //Debug.
      currentApplication.setFileId(fileId); // SEt file id.
      currentApplication.setFunctionName("FileMessage"); //Set function name as file message
      currentApplication.setFileLength(fileLength); // Set file length.

      Log.d(TAG, CodePosition.newInstance().toString()+", file id: "+fileId); //Debug.

      articleInfoArrayList.add(currentApplication); //添加应用。

      mAdapter.setArticleInfoArrayList(articleInfoArrayList); //设置文章信息列表。
      mAdapter.notifyDataSetChanged(); //通知数据变更。
	} //private void showFileMessage(String uid)

	/**
	 * 显示文字内容。
	 * @param uid 文字内容
	 */
	private void showTextMessage(String uid)
	{
      ArticleInfo currentApplication=new ArticleInfo(); //创建应用程序信息对象。

      currentApplication.setApplicationLabel(uid); //设置应用程序文字。

      articleInfoArrayList.add(currentApplication); //添加应用。

      mAdapter.setArticleInfoArrayList(articleInfoArrayList); //设置文章信息列表。
      mAdapter.notifyDataSetChanged(); //通知数据变更。
	} //private void showTextMessage(String uid)

	/**
	* Send the file.
	*/
	private int sendFile(FileInputStream content, String filePathWithoutSchema, long fileSize, InputStream reader)
	{
      Log.d(TAG, CodePosition.newInstance().toString()); //Debug.
      //由消息工厂构造消息体：
      HxLauncherApplication hxlauncherApplication=HxLauncherApplication.getInstance(); //获取应用程序对象。


      Log.d(TAG, CodePosition.newInstance().toString()); //Debug.
      
      FileSendManager fileSendManager=new FileSendManager(); // Create file send manager.
      
      int result=fileSendManager.sendFile(content, filePathWithoutSchema, fileSize, reader); // Send the file.
      Log.d(TAG, CodePosition.newInstance().toString()); //Debug.
      
      return result;
	} // private void sendFile(byte[] content, String filePathWithoutSchema)
	
//       @OnClick({R.id.homeNewsLayout,R.id.launchRipple})
	@OnClick({R.id.splashRelativeLayout1,R.id.articleListmy_recycler_view,R.id.darkBackgroundImage,R.id.wallpaper})
	public void hideKeyboard()
	{
    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(splashRelativeLayout1.getWindowToken(), 0);
	} // public void hideKeyboard()
	
	/**
	 * 绘制背景图片
	 */
	private void drawBackgroundImage()
	{
      boolean tileVertically=false; //竖直平铺
      boolean tileHorizontally=false; //水平平铺
	} //private void	drawBackgroundImage()

	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) 
    {
      // First decode with inJustDecodeBounds=true to check dimensions
      final BitmapFactory.Options options = new BitmapFactory.Options();
      //		options.inJustDecodeBounds = true;
      //		BitmapFactory.decodeResource(res, resId, options);

      // Calculate inSampleSize
      //		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

      // Decode bitmap with inSampleSize set
      options.inJustDecodeBounds = false;
      return BitmapFactory.decodeResource(res, resId, options);
	}

	public static int calculateInSampleSize( BitmapFactory.Options options, int reqWidth, int reqHeight) 
	{
      // Raw height and width of image
      final int height = options.outHeight;
      final int width = options.outWidth;
      int inSampleSize = 1;

      if (height > reqHeight || width > reqWidth) 
      {
        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) 
        {
          inSampleSize *= 2;
        }
      }

      return inSampleSize;
	}

	/**
	 * 检查权限。
	 */
	private void checkPermission()
	{
      if (hasPermission()) 
      {
      }
      else 
      {
        requestPermission();
      }
	} //private void checkPermission()

	/**
	 * 请求获取权限
	 */
	private void requestPermission()
	{
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //动态权限
      {
        if ( shouldShowRequestPermissionRationale(PERMISSION_STORAGE)  || shouldShowRequestPermissionRationale(PERMISSION_RECORD_AUDIO) || shouldShowRequestPermissionRationale(PERMISSION_FINE_LOCATIN)) //应当告知原因。
        {
          Toast.makeText(this, "Camera AND storage permission are required for this demo", Toast.LENGTH_LONG).show();
        } //if ( shouldShowRequestPermissionRationale(PERMISSION_STORAGE)  || shouldShowRequestPermissionRationale(PERMISSION_RECORD_AUDIO)) //应当告知原因。
        requestPermissions(new String[] {PERMISSION_STORAGE, PERMISSION_RECORD_AUDIO, PERMISSION_FINE_LOCATIN}, PERMISSIONS_REQUEST);
      } //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //动态权限
	} //private void requestPermission()

	private boolean hasPermission()
	{
      boolean result=false; //结果。

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //安卓6.
      {
        result= checkSelfPermission(PERMISSION_STORAGE) == PackageManager.PERMISSION_GRANTED; //存储权限。

        if (result) //存储权限已有。
        {
          result=(checkSelfPermission(PERMISSION_RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED); //录音权限。
        } //if (result) //存储权限已有。
      } //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //安卓6.
      else //旧版本。
      {
        result=true; //有权限。
      } //else //旧版本。

      return result;
	} //private boolean hasPermission()
} //public class SimoStartupActivity extends Activity
