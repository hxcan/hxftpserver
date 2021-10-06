package com.stupidbeauty.builtinftp.demo;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import java.util.Random;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;
import com.stupidbeauty.builtinftp.BuiltinFtpServer;
import butterknife.Bind;
import butterknife.ButterKnife;
import android.content.ClipboardManager;
import butterknife.OnClick;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;


public class LauncherActivity extends Activity 
{
    private BuiltinFtpServer builtinFtpServer=new BuiltinFtpServer(this); //!< The builtin ftp server.

    @Bind(R.id.statustextView) TextView statustextView; //!< Label to show status text.
    
    @OnClick(R.id.copyUrlButton)
    public void copyUrlButton()
    {
//     陈欣

String stringNodeCopied= statustextView.getText().toString();

            ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = android.content.ClipData.newPlainText("Copied", stringNodeCopied);

            clipboard.setPrimaryClip(clip);
    }

    /**
    * Choose a random port.
    */
    private int chooseRandomPort() 
    {
        Random random=new Random(); // Get the random.

        int randomIndex=random.nextInt(65535-1025)+1025; // Choose a random port.

        return randomIndex;
    } //private int chooseRandomPort()

    @Override
    /**
     * The activity is being created.
     */
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        int actualPort=chooseRandomPort(); // Choose a random port.
        builtinFtpServer.setPort(actualPort); // Set the port.
        builtinFtpServer.setAllowActiveMode(false); // Do not allow active mode.
        builtinFtpServer.start(); // Start the builtin ftp server.

        setContentView(R.layout.launcher_activity);

        ButterKnife.bind(this); // Inject view.

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());

        String ftpUrl="ftp://"+ ipAddress + ":"+ actualPort +"/"; // Construct the ftp server url.

        statustextView.setText(ftpUrl); // Show the FTP url
    } //protected void onCreate(Bundle savedInstanceState)
}
