package com.kgg.controlpc.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kgg.controlpc.R;
import com.kgg.controlpc.data.HotKeyData;
import com.kgg.controlpc.socket.CmdSocketClient;

import java.util.ArrayList;

public class HotkeyGridAdapter extends ArrayAdapter<HotKeyData> {
    private Context context;
    private ArrayList<HotKeyData> list;
    CmdSocketClient cmdSocketClient;

    public HotkeyGridAdapter(@NonNull Context context, ArrayList<HotKeyData> list, CmdSocketClient cmdSocketClient) {
        super(context, android.R.layout.simple_list_item_1, list);
        this.context = context;
        this.list = list;
        this.cmdSocketClient = cmdSocketClient;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.hotkey_row_view, null, false);
        TextView textView = convertView.findViewById(R.id.hotkey_name);
        HotKeyData hotKeyData = list.get(position);
        textView.setText(hotKeyData.getHotkeyName());
        final String cmd = hotKeyData.getHotkeyCmd();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdSocketClient.work("key:" + cmd);
            }
        });
        return convertView;
    }
}
