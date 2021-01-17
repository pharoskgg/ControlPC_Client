package com.kgg.controlpc.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.kgg.controlpc.R;
import com.kgg.controlpc.data.NetFileData;
import com.kgg.controlpc.view.NetFileDataAdapter;

import java.io.File;
import java.util.ArrayList;

public class FileBrowsActivity extends AppCompatActivity {
    private String currentPath = "";
    private NetFileData selectedFile;
    private ArrayList<NetFileData> fileList;
    NetFileDataAdapter lvAdapter;
    private String selectedFilePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_brows);

        final ListView lv = findViewById(R.id.file_brows_lv);
        Button bt_back = findViewById(R.id.file_brows_bt_back);
        Button bt_ok = findViewById(R.id.file_brows_bt_ok);
        final TextView tv_filePath = findViewById(R.id.file_brows_bt_tv_filePath);

        fileList = openBrowser(Environment.getExternalStorageDirectory().getPath());
        lvAdapter = new NetFileDataAdapter(this, fileList);
        lv.setAdapter(lvAdapter);
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
                    fileList.clear();
                    fileList.addAll(openBrowser(filePath));
                    lvAdapter.notifyDataSetChanged();
                } else if (file.getFileType() == 0){
                    selectedFilePath = filePath;
                    selectedFile = file;
                    tv_filePath.setText(selectedFilePath);
                }
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Environment.getExternalStorageDirectory().getPath().equals(currentPath)){
                    String[] split = currentPath.split("\\/");
                    if (split.length > 1){
                        String filePath = "";
                        for (int i = 0; i < split.length - 1; i++){
                            filePath += split[i] + "/";
                        }
                        fileList.clear();
                        fileList.addAll(openBrowser(filePath));
                        lvAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.putExtra("filePath", selectedFilePath);
//                intent.putExtra("netFile", selectedFile);
//                intent.pu
                setResult(1, intent);
                finish();
            }
        });
    }

    private ArrayList<NetFileData> openBrowser(String path){
        File direction = new File(path);
        File [] files = direction.listFiles();
        ArrayList<NetFileData> resultInfoList = new ArrayList<>();
        NetFileData fileAttribute = null;
        try {
            currentPath = getAbsolotePath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (File file : files) {
            Long date = file.lastModified();
            if (file.isDirectory())
                fileAttribute = new NetFileData(date, file.getName(), 1);
            else
                fileAttribute = new NetFileData(date, file.getName(), file.length(), 0);
            fileAttribute.setFilePath(currentPath);
            resultInfoList.add(fileAttribute);
        }
        return resultInfoList;
    }

    private String getAbsolotePath(String path) throws Exception{
        File file = new File(path);
        return file.getCanonicalPath();
    }
}
