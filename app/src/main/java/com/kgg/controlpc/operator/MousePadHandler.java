package com.kgg.controlpc.operator;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kgg.controlpc.config.ResultStatus;
import com.kgg.controlpc.socket.CmdSocketClient;

import java.util.ArrayList;

public class MousePadHandler extends Handler {
    private Context context;

    public MousePadHandler(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        Bundle data = msg.getData();
        ArrayList<String> msgList = data.getStringArrayList(CmdSocketClient.KEY_SERVER_ACK_MSG);
        String status = msgList.get(0);
        String cmd = msgList.get(1);
        if (ResultStatus.FAILURE.equalsIgnoreCase(status)) {
            showToastMsg(String.format("命令{%s}执行错误---%s", cmd, msgList.get(2)));
        } else if ("clk".equalsIgnoreCase(cmd)){
            showToastMsg(msgList.get(2));
        } else if("mov".equalsIgnoreCase(cmd)){
            showToastMsg(msgList.get(2));
        } else if ("rol".equalsIgnoreCase(cmd)){
            showToastMsg(msgList.get(2));
        }
    }

    private void showToastMsg(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
