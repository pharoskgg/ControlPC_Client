package com.kgg.controlpc.data;

import java.util.ArrayList;

public class PPT_HotKey {
    static ArrayList<HotKeyData> hotKeyData = new ArrayList<>();
    public static ArrayList<HotKeyData> getHotkeyList(){
        hotKeyData.clear();
        hotKeyData.add(new HotKeyData("切换程序", "VK_ALT+VK_TAB"));
        //退出放映
        hotKeyData.add(new HotKeyData("ESC", "VK_ESCAPE"));
        //下一页
        hotKeyData.add(new HotKeyData("下一页", "VK_DOWN"));
        //上一页
        hotKeyData.add(new HotKeyData("上一页", "VK_UP"));
        hotKeyData.add(new HotKeyData("从头放映", "VK_F5"));
        hotKeyData.add(new HotKeyData("从当前开始放映", "VK_SHIFT+VK_F5"));
        hotKeyData.add(new HotKeyData("退出当前程序", "VK_ALT+VK_F4"));
        hotKeyData.add(new HotKeyData("黑屏/正常", "VK_B"));
        return hotKeyData;
    }
}
