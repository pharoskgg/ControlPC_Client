package com.kgg.controlpc.activity;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.kgg.controlpc.R;
import com.kgg.controlpc.config.CmdServerIpSetting;
import com.kgg.controlpc.operator.MousePadHandler;
import com.kgg.controlpc.socket.CmdSocketClient;

public class MousePadActivity extends AppCompatActivity {
    private MousePadHandler mousePadHandler;
    private String cmd;
    private CmdSocketClient cmdClientSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse_pad);

        final GestureDetector mGestureDetector=new GestureDetector(this, new MousePadOnGestureListener());
        final GestureDetector whellGestureDetector = new GestureDetector(this, new MouseWheelOnGestureListener());

        mousePadHandler = new MousePadHandler(this);
        cmdClientSocket = new CmdSocketClient(CmdServerIpSetting.ip, CmdServerIpSetting.port, MousePadActivity.this, mousePadHandler);

        TextView mousePad = findViewById(R.id.mousepad);
        TextView mouseWheel = findViewById(R.id.mousewheel);
        Button bt_left = findViewById(R.id.mousepad_bt_left);
        Button bt_right = findViewById(R.id.mousepad_bt_right);
        ToggleButton toggleButton = findViewById(R.id.mousepad_bt_toggle);

        bt_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmd = "clk:left";
                cmdClientSocket.work(cmd);
            }
        });

        bt_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmd = "clk:right";
                cmdClientSocket.work(cmd);
            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    System.out.println("被锁定");
                    cmdClientSocket.work("clk:left_press");
                }else {
                    cmdClientSocket.work("clk:left_release");
                }
            }
        });

        mouseWheel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                whellGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        mousePad.setOnTouchListener(new TextView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    class MouseWheelOnGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            cmd = "rol:" + (int) (-distanceY * 0.3);
            new CmdSocketClient(CmdServerIpSetting.ip, CmdServerIpSetting.port,MousePadActivity.this, mousePadHandler).work(cmd);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    class MousePadOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        //手势处理接口，通过集成SimpleOnGestureListener改写对应手势方法
        @Override
        public boolean onDoubleTap(MotionEvent e) {//双击
            // TODO Auto-generated method stub
            cmd = "clk:left";
            cmdClientSocket.work(cmd);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {//滚动
            // TODO Auto-generated method stub
//            Log.d("disx:", "" + distanceX);
//            Log.d("disy:", "" + distanceY);
            cmd = "mov:" + (int) -distanceX + "," + (int) -distanceY;//手势方向与鼠标控制方向相反，对值取反
//            cmdClientSocket.work(cmd);//为何此种方式不行，会报错
            new CmdSocketClient(CmdServerIpSetting.ip, CmdServerIpSetting.port,MousePadActivity.this, mousePadHandler).work(cmd);
            return true;
        }
    }

    @Override
    protected void onPause() {
        new CmdSocketClient(CmdServerIpSetting.ip, CmdServerIpSetting.port,MousePadActivity.this, mousePadHandler).work("clk:left_release");
        super.onPause();
    }
}
