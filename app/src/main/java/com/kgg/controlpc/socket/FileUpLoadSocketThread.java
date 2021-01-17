package com.kgg.controlpc.socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class FileUpLoadSocketThread {
    private String SERVICE_IP;
    private int SERVICE_PORT;
    private Handler handler;
    private File file;
    private long fileSize;
    long progress = 0;

    public FileUpLoadSocketThread(String ip, int port, Handler handler, File file, long fileSize) {
        this.SERVICE_IP = ip;
        this.SERVICE_PORT = port;
        this.handler = handler;
        this.file = file;
        this.fileSize = fileSize;
    }

    public void start() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[1024 * 8];
                int length = 0;

                Timer timer = new Timer();
                FileUpLoadSocketThread.OneTask oneTask = new FileUpLoadSocketThread.OneTask();
                timer.schedule(oneTask, 500, 500);

                final byte[] bytes = new byte[1024 * 8];
                InetSocketAddress address = new InetSocketAddress(SERVICE_IP, SERVICE_PORT);
                Socket socket = new Socket();
                try {
                    socket.connect(address, 10);

                    //文件的输出流
                    FileInputStream fis = new FileInputStream(file);
//                    fis.skip(filePos);
                    //socket的输出流
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                    while ((length = fis.read(buffer)) != -1){
                        dos.write(buffer, 0, length);
                        progress += length;
                        dos.flush();
                    }

                    fis.close();
                    dos.close();
                    socket.close();

                    System.out.println("文件上传成功");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("连接成功");
            }
        }).start();
    }

    class OneTask extends TimerTask {
        @Override
        public void run() {
            Message message = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putInt("progress", (int) ((progress * 100 / fileSize)));
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }
}
