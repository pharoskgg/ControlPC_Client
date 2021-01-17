package com.kgg.controlpc.utility;

import android.os.Environment;

import java.io.File;

public class CheckLocalDownloadFolder {
    public static String check(){
        String filePath = Environment.getExternalStorageDirectory().getPath() + "/1/";//这是sd卡的目录
        File file = new File(filePath);
        if (!file.exists())
            file.mkdirs();
        return file.getAbsolutePath();
    }
}
