package com.stupidbeauty.builtinftp.demo;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import java.util.Random;
import com.stupidbeauty.hxlauncher.asynctask.FileExtractTask;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;
import com.stupidbeauty.builtinftp.BuiltinFtpServer;
// import java.util.Map;
import butterknife.Bind;
import butterknife.ButterKnife;

public class LauncherActivity extends Activity 
{
    private BuiltinFtpServer builtinFtpServer=new BuiltinFtpServer(this); //!< The builtin ftp server.

    @Bind(R.id.statustextView) TextView statustextView; //!< Label to show status text.

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

        startExtractFiles(); // Start extracting file for demonstration.
    } //protected void onCreate(Bundle savedInstanceState)
    
    /**
    *  Start extracting files for demonstration.
    */
    private void startExtractFiles()
    {
        FileExtractTask fileExtractTask =new FileExtractTask(  ); // Create the  async task.

        fileExtractTask.execute(this); // Execute the task.
    } //private void startExtractFiles()
}
