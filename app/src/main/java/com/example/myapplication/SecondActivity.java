package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IFactoryA factoryA  = new FactoryA();
        ProxyC proxyC = new ProxyC();
        proxyC.setFactoryA(factoryA);
        proxyC.saleMainTools("1");
        Button btn = findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick1");
            }
        });
        Button btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG, "onClick2");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = Message.obtain();
                        message.what = HANDLER_UI;
                        message.obj = "延时5秒：" + Thread.currentThread().getName();
                        handlerUI.sendMessageDelayed(message,5000);

                        Message message1 = Message.obtain();
                        message1.what = HANDLER_UI;
                        message1.obj = "延时2秒：" + Thread.currentThread().getName();
                        handlerUI.sendMessageDelayed(message1,2000);
                        Message message2 = Message.obtain();
                        message2.what = HANDLER_UI;
                        message2.obj = "延时4秒：" + Thread.currentThread().getName();
                        handlerUI.sendMessageDelayed(message2,4000);
                    }
                }).start();
            }
        });
    }
    private static final int HANDLER_UI = 1;
    private static final int HANDLER_UI2 = 2;

    public Handler handlerUI = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_UI:
//                    textView.setText(strData);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String strData = (String) msg.obj;

                    Log.d("TAG",strData);
                    ((Button)findViewById(R.id.btn2)).setText(strData);
                    break;
                default:
                    break;
            }
        }
    };

}
