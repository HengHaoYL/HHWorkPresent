package com.henghao.hhworkpresent.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.henghao.hhworkpresent.views.DatabaseHelper;

/**
 * Created by bryanrady on 2017/5/24.
 */

public class SqliteDBUtils {

    private Context mContext;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public SqliteDBUtils(Context context){
        mContext = context;
        dbHelper = new DatabaseHelper(mContext,"user_login.db");
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 从本地数据库读取登录用户Id 用来作为数据请求id
     * @return
     */
    public String getLoginUid(){
        Cursor cursor = db.query("user",new String[]{"uid"},null,null,null,null,null);
        String uid = null;
        while (cursor.moveToNext()){
            uid = cursor.getString((cursor.getColumnIndex("uid")));
        }
        return uid;
    }

    public String getLoginFirstName(){
        Cursor cursor = db.query("user",new String[]{"firstName"},null,null,null,null,null);
        String firstName = null;
        while (cursor.moveToNext()){
            firstName = cursor.getString((cursor.getColumnIndex("firstName")));
        }
        return firstName;
    }

    public String getLoginGiveName(){
        Cursor cursor = db.query("user",new String[]{"giveName"},null,null,null,null,null);
        String giveName = null;
        while (cursor.moveToNext()){
            giveName = cursor.getString((cursor.getColumnIndex("giveName")));
        }
        return giveName;
    }

    public String getUsername(){
        String username = null;
        Cursor cursor = db.query("user",new String[]{"username"},null,null,null,null,null);
        // 将光标移动到下一行，从而判断该结果集是否还有下一条数据，如果有则返回true，没有则返回false
        while (cursor.moveToNext()){
            username = cursor.getString((cursor.getColumnIndex("username")));
        }
        return username;
    }

}
