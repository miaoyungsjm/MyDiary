package com.example.my.mydiary.application;

import android.app.Application;
import android.content.Context;

/**
 * @author my
 * @date 2018/7/7
 */
public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
