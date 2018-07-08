package com.example.my.mydiary.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.my.mydiary.bean.Diary;

import java.util.ArrayList;
import java.util.List;

public class DiaryManager {

    private Context mContext;

    public DiaryManager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 查询数据库
     */
    public List<Diary> selectDB(String selection) {

        List<Diary> list = new ArrayList<>();

        // 连接数据库
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
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
            databaseHelper.close();
            db.close();
        }

        return list;
    }

    /**
     * 插入数据库
     */
    public void insertDB(Diary diary) {

        // 连接数据库
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", diary.getTitle());
        contentValues.put("content", diary.getContent());
        contentValues.put("pubDate", diary.getPubDate());

        db.insert(DatabaseHelper.TABLE_NAME_DIARY, null, contentValues);
        contentValues.clear();

        // 关闭数据库
        databaseHelper.close();
        db.close();
    }

    /**
     * 更新数据库
     */
    public void updateDB(Diary diary) {
        // 连接数据库
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

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

        // 关闭数据库
        databaseHelper.close();
        db.close();
    }

    /**
     * 删除一组数据
     */
    public void deleteDB(int id) {
        // 连接数据库
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        db.delete(DatabaseHelper.TABLE_NAME_DIARY,
                "_id = ?",
                new String[]{String.valueOf(id)});

        // 关闭数据库
        databaseHelper.close();
        db.close();
    }

    /**
     * 删除全部数据
     */
    public void deleteAllDB() {
        // 连接数据库
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        db.delete(DatabaseHelper.TABLE_NAME_DIARY, null, null);

        // 关闭数据库
        databaseHelper.close();
        db.close();
    }
}
