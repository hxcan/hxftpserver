package com.stupidbeauty.hxlauncher.asynctask;

import android.content.Context;
import android.content.pm.PackageItemInfo;
import android.os.AsyncTask;
// import android.util.Log;
// import java.util.Set;
import java.util.HashSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import com.stupidbeauty.victoriafresh.VFile;

import java.io.File;
// import java.io.IOException;
// import java.io.InputStream;
import java.nio.charset.StandardCharsets;
// import java.util.Collection;
import java.util.Random;

/**
 * @author Hxcan
 * @since Mar 13, 2014
 */
public final class FileExtractTask extends AsyncTask<Object, Void, Boolean>
{

	private static final String TAG="FileExtractTask"; //!<输出调试信息时使用的标记。

		@Override
		protected Boolean doInBackground(Object... params)
        {
            //参数顺序：
//            context
            Context context=(Context)(params[0]); // 上下文。陈欣
            
            Boolean result=false; //结果，是否成功。
            
            releaseVfsFile(context, ":/drawable-hdpi/icon.jpg"); // Release the vfs file.
            releaseVfsFile(context, ":/drawable-hdpi/stopusingphone.webp"); // Release the vfs file.
            releaseVfsFile(context, ":/mipmap-xxhdpi.ek/img_video_2.webp"); // Release the vfs file.
            releaseVfsFile(context, ":/mipmap-xxhdpi.ek/img_video_1.webp"); // Release the vfs file.
            releaseVfsFile(context, ":/mipmap-xxhdpi.ek/img_video_3.webp"); // Release the vfs file.
            releaseVfsFile(context, ":/mipmap-xxhdpi.ek/img_video_5.webp"); // Release the vfs file.
            releaseVfsFile(context, ":/mipmap-xxhdpi.ek/img_video_6.webp"); // Release the vfs file.
            releaseVfsFile(context, ":/mipmap-xxhdpi.ek/img_video_7.webp"); // Release the vfs file.
            releaseVfsFile(context, ":/mipmap-xxhdpi.ek/img_video_8.webp"); // Release the vfs file.
            releaseVfsFile(context, ":/mipmap-xxhdpi.ek/header_icon_1.jpg"); // Release the vfs file.
            releaseVfsFile(context, ":/mipmap-xxhdpi.ek/header_icon_2.webp"); // Release the vfs file.
            releaseVfsFile(context, ":/mipmap-xxhdpi.ek/icon_home_like_after.png"); // Release the vfs file.
            releaseVfsFile(context, ":/mipmap-xxhdpi.ek/icon_home_like_before.png"); // Release the vfs file.
            releaseVfsFile(context, ":/ApplicationIcon/com.jingdong.app.mall"); // Release the vfs file.
            releaseVfsFile(context, ":/ApplicationIcon/com.shoudu.se2.actv"); // Release the vfs file.
            releaseVfsFile(context, ":/ApplicationIcon/com.ss.android.article.news"); // Release the vfs file.
            releaseVfsFile(context, ":/ApplicationIcon/com.stupidbeauty.filepathdetector"); // Release the vfs file.
            releaseVfsFile(context, ":/ApplicationIcon/com.taobao.taobao"); // Release the vfs file.
            releaseVfsFile(context, ":/VoicePackageItemMap/voicePackageNameMap.ost.exz"); // Release the vfs file.
            releaseVfsFile(context, ":/VoicePackageItemMap/voiceShortMap.ost"); // Release the vfs file.

            return result;
		}
		
		/**
		* Release the vfs file.
		*/
		private void releaseVfsFile(Context context, String mipmapxxhdpiekimg_video_2png) 
		{
            String fullQrcFileName=mipmapxxhdpiekimg_video_2png; //构造完整的qrc文件名。

            VFile qrcHtmlFile=new VFile(context, fullQrcFileName); //qrc网页文件。

            String targetFileName=qrcHtmlFile.getFileName();
                
            qrcHtmlFile.copy(targetFileName);
		} //private void releaseVfsFile(QString mipmapxxhdpiekimg_video_2png)

    /**
     * 报告结果。
     * @param result 结果。是否成功。
     */
		@Override
		protected void onPostExecute(Boolean result)
        {
		} //protected void onPostExecute(Boolean result)
	}



