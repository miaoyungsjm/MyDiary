package com.example.my.mydiary.main.presenter;

import com.example.my.mydiary.main.bean.Diary;
import com.example.my.mydiary.main.listener.DataListener;
import com.example.my.mydiary.main.model.DiaryModel;
import com.example.my.mydiary.main.model.DiaryModelImpl;
import com.example.my.mydiary.main.view.DiaryViewInterface;

import java.util.List;

/**
 * @author my
 * @date 2018/7/5
 */
public class DiaryPresenter {

    /**
     * View 接口
     */
    private DiaryViewInterface mDiaryViewInterface;

    /**
     * Model
     */
    private DiaryModel mDiaryModel = new DiaryModelImpl();


    public DiaryPresenter(DiaryViewInterface diaryViewInterface) {
        mDiaryViewInterface = diaryViewInterface;
    }

    /**
     * 业务逻辑 - 获取数据
     */
    public void fetchData() {
        // 从数据库读取数据
        mDiaryModel.loadDiary(new DataListener() {
            @Override
            public void onCompleted(List<Diary> diaryList) {
                // 刷新界面
                mDiaryViewInterface.showData(diaryList);
            }

            @Override
            public void onFailed() {
                // 提示获取数据失败
            }
        });
    }

    /**
     * 保存数据
     */
    public void saveData(Diary diary) {
        mDiaryModel.saveDiary(diary);
        // 提示数据保存成功
    }

    public void deleteData(int id) {
        mDiaryModel.deleteDiary(id);
        // 提示数据删除成功
    }
}
