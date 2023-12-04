package com.bhp.memo_book;

import android.app.Application;
import android.content.Context;

import com.bhp.memo_book.utils.AppProxy;

public class MainApplication extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AppProxy.getInstance().setContext(base);
    }
}
