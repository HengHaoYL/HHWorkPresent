package com.henghao.hhworkpresent.views;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bryanrady on 2017/4/5.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 13;

    public DatabaseHelper(Context context, String name) {
        this(context,name,null,VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user(id integer PRIMARY KEY AUTOINCREMENT NOT NULL,uid varchar(20),username varchar(20),password varchar(20),firstName varchar(20),giveName varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        onCreate(db);
    }
}
