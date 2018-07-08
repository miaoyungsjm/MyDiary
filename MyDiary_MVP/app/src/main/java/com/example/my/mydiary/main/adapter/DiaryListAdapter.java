package com.example.my.mydiary.main.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my.mydiary.R;
import com.example.my.mydiary.main.bean.Diary;
import com.example.my.mydiary.main.listener.OnItemClickListener;
import com.example.my.mydiary.main.listener.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * @author my
 */
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


    private List<Diary> mDiaryList;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    /**
     * 构造函数
     */
    public DiaryListAdapter(List<Diary> mDiaryList) {
        this.mDiaryList = mDiaryList;
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
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
                if (mOnItemClickListener != null) {
                    int position = holder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });

        // 长按事件
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                if (mOnItemLongClickListener != null) {
                    int position = holder.getAdapterPosition();
                    mOnItemLongClickListener.onItemLongClick(v, position);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDiaryList.size();
    }

    /**
     * 绑定
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }
}
