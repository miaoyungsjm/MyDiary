package com.example.my.mydiary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author my
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "database.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME_DIARY = "diary";
    private static final String CREATE_TABLE_DIARY =
            "create table if not exists " + TABLE_NAME_DIARY
                    + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "title TEXT, "
                    + "content TEXT, "
                    + "pubDate TEXT )";

    /**
     * 构造函数
     */
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DIARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME_DIARY);
        onCreate(db);
    }
}
