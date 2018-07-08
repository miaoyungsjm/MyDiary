package com.example.my.mydiary.main.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.my.mydiary.R;
import com.example.my.mydiary.main.adapter.DiaryListAdapter;
import com.example.my.mydiary.main.bean.Diary;
import com.example.my.mydiary.main.handler.MainHandler;
import com.example.my.mydiary.main.listener.OnItemClickListener;
import com.example.my.mydiary.main.listener.OnItemLongClickListener;
import com.example.my.mydiary.main.presenter.DiaryPresenter;

import java.util.ArrayList;
import java.util.List;

import static com.example.my.mydiary.main.handler.MainHandler.DELETE_ALL_DIARY;
import static com.example.my.mydiary.main.handler.MainHandler.DELETE_ONE_DIARY;

/**
 * @author my
 */
public class MainActivity extends AppCompatActivity implements DiaryViewInterface {
    public static final int REQUEST_CODE_UPDATE = 0;
    public static final String KEY_BUNDLE = "bundle";
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_PUB_DATE = "pubDate";


    private FloatingActionButton mFloatingActionBtn;

    private List<Diary> mDiaryList = new ArrayList<>();
    private DiaryListAdapter mAdapter;

    public DiaryPresenter mPresenter;

    private MainHandler mHandler = new MainHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initRecyclerView();

        // 构建 Present, 与 Activity 建立关联
        mPresenter = new DiaryPresenter(this);
        // 请求数据
        mPresenter.fetchData();
    }

    private void initView() {
        // ToolBar （代替 ActionBar）
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // FloatingActionBtn
        mFloatingActionBtn = findViewById(R.id.floating_action_btn);
        mFloatingActionBtn.setOnClickListener(mOnClickListener);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new DiaryListAdapter(mDiaryList);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 页面跳转
                Diary diary = mDiaryList.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt(KEY_ID, diary.getId());
                bundle.putString(KEY_TITLE, diary.getTitle());
                bundle.putString(KEY_CONTENT, diary.getContent());
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(KEY_BUNDLE, bundle);
                startActivityForResult(intent, REQUEST_CODE_UPDATE);
            }
        });
        mAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                // 显示删除提示框
                showDeleteDialog(view, position);
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.floating_action_btn:
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_UPDATE);
                    break;

                default:
            }
        }
    };

    /**
     * 初始化 toolbar - item
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_add:
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(intent, REQUEST_CODE_UPDATE);
                break;

            case R.id.toolbar_delete_all:
                // 延时发送消息，更新数据库
                Message msg = mHandler.obtainMessage(DELETE_ALL_DIARY);
                mHandler.sendMessageDelayed(msg, 5000);
                // 显示撤销控件，供用户撤销操作
                showSnackBar(mFloatingActionBtn, DELETE_ALL_DIARY);

                // 临时刷新布局
                mDiaryList.clear();
                mAdapter.notifyDataSetChanged();
                break;

            case R.id.toolbar_about:
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                break;

            default:
        }
        return true;
    }

    @Override
    public void showData(List<Diary> diaryList) {
        mDiaryList.clear();
        mDiaryList.addAll(diaryList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_UPDATE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getBundleExtra(KEY_BUNDLE);
                    if (bundle != null) {
                        int id = bundle.getInt(KEY_ID, -1);
                        String title = bundle.getString(KEY_TITLE, null);
                        String content = bundle.getString(KEY_CONTENT, null);
                        String pubDate = bundle.getString(KEY_PUB_DATE, null);
                        Diary diary = new Diary(id, title, content, pubDate);

                        // 保存数据
                        mPresenter.saveData(diary);
                        // 重新请求数据
                        mPresenter.fetchData();
                    }
                }
                break;

            default:
        }
    }

    public void showDeleteDialog(final View v, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_delete)
                .setTitle(R.string.delete_one_diary_title)
                .setMessage(R.string.delete_one_diary_info)
                .setPositiveButton(R.string.delete_one_diary_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        // 延时发送消息，更新数据库
                        Message msg = mHandler.obtainMessage(DELETE_ONE_DIARY);
                        Bundle bundle = new Bundle();
                        bundle.putInt(KEY_ID, mDiaryList.get(position).getId());
                        msg.setData(bundle);
                        mHandler.sendMessageDelayed(msg, 5000);
                        // 显示撤销控件，供用户撤销操作
                        showSnackBar(v, DELETE_ONE_DIARY);

                        // 临时刷新布局
                        mDiaryList.remove(position);
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.delete_one_diary_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    public void showSnackBar(View v, final int msgWhat) {
        Snackbar.make(v, " - Delete Diary - ", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 清除 Handler Message
                        mHandler.removeMessages(msgWhat);
                        // 重新请求数据
                        mPresenter.fetchData();

                        Toast.makeText(MainActivity.this, "Undo", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }
}
