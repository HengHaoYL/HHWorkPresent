/*
 * 文件名：Constant.java
 * 版权：Copyright 2009-2010 companyName MediaNet. Co. Ltd. All Rights Reserved.
 * 描述：
 * 修改人：
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.henghao.hhworkpresent;

import android.os.Environment;

/**
 * 〈一句话功能简述〉 〈功能详细描述〉
 * 
 * @author zhangxianwen
 * @version HDMNV100R001, 2015-4-20
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Constant {


	/**
	 * 按下保存键
	 */
	public static final String PRESS_SAVE_BUTTON = "press save button";

	/**
	 * 停止服务
	 */
	public static final String STOP_REALTIMESERVICE = "stop realtimeService";

	/**
	 * 本地数据的配置
	 */
	public static final String SHARED_SET = "sharedset";// 登录设置

	/**
	 * 程序运行期间产生的文件，缓存根目录
	 */
	public static final String ROOT_DIR_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/gyajj/cache";

	/**
	 * 缓存文件保存的根目录
	 */
	public static final String CACHE_DIR_PATH_ROOT = ROOT_DIR_PATH + "/file";

	/**
	 * 错误日志
	 */
	public static final String LOG_DIR_PATH = ROOT_DIR_PATH + "/log";


	/**
	 * 图片文件夹
	 */
	public static final String CACHE_DIR_PATH = CACHE_DIR_PATH_ROOT + "/images";

	/**
	 * 用户ID
	 */
	public static final String USERID = "" +
			"";

	public static final String USERNAME = "user_name";

	public static final String USERSTATE = "user_state";

	public static final String USER_LOGIN_DATABASE = "user_login.db";

	public static final String USER_LOGIN_REMEMBER_DATABASE = "user_login_remenber.db";

	public static final String DATE_TIME_MM_FORMAT = "yyyy-MM-dd HH:mm";

	public static final String DATE_TIME_SS_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String TIME_MM_FORMAT = "HH:mm";


	public static final String IMAGELOADER_CACHE = "universalimageloader/Cache";

	/** 我要检查列表*/
	public final static int WOYAO_CHECK = 0;

	/** 我要复查列表*/
	public final static int WOYAO_FUCHA = 2;

}
