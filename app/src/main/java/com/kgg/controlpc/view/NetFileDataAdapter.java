package com.kgg.controlpc.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kgg.controlpc.R;
import com.kgg.controlpc.data.NetFileData;

import java.text.SimpleDateFormat;
import java.util.List;

public class NetFileDataAdapter extends ArrayAdapter<NetFileData> {
    private List<NetFileData> fileList;
    private Context context;

    public NetFileDataAdapter(@NonNull Context context, List<NetFileData> list) {
        super(context, android.R.layout.simple_list_item_1, list);
        this.context = context;
        this.fileList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.file_attribute_row_view, null, false);
        ImageView imageView = convertView.findViewById(R.id.file_row_view_imageView);
        TextView tv_fileName = convertView.findViewById(R.id.file_row_view_fileName);
        TextView tv_fileSize = convertView.findViewById(R.id.file_row_view_fileSize);
        TextView tv_fileModifytime = convertView.findViewById(R.id.file_row_view_modifytime);

        NetFileData file = fileList.get(position);
        switch (file.getFileType()){
            case 0:
                toChoiceImg(file.getFileName(), imageView);
                break;
            case 1:
                imageView.setImageResource(R.drawable.document);
                break;
            case 2:
                imageView.setImageResource(R.drawable.windrive);
                break;
        }

        tv_fileName.setText(file.getFileName());

        if (file.getFileType() == 0){
            String fileSize;
            long size = file.getFileSize();
            if (size < 1024 ){//B
                fileSize = String.format("%.2fB", size / 1.0);
            } else if (size >= 1024 && size < 1048576){//KB
                fileSize = String.format("%.2fKB", size / (1024.0));
            }else if (size >= 1048576 && size < 1073741824){//MB->1024*1024
                fileSize = String.format("%.2fMB", size / (1048576.0));
            }else {//G
                fileSize = String.format("%.2fGB", size / (1048576.0 * 1024));
            }
            tv_fileSize.setText(fileSize);
            tv_fileSize.setVisibility(View.VISIBLE);
        } else {
            tv_fileSize.setVisibility(View.GONE);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeFormat = dateFormat.format(file.getFileDate());
        tv_fileModifytime.setText(timeFormat);
        return convertView;
    }

    private void toChoiceImg(String fileName, ImageView imageView) {
        String[] fileNameSplit = new String[0];
        try {
            fileNameSplit = fileName.split("\\.");
            String suffix = fileNameSplit[fileNameSplit.length - 1];
            if ("avi".equalsIgnoreCase(suffix))
                imageView.setImageResource(R.drawable.avi);
            else if ("doc".equalsIgnoreCase(suffix) || "rtf".equalsIgnoreCase(suffix) || "docx".equalsIgnoreCase(suffix))
                imageView.setImageResource(R.drawable.doc);
            else if ("exe".equalsIgnoreCase(suffix))
                imageView.setImageResource(R.drawable.exe);
            else if ("png".equalsIgnoreCase(suffix) || "jpg".equalsIgnoreCase(suffix))
                imageView.setImageResource(R.drawable.img);
            else if ("mp3".equalsIgnoreCase(suffix) || "mp4".equalsIgnoreCase(suffix))
                imageView.setImageResource(R.drawable.music);
            else if ("pdf".equalsIgnoreCase(suffix))
                imageView.setImageResource(R.drawable.pdf);
            else if ("ppt".equalsIgnoreCase(suffix) || "pptx".equalsIgnoreCase(suffix))
                imageView.setImageResource(R.drawable.ppt);
            else if ("txt".equalsIgnoreCase(suffix))
                imageView.setImageResource(R.drawable.txt);
            else if ("xls".equalsIgnoreCase(suffix))
                imageView.setImageResource(R.drawable.xls);
            else if ("zip".equalsIgnoreCase(suffix) || "rar".equalsIgnoreCase(suffix) || "7z".equalsIgnoreCase(suffix) || "gz".equalsIgnoreCase(suffix))
                imageView.setImageResource(R.drawable.zip);
            else
                imageView.setImageResource(R.drawable.unknow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
