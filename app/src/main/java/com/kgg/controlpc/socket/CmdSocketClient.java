package com.kgg.controlpc.socket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.kgg.controlpc.activity.MousePadActivity;
import com.kgg.controlpc.config.SocketConfig;
import com.kgg.controlpc.operator.FileTransferBeginHandler;
import com.kgg.controlpc.operator.FileUploadHandler;
import com.kgg.controlpc.operator.MousePadHandler;
import com.kgg.controlpc.operator.ShowRemoteFileHandler;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

public class CmdSocketClient {
    private String ip;
    private int port;
    private ArrayList<String> cmd;
    private int time_out = 2000;
    private Handler handler;
    private Socket socket;
    private Activity activity;
    public static final String KEY_SERVER_ACK_MSG = "KEY_SERVER_ACK_MSG";
    public static final String KEY_SERVER_ABSOLUTE_PATH = "KEY_SERVER_ABSOLUTE_PATH";
    private OutputStreamWriter writer;
    private BufferedReader bufferedReader;

    public CmdSocketClient(String ip, int port,Activity activity, ShowRemoteFileHandler handler) {
        this.port = port;
        this.ip = ip;
        this.handler = handler;
        this.activity = activity;
    }

    public CmdSocketClient(String ip, int port, Activity activity, MousePadHandler mousePadHandler) {
        this.port = port;
        this.ip = ip;
        this.handler = mousePadHandler;
        this.activity = activity;
    }

    public CmdSocketClient(String ip, int port, Activity activity, FileTransferBeginHandler handler) {
        this.port = port;
        this.ip = ip;
        this.handler = handler;
        this.activity = activity;
    }

    public CmdSocketClient(String ip, int port, Activity activity, FileUploadHandler handler) {
        this.port = port;
        this.ip = ip;
        this.handler = handler;
        this.activity = activity;
    }

    private void connect() throws IOException {
        InetSocketAddress address = new InetSocketAddress(ip, port);
        socket = new Socket();
        socket.connect(address, time_out);
        System.out.println("连接成功");
    }

    private void writeCmd(String cmd) throws IOException {
        BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());
        writer = new OutputStreamWriter(os, "UTF-8");
        writer.write("1\n");
        writer.write(cmd + "\n");
        writer.flush();
        System.out.println("向服务端发送信息成功");
    }

    private ArrayList<String> readSocketMsg() throws PatternSyntaxException, IOException {
        ArrayList<String> msgList = new ArrayList<>();
        InputStreamReader isr = new InputStreamReader(socket.getInputStream(), SocketConfig.stringEncode);
        bufferedReader = new BufferedReader(isr);
        //第一行为后续还有几行信息
        int numLine = Integer.parseInt(bufferedReader.readLine());
        Log.e("e", numLine+"");
        for (int i = 0; i < numLine; i++)
            msgList.add(bufferedReader.readLine());
        Log.e("e", msgList.toString());
        return msgList;
    }

    private void close() throws IOException {
        bufferedReader.close();
        writer.close();
        socket.close();
    }

    private void doCmdTask(String cmd) throws Exception {
        ArrayList<String> msgList = new ArrayList<>();
        connect();
        writeCmd(cmd);
        msgList = readSocketMsg();
        close();//若中途抛出异常，此处是否会关闭socket连接
        sendMsg(msgList);
    }

    private void sendMsg(ArrayList<String> msgList) {
        Message message = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(KEY_SERVER_ACK_MSG, msgList);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    public void work(final String cmd) {
        final ArrayList<String> errorList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    doCmdTask(cmd);
                } catch (UnknownHostException e) {
                    showErrorInfo("客户端错误：未知主机/IP地址");
                    e.printStackTrace();
                } catch (SocketTimeoutException e) {
                    showErrorInfo("客户端错误：连接超时，请检查服务端是否开启/IP地址 or 端口 是否正确");
                    e.printStackTrace();
                } catch (Exception e) {
                    showErrorInfo("客户端未知错误："+e.toString());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showErrorInfo(final String errorInfo) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, errorInfo, Toast.LENGTH_SHORT).show();
            }
        });
    }
}