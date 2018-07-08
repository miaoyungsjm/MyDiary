package com.example.my.mydiary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.my.mydiary.main.bean.Diary;

import java.util.ArrayList;
import java.util.List;

/**
 * @author my
 * @date 2018/7/7
 */
public class DatabaseDao {
    private DatabaseHelper mDatabaseHelper;

    /**
     * 单例对象
     */
    private volatile static DatabaseDao instance = null;

    /**
     * 构造函数
     */
    public DatabaseDao(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    /**
     * 双锁单例模式（DCL）
     *
     * 获取单例对象
     * 要求传入 Application 的 Context 参数
     */
    public static DatabaseDao getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseDao.class) {
                if (instance == null) {
                    instance = new DatabaseDao(context);
                }
            }
        }
        return instance;
    }

    /**
     * 查询数据
     */
    public List<Diary> selectDB(String selection) {
        List<Diary> list = new ArrayList<>();
        // 开启数据库
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            // 参数：table, columns, selection, selectionArgs, groupBy, having, orderBy, (limit)
            cursor = db.query(DatabaseHelper.TABLE_NAME_DIARY, null, selection, null, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Integer id = cursor.getInt(cursor.getColumnIndex("_id"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String content = cursor.getString(cursor.getColumnIndex("content"));
                    String pubDate = cursor.getString(cursor.getColumnIndex("pubDate"));

                    Diary diary = new Diary(id, title, content, pubDate);

                    list.add(diary);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            // 关闭数据库
            db.close();
        }

        return list;
    }

    /**
     * 插入数据库
     */
    public void insertDB(Diary diary) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", diary.getTitle());
        contentValues.put("content", diary.getContent());
        contentValues.put("pubDate", diary.getPubDate());

        db.insert(DatabaseHelper.TABLE_NAME_DIARY, null, contentValues);
        contentValues.clear();

        db.close();
    }

    /**
     * 更新数据库
     */
    public void updateDB(Diary diary) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", diary.getTitle());
        contentValues.put("content", diary.getContent());
        contentValues.put("pubDate", diary.getPubDate());

        // 修改数据，参数：table, contentValues, whereClause, whereArgs
        int err = db.update(DatabaseHelper.TABLE_NAME_DIARY, contentValues,
                "_id = ?", new String[]{String.valueOf(diary.getId())});
        // 如果没有数据，插入数据
        if (err == 0) {
            db.insert(DatabaseHelper.TABLE_NAME_DIARY, null, contentValues);
        }
        contentValues.clear();

        db.close();
    }

    /**
     * 删除一组数据
     */
    public void deleteDB(int id) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        db.delete(DatabaseHelper.TABLE_NAME_DIARY,
                "_id = ?",
                new String[]{String.valueOf(id)});

        db.close();
    }

    /**
     * 删除全部数据
     */
    public void deleteAllDB() {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        db.delete(DatabaseHelper.TABLE_NAME_DIARY, null, null);

        db.close();
    }
}
