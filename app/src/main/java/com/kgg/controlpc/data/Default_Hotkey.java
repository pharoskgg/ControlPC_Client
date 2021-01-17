package com.kgg.controlpc.data;

import java.util.ArrayList;

public class Default_Hotkey {
    static ArrayList<HotKeyData> hotKeyData = new ArrayList<>();
    public static ArrayList<HotKeyData> getHotkeyList(){
        hotKeyData.clear();
        hotKeyData.add(new HotKeyData("切换程序", "VK_ALT+VK_TAB"));
        hotKeyData.add(new HotKeyData("退出当前程序", "VK_ALT+VK_F4"));

        hotKeyData.add(new HotKeyData("窗口最大化", "VK_ALT+VK_SPACE+VK_X"));
        hotKeyData.add(new HotKeyData("窗口最小化", "VK_ALT+VK_SPACE+VK_N"));
        return hotKeyData;
    }
}
