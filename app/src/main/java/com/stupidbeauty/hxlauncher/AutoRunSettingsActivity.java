package com.stupidbeauty.hxlauncher;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
// import androidx.recyclerview.widget.RecyclerView;
// import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import com.stupidbeauty.builtinftp.demo.R;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
// import com.stupidbeauty.hxlauncher.bean.ApplicationListData;
import com.stupidbeauty.hxlauncher.interfaces.LocalServerListLoadListener;
// import com.stupidbeauty.qtdocchinese.ArticleInfo;
// import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import butterknife.Bind;
import butterknife.ButterKnife;

public class AutoRunSettingsActivity extends Activity
{
    private Map<String, Boolean> packageNameAutoRunMap=new HashMap<>(); //!<应用程序包名与是否要自动启动的映射。
    private static final String TAG="AutoRunSettingsActivity"; //!<输出调试信息时使用的标记。

//     @Bind(R.id.articleListmy_recycler_view) RecyclerView mRecyclerView; //!<回收视图。

//     private AutoRunApplicationInformationAdapter mAdapter; //!<适配器。

    @Override
    /**
     * 活动被创建。
     */
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.auto_run_settings_activity); //设置界面内容。

        ButterKnife.bind(this); //视图注入。

        initializeBus(); //初始化总线。


    } //protected void onCreate(Bundle savedInstanceState)

    /**
     * 初始化总线。
     */
    private void initializeBus()
    {
//        bus=new Bus(); //初始化总线。

//        mAdapter.setBus(bus); //设置总线。

//        bus.register(this); //注册总线。
    } //private void initializeBus()




    /**
     * 随机寻找一个照片文件。
     * @return 随机寻找的一个照片文件。
     */
    @SuppressWarnings("StatementWithEmptyBody")
    private  File findRandomPhotoFile()
    {
        File result=null;

        File filesDir=getFilesDir();

        Log.d(TAG, "findRandomPhotoFile, files dir: "+ filesDir); //Debug.

        if (filesDir==null) //该目录不存在。
        {

        } //if (filesDir==null) //该目录不存在。
        else //该目录存在。
        {
            result=new File(filesDir.getAbsolutePath()+"/packageAutoRunMap.otz"); //指定文件名。
//            R

            if (result.exists()) //文件存在。
            {

            } //if (result.exists()) //文件存在。
            else //文件不存在。
            {
                try
                {
                    boolean createResult=result.createNewFile(); //创建文件。

                    Log.d(TAG, "findRandomPhotoFile, create file result: " + createResult); //Debug.

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            } //else //文件不存在。
        } //else //该目录存在。

        return result;
    } //private  File findRandomPhotoFile()



}



