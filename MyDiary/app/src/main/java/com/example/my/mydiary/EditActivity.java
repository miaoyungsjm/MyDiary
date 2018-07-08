package com.example.my.mydiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.my.mydiary.bean.Diary;
import com.example.my.mydiary.db.DiaryManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.my.mydiary.MainActivity.KEY_BUNDLE;
import static com.example.my.mydiary.MainActivity.KEY_CONTENT;
import static com.example.my.mydiary.MainActivity.KEY_ID;
import static com.example.my.mydiary.MainActivity.KEY_TITLE;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mTitleEt;
    private EditText mContentEt;

    private Integer mId = -1;
    private String mTitle = null;
    private String mContent = null;

    private DiaryManager mDiaryManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // 提取参数
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(KEY_BUNDLE);
        if (bundle != null) {
            mId = bundle.getInt(KEY_ID, -1);
            mTitle = bundle.getString(KEY_TITLE, null);
            mContent = bundle.getString(KEY_CONTENT, null);
        }


        // 初始化控件
        initView();

        // 数据管理
        mDiaryManager = new DiaryManager(this);
    }

    private void initView() {
        mTitleEt = findViewById(R.id.et_title);
        mContentEt = findViewById(R.id.et_content);

        if (mTitle != null) {
            mTitleEt.setText(mTitle);
        }

        if (mContent != null) {
            mContentEt.setText(mContent);
        }

        Button saveBtn = findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:

                Diary diary = new Diary(
                        mId,
                        mTitleEt.getText().toString(),
                        mContentEt.getText().toString(),
                        getCurrentTime(new Date()));

                if (mId == -1) {
                    // 没有记录，插入数据库
                    mDiaryManager.insertDB(diary);

                } else {
                    // 有记录，修改数据库
                    mDiaryManager.updateDB(diary);
                }

                // 销毁 Activity ，并触发回调，刷新瀑布流列表
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;

            default:
        }
    }

    public String getCurrentTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy年MM月dd日hh时mm分ss秒");
        return simpleDateFormat.format(date);
    }
}
