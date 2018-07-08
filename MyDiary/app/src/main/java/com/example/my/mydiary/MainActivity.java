package com.example.my.mydiary;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.my.mydiary.adapter.DiaryListAdapter;
import com.example.my.mydiary.bean.Diary;
import com.example.my.mydiary.db.DiaryManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int TOOLBAR_ITEM_ADD = 0;
    public static final int TOOLBAR_ITEM_DELETE_ALL = 1;
    public static final int TOOLBAR_ITEM_ABOUT = 2;
    public static final int UPDATE_ONE_DIARY = 3;
    public static final int DELETE_ONE_DIARY = 4;

    public static final int REQUEST_CODE_UPDATE = 0;

    public static final String KEY_BUNDLE = "bundle";
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";

    private FloatingActionButton mFloatingActionBtn;

    private List<Diary> mDiaryList;
    private DiaryManager mDiaryManager;
    private DiaryListAdapter mAdapter;


    /**
     * Handler - 消息队列
     * 实现：
     * （1）接收消息，加入队列，然后处理消息，实现数据库或跳转操作
     * （2）使用延迟消息，添加误删操作，实现撤销功能
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TOOLBAR_ITEM_ADD:
                    Intent intent0 = new Intent(MainActivity.this, EditActivity.class);
                    startActivityForResult(intent0, REQUEST_CODE_UPDATE);
                    break;

                case TOOLBAR_ITEM_DELETE_ALL:
                    // 删除数据库中的数据
                    mDiaryManager.deleteAllDB();
                    Toast.makeText(MainActivity.this, "Delete All", Toast.LENGTH_SHORT).show();
                    break;

                case TOOLBAR_ITEM_ABOUT:
                    Toast.makeText(MainActivity.this, "About", Toast.LENGTH_SHORT).show();
                    break;

                case UPDATE_ONE_DIARY:
                    Bundle bundle1 = msg.getData();
                    Intent intent1 = new Intent(MainActivity.this, EditActivity.class);
                    intent1.putExtra(KEY_BUNDLE, bundle1);
                    startActivityForResult(intent1, REQUEST_CODE_UPDATE);
                    break;

                case DELETE_ONE_DIARY:
                    Bundle bundle = msg.getData();
                    int id = bundle.getInt(KEY_ID);
                    mDiaryManager.deleteDB(id);
                    Toast.makeText(MainActivity.this, "Delete One", Toast.LENGTH_SHORT).show();
                    break;

                default:
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化控件
        initView();

        // 初始化数据
        initData();

        // 初始化列表
        initRecyclerView();
    }


    /**
     * 初始化控件
     */
    private void initView() {
        // ToolBar （代替 ActionBar）
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // FloatingActionBtn
        mFloatingActionBtn = findViewById(R.id.floating_action_btn);
        mFloatingActionBtn.setOnClickListener(this);
    }


    /**
     * 初始化数据
     */
    private void initData() {
        mDiaryManager = new DiaryManager(this);
        mDiaryList = mDiaryManager.selectDB(null);
    }


    /**
     * 初始化列表
     */
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        // 瀑布流
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new DiaryListAdapter(this, mDiaryList, mHandler);
        recyclerView.setAdapter(mAdapter);
    }


    /**
     * 点击事件
     * 响应：setOnClickListener(this)
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_action_btn:
                mHandler.sendEmptyMessage(TOOLBAR_ITEM_ADD);
                break;

            default:
        }
    }


    /**
     * 初始化 toolbar 的 item
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_item, menu);
        return true;
    }


    /**
     * toolbar 的选择
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_add:
                mHandler.sendEmptyMessage(TOOLBAR_ITEM_ADD);
                break;

            case R.id.toolbar_delete_all:
                // 备份原列表
                final List<Diary> list = new ArrayList<>();
                list.addAll(mDiaryList);

                // 更新列表
                mDiaryList.clear();
                mAdapter.notifyDataSetChanged();

                // 延时发送消息，更新数据库
                mHandler.sendEmptyMessageDelayed(TOOLBAR_ITEM_DELETE_ALL, 5000);
                // 显示撤销控件，供用户撤销操作
                Snackbar.make(mFloatingActionBtn, " - Delete All - ", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 点击撤销，清空 mHandler 消息，恢复备份
                                mHandler.removeMessages(TOOLBAR_ITEM_DELETE_ALL);
                                mDiaryList.addAll(list);
                                mAdapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Undo",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                break;

            case R.id.toolbar_about:
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                break;

            default:
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_UPDATE:
                if (resultCode == RESULT_OK) {
                    // 重置列表
                    mDiaryList = mDiaryManager.selectDB(null);
                    initRecyclerView();
                }
                break;

            default:
        }
    }
}
