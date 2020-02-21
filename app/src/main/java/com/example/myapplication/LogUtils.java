package com.example.myapplication;

import android.util.Log;
import android.view.View;

public class LogUtils {
    private static final String TAG = "LogUtils";
    public static void system(View v){
        Log.d(TAG, "ASM无痕插入的点");
    }
}