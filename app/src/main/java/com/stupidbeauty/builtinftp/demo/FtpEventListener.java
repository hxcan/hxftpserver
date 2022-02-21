package com.stupidbeauty.builtinftp.demo;

import com.stupidbeauty.ftpserver.lib.EventListener;

public class FtpEventListener implements EventListener
{
    private LauncherActivity launcherActivity=null; //!< 启动活动。

    @Override
    public void onEvent(String eventCode)
    {
        launcherActivity.refreshAvailableSpace(); // 刷新可用的空间。
    }
    
    public FtpEventListener(LauncherActivity launcherActivity)
    {
        this.launcherActivity=launcherActivity;
    }
}

