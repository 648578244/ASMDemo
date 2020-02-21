package com.example.myapplication;

import android.util.Log;

public class ProxyC implements IFactoryA {
    private IFactoryA factoryA;
    @Override
    public void saleMainTools(String size) {
        doBefore();
        factoryA.saleMainTools(size);
        doAfter();

    }

    private void doAfter() {
        Log.d("TAG","代理C收集用户反馈");

    }

    private void doBefore() {
        Log.d("TAG","代理C收集资料定制方案");

    }

    public void setFactoryA(IFactoryA factoryA) {
        this.factoryA = factoryA;
    }
}
