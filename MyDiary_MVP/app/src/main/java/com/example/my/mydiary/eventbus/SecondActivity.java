package com.example.my.mydiary.eventbus;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.my.mydiary.R;

import org.greenrobot.eventbus.EventBus;

/**
 * @author ggz
 * @date 18年8月13日
 */

public class SecondActivity extends Activity {
    private final String TAG = SecondActivity.class.getSimpleName();
    private final String TAG_EVENT_BUS = "EVENT_BUS";

    private EventBus mEventBus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG_EVENT_BUS, "onCreate() 2");
        setContentView(R.layout.activity_second);

        mEventBus = EventBus.getDefault();

        Button sendBtn = findViewById(R.id.btn_send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEventBus.post(new MessageEvent(getResources().getString(R.string.second_send_msg)));
                finish();
            }
        });
    }

    @Override
    protected void onRestart() {
        Log.d(TAG_EVENT_BUS, "onRestart() 2");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.d(TAG_EVENT_BUS, "onPause() 2");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG_EVENT_BUS, "onStop() 2" +
                " isFinishing() = " + isFinishing());
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG_EVENT_BUS, "onDestroy() 2");
        super.onDestroy();
    }
}
