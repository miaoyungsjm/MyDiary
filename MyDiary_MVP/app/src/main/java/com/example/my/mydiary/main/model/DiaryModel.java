package com.example.my.mydiary.main.model;

import com.example.my.mydiary.main.bean.Diary;
import com.example.my.mydiary.main.listener.DataListener;

/**
 * @author my
 * @date 2018/7/5
 */
public interface DiaryModel {

    void saveDiary(Diary diary);

    void loadDiary(DataListener listener);

    void deleteDiary(int id);

}
