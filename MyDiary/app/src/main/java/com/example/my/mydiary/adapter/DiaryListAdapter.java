package com.example.my.mydiary.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my.mydiary.EditActivity;
import com.example.my.mydiary.MainActivity;
import com.example.my.mydiary.R;
import com.example.my.mydiary.bean.Diary;

import java.util.ArrayList;
import java.util.List;

import static com.example.my.mydiary.MainActivity.DELETE_ONE_DIARY;
import static com.example.my.mydiary.MainActivity.KEY_CONTENT;
import static com.example.my.mydiary.MainActivity.KEY_ID;
import static com.example.my.mydiary.MainActivity.KEY_TITLE;
import static com.example.my.mydiary.MainActivity.TOOLBAR_ITEM_DELETE_ALL;
import static com.example.my.mydiary.MainActivity.UPDATE_ONE_DIARY;

public class DiaryListAdapter extends RecyclerView.Adapter<DiaryListAdapter.MyViewHolder> {

    static class MyViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView itemTitle;
        TextView itemContent;
        TextView itemPubDate;

        public MyViewHolder(View v) {
            super(v);
            itemView = v;
            itemTitle = v.findViewById(R.id.item_title);
            itemContent = v.findViewById(R.id.item_content);
            itemPubDate = v.findViewById(R.id.item_pub_date);
        }
    }

    private Context mContext;
    private List<Diary> mDiaryList;
    private Handler mHandler;

    public DiaryListAdapter(Context mContext, List<Diary> mDiaryList, Handler mHandler) {
        this.mContext = mContext;
        this.mDiaryList = mDiaryList;
        this.mHandler = mHandler;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // 填充数据
        final Integer id = mDiaryList.get(position).getId();
        final String title = mDiaryList.get(position).getTitle();
        final String content = mDiaryList.get(position).getContent();
        String pubDate = mDiaryList.get(position).getPubDate();
        holder.itemTitle.setText(title);
        holder.itemContent.setText(content);
        holder.itemPubDate.setText(pubDate);

        // 点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message msg = mHandler.obtainMessage(UPDATE_ONE_DIARY);
                Bundle bundle = new Bundle();
                bundle.putInt(KEY_ID, id);
                bundle.putString(KEY_TITLE, title);
                bundle.putString(KEY_CONTENT, content);
                msg.setData(bundle);

                mHandler.sendMessage(msg);
            }
        });

        // 长按事件
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                // 删除提示框
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setIcon(android.R.drawable.ic_menu_delete)
                        .setTitle(R.string.delete_one_diary_title)
                        .setMessage(R.string.delete_one_diary_info)
                        .setPositiveButton(R.string.delete_one_diary_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 备份原列表
                                final List<Diary> list = new ArrayList<>();
                                list.addAll(mDiaryList);

                                // 更新列表
                                mDiaryList.remove(position);
                                notifyDataSetChanged();

                                // 延时发送消息，更新数据库
                                Message msg = mHandler.obtainMessage(DELETE_ONE_DIARY);
                                Bundle bundle = new Bundle();
                                bundle.putInt(KEY_ID, id);
                                msg.setData(bundle);
                                mHandler.sendMessageDelayed(msg, 5000);

                                // 显示撤销控件，供用户撤销操作
                                Snackbar.make(v, " - Delete One Diary - ", Snackbar.LENGTH_LONG)
                                        .setAction("Undo", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // 点击撤销，清空 mHandler 消息，恢复备份
                                                mHandler.removeMessages(DELETE_ONE_DIARY);
                                                mDiaryList.clear();
                                                mDiaryList.addAll(list);
                                                notifyDataSetChanged();
                                                Toast.makeText(mContext, "Undo",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }).show();
                            }
                        })
                        .setNegativeButton(R.string.delete_one_diary_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDiaryList.size();
    }
}
