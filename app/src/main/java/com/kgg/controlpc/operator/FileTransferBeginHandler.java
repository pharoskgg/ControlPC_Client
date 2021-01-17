package com.kgg.controlpc.operator;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;

import com.kgg.controlpc.config.CmdServerIpSetting;
import com.kgg.controlpc.data.NetFileData;
import com.kgg.controlpc.socket.FileDownLoadSocketThread;
import com.kgg.controlpc.utility.CheckLocalDownloadFolder;
import com.kgg.controlpc.view.FileProgressDialog;

import java.io.File;
import java.util.ArrayList;

import static com.kgg.controlpc.socket.CmdSocketClient.KEY_SERVER_ACK_MSG;

public class FileTransferBeginHandler extends Handler {
    private NetFileData fileData;
    private Context context;

    private Handler handler;

    public FileTransferBeginHandler(Context context, NetFileData fileData) {
        super();
        this.fileData = fileData;
        this.context = context;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {

        Bundle bundle = msg.getData();
        ArrayList<String> msgList = bundle.getStringArrayList(KEY_SERVER_ACK_MSG);
        int port = Integer.parseInt(msgList.get(2));
        long fileSize = Long.parseLong(msgList.get(3));
        //检查是否有存储下载文件的目录，没有则创建并返回路径
        String savePath = CheckLocalDownloadFolder.check();
        File file = new File(savePath + File.separator + fileData.getFileName());
        FileProgressDialog progressDialog = new FileProgressDialog(context, "文件下载");
        this.handler = progressDialog.getHandler();
        new FileDownLoadSocketThread(CmdServerIpSetting.ip, port, handler, file, fileSize).start();
    }

}
