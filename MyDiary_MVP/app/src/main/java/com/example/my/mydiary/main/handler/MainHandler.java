package com.example.my.mydiary.main.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.my.mydiary.main.view.MainActivity;

import java.lang.ref.WeakReference;

import static com.example.my.mydiary.main.view.MainActivity.KEY_ID;

/**
 * @author my
 * @date 2018/7/8
 */
public class MainHandler extends Handler {
    public static final int DELETE_ONE_DIARY = 1;
    public static final int DELETE_ALL_DIARY = 2;

    private WeakReference<MainActivity> mWeakReference;

    public MainHandler(MainActivity activity) {
        super();
        mWeakReference = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        MainActivity activity = mWeakReference.get();
        if (activity != null) {
            switch (msg.what) {
                case DELETE_ONE_DIARY:
                    Bundle bundle = msg.getData();
                    int id = bundle.getInt(KEY_ID);
                    activity.mPresenter.deleteData(id);
                    break;

                case DELETE_ALL_DIARY:
                    activity.mPresenter.deleteData(-1);
                    break;

                default:
            }
        }
    }
}
