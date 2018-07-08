package com.example.my.mydiary.main.model;

import com.example.my.mydiary.application.MyApplication;
import com.example.my.mydiary.db.DatabaseDao;
import com.example.my.mydiary.main.bean.Diary;
import com.example.my.mydiary.main.listener.DataListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author my
 * @date 2018/7/5
 */
public class DiaryModelImpl implements DiaryModel {
    private DatabaseDao mDao;
    private List<Diary> mDiaryList = new ArrayList<>();

    @Override
    public void saveDiary(Diary diary) {
        // 保存到数据库
        mDao = DatabaseDao.getInstance(MyApplication.getContext());

        if (diary.getId() == -1) {
            mDao.insertDB(diary);
        } else {
            mDao.updateDB(diary);
        }
    }

    @Override
    public void loadDiary(DataListener listener) {
        // 从数据库读取数据
        mDao = DatabaseDao.getInstance(MyApplication.getContext());
        mDiaryList = mDao.selectDB(null);

        if (mDiaryList != null) {
            listener.onCompleted(mDiaryList);
        } else {
            listener.onFailed();
        }
    }

    @Override
    public void deleteDiary(int id) {
        mDao = DatabaseDao.getInstance(MyApplication.getContext());

        if (id == -1) {
            mDao.deleteAllDB();
        } else {
            mDao.deleteDB(id);
        }
    }
}
