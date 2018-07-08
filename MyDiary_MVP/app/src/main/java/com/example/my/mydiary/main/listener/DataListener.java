package com.example.my.mydiary.main.listener;

import com.example.my.mydiary.main.bean.Diary;

import java.util.List;

/**
 * @author my
 * @date 2018/7/5
 */
public interface DataListener {

    void onCompleted(List<Diary> diaryList);

    void onFailed();
}
