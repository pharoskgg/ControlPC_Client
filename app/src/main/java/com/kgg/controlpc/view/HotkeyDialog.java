package com.kgg.controlpc.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.kgg.controlpc.data.HotKeyData;
import com.kgg.controlpc.socket.CmdSocketClient;

import java.util.ArrayList;

public class HotkeyDialog {
    private Context context;
    private ArrayList<HotKeyData> hotkeyList;
    private String title;
    private CmdSocketClient cmdSocketClient;
    private AlertDialog alertDialog;

    public HotkeyDialog(Context context, ArrayList<HotKeyData> hotkeyList, String title, CmdSocketClient cmdSocketClient) {
        this.context = context;
        this.hotkeyList = hotkeyList;
        this.title = title;
        this.cmdSocketClient = cmdSocketClient;
        init();
    }

    private void init() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(this.title);
        builder.setNegativeButton("退出", null);
        builder.setAdapter(new HotkeyGridAdapter(context, hotkeyList, cmdSocketClient), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog = builder.create();
    }

    public void show(){
        alertDialog.show();
    }
}
