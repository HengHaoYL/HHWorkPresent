package com.henghao.hhworkpresent.views;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 添加隐患描述保存信息的数据库  threat表
 * Created by ASUS on 2017/9/13.
 */

public class YinhuanDatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    public YinhuanDatabaseHelper(Context context, String name) {
        this(context,name,null,VERSION);
    }

    public YinhuanDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,name,factory,version);
    }

    public YinhuanDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table threat(id integer PRIMARY KEY AUTOINCREMENT NOT NULL,threat_time varchar(20)," +
                "threat_degree varchar(20),threat_position varchar(200),threat_description varchar(200),threat_imagepath varchar(200))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists threat");
        onCreate(db);
    }
}
