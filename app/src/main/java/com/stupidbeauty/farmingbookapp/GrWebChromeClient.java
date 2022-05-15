package com.stupidbeauty.farmingbookapp;

import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;

@SuppressWarnings({"FieldCanBeLocal", "CanBeFinal"})
public class GrWebChromeClient extends WebChromeClient
{
	private static String TAG="GrWebChromeClient"; //!<输出调试信息时使用的标记。

	@Override
	public boolean onConsoleMessage(ConsoleMessage consoleMessage)
	{
		Log.d(TAG, "onConsoleMessage, message: "+ consoleMessage.message() + ", line: " + consoleMessage.lineNumber() + ", source id: "+ consoleMessage.sourceId()); //Debug.

		return super.onConsoleMessage(consoleMessage);
	}

}
