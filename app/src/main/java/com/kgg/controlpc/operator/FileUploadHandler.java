package com.kgg.controlpc.operator;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.kgg.controlpc.config.CmdServerIpSetting;
import com.kgg.controlpc.data.NetFileData;
import com.kgg.controlpc.socket.FileDownLoadSocketThread;
import com.kgg.controlpc.socket.FileUpLoadSocketThread;
import com.kgg.controlpc.utility.CheckLocalDownloadFolder;
import com.kgg.controlpc.view.FileProgressDialog;

import java.io.File;
import java.util.ArrayList;

import static com.kgg.controlpc.socket.CmdSocketClient.KEY_SERVER_ACK_MSG;

public class FileUploadHandler extends Handler {
//    private NetFileData fileData;
    private String filePath;
    private Context context;

    private Handler handler;

    public FileUploadHandler(Context context, String filePath) {
        super();
        this.filePath = filePath;
        this.context = context;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        Bundle bundle = msg.getData();
        ArrayList<String> msgList = bundle.getStringArrayList(KEY_SERVER_ACK_MSG);
        int port = Integer.parseInt(msgList.get(2));
        System.out.println("可以开始处理数据了");
//        long fileSize = Long.parseLong(msgList.get(3));
        //检查是否有存储下载文件的目录，没有则创建并返回路径
//        String savePath = CheckLocalDownloadFolder.check();
        File file = new File(filePath);
        FileProgressDialog progressDialog = new FileProgressDialog(context, "文件上传");
        this.handler = progressDialog.getHandler();

        new FileUpLoadSocketThread(CmdServerIpSetting.ip, port, handler, file, file.length()).start();
//        new FileDownLoadSocketThread(CmdServerIpSetting.ip, port, handler, file, fileSize).start();
    }
}
