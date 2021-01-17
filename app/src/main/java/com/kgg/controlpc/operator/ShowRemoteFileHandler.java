package com.kgg.controlpc.operator;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kgg.controlpc.activity.MainActivity;
import com.kgg.controlpc.config.ResultStatus;
import com.kgg.controlpc.data.NetFileData;
import com.kgg.controlpc.socket.CmdSocketClient;
import com.kgg.controlpc.view.NetFileDataAdapter;

import java.util.ArrayList;
import java.util.List;


public class ShowRemoteFileHandler extends Handler {
    private Context context;
    private ListView listView;

    public ShowRemoteFileHandler(Context context, ListView listView) {
        super();
        this.context = context;
        this.listView = listView;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        Bundle data = msg.getData();
        ArrayList<String> msgList = data.getStringArrayList(CmdSocketClient.KEY_SERVER_ACK_MSG);
        String status = msgList.get(0);
        String cmd = msgList.get(1);
        if (ResultStatus.FAILURE.equalsIgnoreCase(status)){
            showToastMsg(String.format("命令{%s}执行错误---%s", cmd, msgList.get(2)));
        } else {
            if ("dir".equalsIgnoreCase(cmd)){
                //获取绝对路径
                String absolute = msgList.get(2);
                if ("root".equalsIgnoreCase(absolute))
                    MainActivity.fileAbsolotePath = "\\";
                else
                    MainActivity.fileAbsolotePath = absolute;
                //之后为文件列表
                List<NetFileData> fileList = fileListParse(absolute, msgList.subList(3, msgList.size()));
                listView.setAdapter(new NetFileDataAdapter(context, fileList));
            } else if ("opn".equalsIgnoreCase(cmd) || "cps".equalsIgnoreCase(cmd) || "cmd".equalsIgnoreCase(cmd)){
                showToastMsg(msgList.get(2));
            }
        }
    }

    private List<NetFileData> fileListParse(String absolute, List<String> fileStrList) {
        ArrayList<NetFileData> fileList = new ArrayList<>();

        for (String str_file : fileStrList){
            String[] infos = str_file.split(">");
            String fileName = infos[0];
            long fileDate = Long.parseLong(infos[1]);
            long fileSize = Long.parseLong(infos[2]);
            int fileType = Integer.parseInt(infos[3]);
            NetFileData file = new NetFileData(fileDate, fileName, fileType);
            //当不为盘符时，有绝对路径，为盘符时，绝对路径为：./
            if (fileType != 2){
                file.setFilePath(absolute);
                file.setFileSize(fileSize);
            }else
                file.setFilePath("");
            fileList.add(file);
        }
        return fileList;
    }

    private void showToastMsg(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
