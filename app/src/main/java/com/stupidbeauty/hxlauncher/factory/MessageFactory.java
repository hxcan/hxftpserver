package com.stupidbeauty.hxlauncher.factory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import android.util.Log;
import java.util.Date;    
import java.time.format.DateTimeFormatter;
import java.util.Random;
import static com.stupidbeauty.comgooglewidevinesoftwaredrmremover.Constants.Networks.RabbitMQPassword;
import static com.stupidbeauty.comgooglewidevinesoftwaredrmremover.Constants.Networks.RabbitMQUserName;
// import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Intent;
// import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.stupidbeauty.builtinftp.demo.R;

import com.upokecenter.cbor.CBORObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 
 * @author caihuosheng@gmail.com
 * @since Mar 13, 2014
 * 
 */
public class MessageFactory extends Activity
{
  private static final String TAG = "MessageFactory"; //!<调试时使用的标记。
  private int nextFileId=0; //!< next available file id.
  private EditText mLoginCountText; //!<登录用户名输入框。
  private ProgressBar mProgressBar;

  private HashMap<String, Integer> countryCodeIndexMap=new HashMap<String,Integer>(); //!<国家代码与列表下标之间的映射。
  private String[] FacePrices; //!<国家名字列表。
  
  public MessageFactory()
  {
    Random random=new Random(); //随机数生成器。

    nextFileId=random.nextInt(); // Use random file id init value.
  }

  /**
  * 构造消息体，要求获取文字消息列表。
  * @return 构造得到的消息体
  */
  public byte[] constructRequestTextMessageListMessage()
  {
    byte[] result; //结果。

    CBORObject amqpMessageBuilder=CBORObject.NewMap(); //创建消息构造器。

    basicAnnotateAmqpMessage(amqpMessageBuilder, "RequestTextMessageList"); //做基本的标记，时间戳，函数名字。

    result=amqpMessageBuilder.EncodeToBytes(); //序列化成字节数组。

    Log.d(TAG, "constructTextMesage, content: " + result.toString());

    return result;
  } //public byte[] constructRequestTextMessageListMessage()

  /**
  * 构造消息体，文字消息。
  * @param textContent 文字消息内容
  * @return 构造得到的消息体
  */
  public byte[] constructTextMessage(String textContent)
  {
    byte[] result; //结果。

    CBORObject amqpMessageBuilder=CBORObject.NewMap(); //创建消息构造器。

    basicAnnotateAmqpMessage(amqpMessageBuilder, "TextMessage"); //做基本的标记，时间戳，函数名字。

    CBORObject translateRequestBuilder = CBORObject.FromObject(textContent); //创建消息构造器。

      amqpMessageBuilder.set("textContent" , translateRequestBuilder); //设置消息，注册信息。

      result=amqpMessageBuilder.EncodeToBytes(); //序列化成字节数组。

      Log.d(TAG, "constructTextMesage, content: " + result.toString());

      return result;
	} //public byte[] constructTextMessage(String textContent)

	/**
	* 获取可用的文件编号
	*/
	public int getNextFileId() 
	{
      int result=nextFileId;
      
      nextFileId++;
      
      return result;
	} // public int getNextFileId()

	/**
	 * 做基本的标记，时间戳，函数名字。
	 * @param amqpMessageBuilder 消息体。
	 * @param registerInformation 函数名字。
	 */
	private void basicAnnotateAmqpMessage(CBORObject amqpMessageBuilder, String registerInformation)
	{
      long currentTimeStamp=System.currentTimeMillis(); //获取时间戳。

      Date date=new Date(); //日期。

      DateFormat simpleDateFormat=SimpleDateFormat.getDateTimeInstance(); //获取日期时间格式化器。
      String randomPaddingString=simpleDateFormat.format(date); //格式化日期。

      CBORObject registerInformationObject=CBORObject.FromObject(registerInformation);
      CBORObject currentTimeStampObject=CBORObject.FromObject(currentTimeStamp);
      CBORObject randomPaddingStringObject=CBORObject.FromObject(randomPaddingString);

      amqpMessageBuilder.set("functionName", registerInformationObject); //设置函数名字。
      amqpMessageBuilder.set("timeStamp" , currentTimeStampObject); //设置时间戳。
      amqpMessageBuilder.set("randomPadding", randomPaddingStringObject); //设置随机填充字符串。
	} //private void basicAnnotateAmqpMessage(HxLauncherMessage.Builder amqpMessageBuilder, HxLauncherFunctionName registerInformation)

	/**
	 * 切换是否要显示登录进度条。
	 * @param show 是否要显示。
	 */
	private void showProgressBar(boolean show) 
	{
		if (show) //要显示。 
		{
			mProgressBar.setVisibility(View.VISIBLE); //显示进度条。
		} //if (show) //要显示。
		else //隐藏进度条。 
		{
			mProgressBar.setVisibility(View.GONE); //隐藏进度条。
		} //else //隐藏进度条。
		
		return;
	} //private void showProgressBar(boolean show)

} //public class SimoLoginActivity extends BaseActivity implements OnClickListener
