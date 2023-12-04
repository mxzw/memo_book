package com.bhp.memo_book.utils;


import android.content.Context;

public class AppProxy {

    private Context context;


    public void setContext(Context context) {
        this.context = context;
    }

    public static AppProxy getInstance() {
        return Singleton.appProxy;
    }

    public Context getContext() {
        return context;
    }

    private static class Singleton {
        private static AppProxy appProxy = new AppProxy();
    }

    private AppProxy() {
    }
}
