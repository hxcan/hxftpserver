package com.stupidbeauty.builtinftp.demo;

public interface FtpEventListenerInterface {
    void refreshAvailableSpace(); // 刷新可用的空间
    void notifyDelete(Object eventContent); // 通知文件删除
    void notifyRename(Object eventContent); // 通知文件重命名
    void notifyDownloadFinish(); // 通知文件下载完毕
    void notifyUploadFinish(Object eventContent); // 通知文件上传完毕
    void browseDocumentTree(Object eventContent); // 浏览文档树
    void guideExternalStorageManagerPermission(Object eventContent); // 指导外部存储管理权限
    void notifyDownloadStart(); // 通知文件下载开始
    void notifyIpChange(); // 通知IP变化
}
