package com.example.myapplication;

import android.util.Log;

public class FactoryA implements IFactoryA {
    @Override
    public void saleMainTools(String size) {
        Log.d("TAG","A->size:"+size);
    }
}
