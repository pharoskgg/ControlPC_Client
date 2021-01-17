package com.kgg.controlpc.operator;

import com.kgg.controlpc.data.Default_Hotkey;
import com.kgg.controlpc.data.HotKeyData;
import com.kgg.controlpc.data.NetFileData;
import com.kgg.controlpc.data.PPT_HotKey;

import java.util.ArrayList;

public class HotKeyGenerator {
    public static ArrayList<HotKeyData> getHotkeyList(NetFileData fileData){
        String[] split = fileData.getFileName().split("\\.");
        int index = split.length - 1;
        String extension = split[index];
        if ("ppt".equalsIgnoreCase(extension) || "pptx".equalsIgnoreCase(extension))
            return PPT_HotKey.getHotkeyList();
        else
            return Default_Hotkey.getHotkeyList();
    }
}
