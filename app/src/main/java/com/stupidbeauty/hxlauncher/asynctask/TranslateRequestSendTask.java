package com.stupidbeauty.hxlauncher.asynctask;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.google.protobuf.ByteString;
// import com.rabbitmq.client.Channel;
// import com.rabbitmq.client.Connection;
// import com.rabbitmq.client.ConnectionFactory;
// import com.rabbitmq.client.MessageProperties;
// import com.stupidbeauty.hxlauncher.VoiceCommandHitDataMessageProtos;
import com.stupidbeauty.hxlauncher.datastore.LauncherIconType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Random;

import static com.stupidbeauty.comgooglewidevinesoftwaredrmremover.Constants.Networks.RabbitMQPassword;
import static com.stupidbeauty.comgooglewidevinesoftwaredrmremover.Constants.Networks.RabbitMQUserName;
import static com.stupidbeauty.comgooglewidevinesoftwaredrmremover.Constants.Networks.TRANSLATE_REQUEST_QUEUE_NAME;
// import static com.stupidbeauty.hxlauncher.HxLauncherIconType.PbActivityIconType;
// import static com.stupidbeauty.hxlauncher.HxLauncherIconType.PbShortcutIconType;


/**
 * @author Hxcan
 * @since Mar 13, 2014
 */
public final class TranslateRequestSendTask extends AsyncTask<Object, Void, Boolean>
{

	private static final String TAG="TranslateRequestSen"; //!<输出调试信息时使用的标记。

		@Override
		protected Boolean doInBackground(Object... params)
        {
            //参数顺序：
//            voiceRecognizeResultString, packageName, activityName, recordSoundFilePath, iconType, iconTitle

            Boolean result=false; //结果，是否成功。

            //使用protobuf将各个字段序列化成字节数组，然后使用rabbitmq发送到服务器。

            String subject=(String)(params[0]); //获取识别结果文字内容。

            String body=(String)(params[1]); //获取包名。
            String acitivtyName=(String)(params[2]); //活动名字。
            String recordSoundFilePath=(String)(params[3]); //录音文件路径．
            LauncherIconType iconType=(LauncherIconType)(params[4]); //图标类型．
            String iconTitle=(String)(params[5]); //图标标题．

            File photoFile=new File(recordSoundFilePath); //录音文件。

            try //尝试构造请求对象，并且捕获可能的异常。
            {
                ByteString photoByteArray=null; //照片的字节数组。

                if (photoFile!=null) //找到了照片文件。
                {
                    byte[] photoBytes= FileUtils.readFileToByteArray(photoFile); //将照片文件内容全部读取。
                    photoByteArray=ByteString.copyFrom(photoBytes); //构造照片的字节字符串。
                } //if (photoFile!=null) //找到了照片文件。
			} //try //尝试构造请求对象，并且捕获可能的异常。
            catch (Exception e)
            {
				e.printStackTrace();
			}

			// parse xml to SimoResponseData
			return result;
		}


    /**
     * 报告结果。
     * @param result 结果。是否成功。
     */
		@Override
		protected void onPostExecute(Boolean result)
        {


		} //protected void onPostExecute(Boolean result)
	}



