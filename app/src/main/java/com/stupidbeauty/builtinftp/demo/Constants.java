package com.stupidbeauty.builtinftp.demo;

import android.os.Environment;

import java.io.File;

/**
 * 一些常量的定义。
 * @author root 蔡火胜。
 *
 */
public class Constants 
{
    /**
     * Operations。
     * @author root 蔡火胜。
     *
     */
    public final class Operation
    {
        public static final String TestShutDown = "com.stupidbeauty.shutdownat2100.testShutDown"; //!<测试关机。
        public static final String ReportMessage="com.stupidbeauty.shutdownat2100.reportMessage"; //!<报告消息到来。
        public static final String PinShortcut="com.stupidbeauty.hxlauncher.pinShortcut"; //!<钉住快捷方式。
        public static final String ToggleBuiltinShortcuts="com.stupidbeauty.hxlauncher.toggleBuiltinShortcutss"; //!<切换是否显示内置快捷方式。
        public static final String ToggleHiveLayout="com.stupidbeauty.hxlauncher.toggleHiveLayout"; //!<切换是否要使用蜂窝布局
        public static final String UnlinkVoiceCommand="com.stupidbeauty.hxlauncher.unlinkVoiceCommand"; //!<断开语音指令的链接
    } //public final class FAQLangKey

    public final class RequestCode
    {
      public static final int RootDirectoryPermissionRequestCode = 111252; //!< Request code for browseing root diretory.
    } // public final class RequestCode

    public final class Numbers
    {
        public static final int IgnoreVoiceResultLength=1; //!<短于 这个长度的语音识别结果，不处理
    }

} //public class Constants
