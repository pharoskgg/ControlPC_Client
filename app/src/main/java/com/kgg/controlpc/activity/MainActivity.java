package com.kgg.controlpc.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kgg.controlpc.R;
import com.kgg.controlpc.config.CmdServerIpSetting;
import com.kgg.controlpc.data.NetFileData;
import com.kgg.controlpc.operator.FileTransferBeginHandler;
import com.kgg.controlpc.operator.FileUploadHandler;
import com.kgg.controlpc.operator.HotKeyGenerator;
import com.kgg.controlpc.operator.ShowRemoteFileHandler;
import com.kgg.controlpc.socket.CmdSocketClient;
import com.kgg.controlpc.view.HotkeyDialog;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    private EditText et_host;
    private EditText et_port;
    private EditText et_msg;
    private ArrayList<NetFileData> fileList;
    private ShowRemoteFileHandler showRemoteFileHandler;
    //记录文件栈
    public static String fileAbsolotePath = "\\";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt_send = findViewById(R.id.bt_send);


        lv = findViewById(R.id.lv);
        et_host = findViewById(R.id.et_host);
        et_port = findViewById(R.id.et_port);
        et_msg = findViewById(R.id.et_msg);

        fileList = new ArrayList<>();

        showRemoteFileHandler = new ShowRemoteFileHandler(this, lv);
        registerForContextMenu(lv);

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = et_host.getText().toString();
                int port = Integer.parseInt(et_port.getText().toString());
                CmdServerIpSetting.ip=ip;
                CmdServerIpSetting.port=port;
                try {
                    new CmdSocketClient(CmdServerIpSetting.ip, CmdServerIpSetting.port,MainActivity.this, showRemoteFileHandler).work(et_msg.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NetFileData file = (NetFileData) parent.getItemAtPosition(position);
                String pwd = file.getFilePath();
                String filePath="";
                if(pwd.endsWith("/")|pwd.endsWith("\\")){
                    //文件路径可能带"/"结尾，例如"c://aaa/b/"也可能是"c://aaa/b"因此需要考虑周全
                    //另外Windows系统和Linux系统文件夹分隔符不同，对有些系统其文件目录的表示是"c:\\\\aaa\\b\\"，注意"\\"转义成"\"
                    filePath=pwd+file.getFileName();
                }else{
                    filePath=pwd+ File.separator+file.getFileName();
                }
                if (file.getFileType() == 1 || file.getFileType() == 2){
                    if (file.getFileName().equals("...")){
                        filePath = "...";
                    }
                    new CmdSocketClient(CmdServerIpSetting.ip, CmdServerIpSetting.port, MainActivity.this, showRemoteFileHandler).work("dir:" + filePath);
                }
//                else {
//                    new CmdSocketClient(CmdServerIpSetting.ip, CmdServerIpSetting.port, MainActivity.this, showRemoteFileHandler).work("opn:" + filePath);
//                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opt_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.opt_mousepad:
                Intent mousepad_intent = new Intent(MainActivity.this, MousePadActivity.class);
                startActivity(mousepad_intent);
                break;
            case R.id.opt_return:
                return_previe_director();
                break;
            case R.id.opt_upload:
                upload_file();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //上传文件
    private void upload_file() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), FileBrowsActivity.class);
        startActivityForResult(intent, 1);
    }


    //返回上一级目录
    private void return_previe_director() {
        String[] split = fileAbsolotePath.split("\\\\");
        if (split.length > 1){
            String filePath = "";
            for (int i = 0; i < split.length - 1; i++){
                filePath += split[i] + "\\";
            }
            new CmdSocketClient(CmdServerIpSetting.ip, CmdServerIpSetting.port, MainActivity.this, showRemoteFileHandler).work("dir:" + filePath);
        } else{
            new CmdSocketClient(CmdServerIpSetting.ip, CmdServerIpSetting.port, MainActivity.this, showRemoteFileHandler).work("dir:...");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            String filePath = data.getExtras().getString("filePath");
            if (!"".equals(filePath)){
                String[] split = filePath.split("\\/");
                new CmdSocketClient(CmdServerIpSetting.ip, CmdServerIpSetting.port, MainActivity.this,
                        new FileUploadHandler(this, filePath)).work("ulf:" + split[split.length -1]);
//                Toast.makeText(this, data.getExtras().getString("filePath"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.file_list_context_menu, menu);//R.menu.file_list_context_menu为上下文菜单
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        // TODO Auto-generated method stub
        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos=contextMenuInfo.position;
        NetFileData netFileData=(NetFileData) lv.getAdapter().getItem(pos);//其中listView为显示文件列表的视图
        switch(item.getItemId()){
            case R.id.menu_hotkey:// 弹出热键对话框
                showHotKeyDialog(netFileData);//能根据netFileData类型决定弹出相应的热键对话框
                break;
            case R.id.menu_opn:
                String filePath = netFileData.getFilePath() + File.separator + netFileData.getFileName();
                new CmdSocketClient(CmdServerIpSetting.ip, CmdServerIpSetting.port, MainActivity.this, showRemoteFileHandler).work("opn:" + filePath);
                break;
            case R.id.menu_dlf:
                downloadFile(netFileData);
                break;
            default :break;
        }
        return super.onContextItemSelected(item);
    }

    public void showHotKeyDialog(NetFileData netFileData){
        CmdSocketClient cmdSocketClient = new CmdSocketClient(CmdServerIpSetting.ip, CmdServerIpSetting.port, MainActivity.this, showRemoteFileHandler);
        new HotkeyDialog(MainActivity.this, HotKeyGenerator.getHotkeyList(netFileData), "热键操作表", cmdSocketClient).show();
    }

    private void downloadFile(NetFileData netFileData) {
        if (netFileData.getFileType() == 0){
            new CmdSocketClient(CmdServerIpSetting.ip, CmdServerIpSetting.port, MainActivity.this,
                    new FileTransferBeginHandler(this, netFileData)).work("dlf:" + netFileData.getFilePath() +
                    File.separator + netFileData.getFileName());
        }else {
            Toast.makeText(this, "暂未提供整个文件夹下载的功能", Toast.LENGTH_SHORT).show();
        }
    }
}
