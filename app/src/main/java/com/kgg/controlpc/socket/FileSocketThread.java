package com.kgg.controlpc.socket;

import android.os.Handler;

import java.io.File;

public abstract class FileSocketThread {
    private String SERVICE_IP;
    private int SERVICE_PORT;
    private Handler handler;
    private File file;
    private long fileSize;
    long progress = 0;

    public abstract void transferFile() throws Exception;
}
