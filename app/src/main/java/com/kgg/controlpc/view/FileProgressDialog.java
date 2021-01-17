package com.kgg.controlpc.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

public class FileProgressDialog {
    private Handler handler;
    private String title;
    private ProgressDialog pg;
    private Context context;

    int progress = 0;
    public FileProgressDialog(Context context, String title) {
        this.context = context;
        this.title = title;
        init();
    }

    private void init(){
        pg = new ProgressDialog(context);
        pg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
//        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        pg.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
//        pg.setIcon(android.R.drawable.ic_lock_lock);// 设置提示的title的图标，默认是没有的
        pg.setTitle(this.title);
        pg.setMax(100);
        pg.setMessage(title);
        pg.show();
        this.handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Bundle data = msg.getData();
                progress = data.getInt("progress");
                pg.incrementSecondaryProgressBy(progress);
                pg.setProgress(progress);
                if (progress >= 100)
                    pg.dismiss();
                return false;
            }
        });
    }

    public Handler getHandler(){
        return this.handler;
    }
}
