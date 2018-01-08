/*
 * 文件名：SplashActivity.java
 * 版权：Copyright 2009-2010 companyName MediaNet. Co. Ltd. All Rights Reserved.
 * 描述：
 * 修改人：
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.henghao.hhworkpresent.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.service.KaoqingService;
import com.henghao.hhworkpresent.service.NotificationService;
import com.henghao.hhworkpresent.service.RealTimeService;
import com.henghao.hhworkpresent.views.DatabaseHelper;

/**
 * 引导页面
 */
public class SplashActivity extends ActivityFragmentSupport {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.AppTheme);
		setContentView(R.layout.common_activity_splash);
		com.lidroid.xutils.ViewUtils.inject(this);
		initData();
	}

	@Override
	public void initData() {
		postDelayed(0);
	}

	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 101:
					Intent _intent = new Intent();
					dbHelper = new DatabaseHelper(SplashActivity.this,"user_login.db");
					// 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
					db = dbHelper.getReadableDatabase();
					String uid = null;
					String username = null;
					String password = null;
					// 调用SQLiteDatabase对象的query方法进行查询，返回一个Cursor对象：由数据库查询返回的结果集对象  
					// 第一个参数String：表名  
					// 第二个参数String[]:要查询的列名  
					// 第三个参数String：查询条件  
					// 第四个参数String[]：查询条件的参数  
					// 第五个参数String:对查询的结果进行分组  
					// 第六个参数String：对分组的结果进行限制  
					// 第七个参数String：对查询的结果进行排序 
					Cursor cursor = db.query("user",new String[]{"uid","username","password"},null,null,null,null,null);
					// 将光标移动到下一行，从而判断该结果集是否还有下一条数据，如果有则返回true，没有则返回false
					while (cursor.moveToNext()){
						uid = cursor.getString((cursor.getColumnIndex("uid")));
						username = cursor.getString((cursor.getColumnIndex("username")));
						password = cursor.getString((cursor.getColumnIndex("password")));
					}
					if(("null".equals(username)|| username ==null)
							|| ("null".equals(password)|| password == null )){
						_intent.setClass(SplashActivity.this, LoginActivity.class);
						SplashActivity.this.startActivity(_intent);
						finishDelayed();
					}else{
						_intent.setClass(SplashActivity.this, MainActivity.class);
						SplashActivity.this.startActivity(_intent);

						/**
						 * 开启实时定位服务
						 */
						_intent = new Intent(SplashActivity.this, RealTimeService.class);
						startService(_intent);
						_intent = new Intent(SplashActivity.this, NotificationService.class);
						startService(_intent);
						_intent = new Intent(SplashActivity.this, KaoqingService.class);
						startService(_intent);
						finishDelayed();
					}
					break;
				default:
					break;
			}
		}
	};

	/**
	 * 延迟一秒 让用户看到引导页面
	 * @param time
     */
	private void postDelayed(final int time) {

		this.mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				SplashActivity.this.mHandler.sendEmptyMessage(101);
			}
		},time);
	}

}
