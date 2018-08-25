package com.example.my.mydiary.eventbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.my.mydiary.R;
import com.example.my.mydiary.dialog.MyDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author ggz
 * @date 18年8月13日
 */
public class FirstActivity extends AppCompatActivity {
    private final String TAG = FirstActivity.class.getSimpleName();
    private final String TAG_EVENT_BUS = "EVENT_BUS";

    private EventBus mEventBus;
    private String mMsg = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG_EVENT_BUS, "onCreate() 1");
        setContentView(R.layout.activity_first);

        mEventBus = EventBus.getDefault();
        mEventBus.register(this);

        Button jumpBtn = findViewById(R.id.btn_jump);
        jumpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        showDialog("show");
    }

    @Override
    protected void onRestart() {
        Log.d(TAG_EVENT_BUS, "onRestart() 1");
        super.onRestart();

        if (!mMsg.isEmpty()) {
            showDialog(mMsg);
            mMsg = null;
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG_EVENT_BUS, "onPause() 1");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG_EVENT_BUS, "onStop() 1" +
                " isFinishing() = " + isFinishing() +
                " mEventBus.isRegistered(this) = " + mEventBus.isRegistered(this));
        super.onStop();
        if (isFinishing() && mEventBus.isRegistered(this)) {
            mEventBus.unregister(this);
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG_EVENT_BUS, "onDestroy() 1" +
                " mEventBus.isRegistered(this) = " + mEventBus.isRegistered(this));
        super.onDestroy();
        if (mEventBus.isRegistered(this)) {
            mEventBus.unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventThread(MessageEvent msgEvent) {
        Log.d(TAG_EVENT_BUS, "onEventThread() 1" +
                " msgEvent.getMessage() " + msgEvent.getMessage());
        mMsg = msgEvent.getMessage();
    }

    private void showDialog(String msg) {

        MyDialogFragment dialog = new MyDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("msg", msg);
        dialog.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialog.show(fm, "DialogFragment");
    }
}
