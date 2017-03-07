/*
 * 文件名：ProtocolUrl.java
 * 版权：Copyright 2009-2010 companyName MediaNet. Co. Ltd. All Rights Reserved.
 * 描述：
 * 修改人：
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.henghao.hhworkpresent;

/**
 * 〈一句话功能简述〉 〈功能详细描述〉
 *
 * @author zhangxianwen
 * @version HDMNV100R001, 2015-4-20
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ProtocolUrl {

    /**
     * 服务端根地址
     */
    public static String ROOT_URL = "";

    public static boolean isURL = true;

    static {
        if (!isURL) {
            // 生产地址
        //    ROOT_URL = "http://safe.higdata.com/YL_BigData";
        } else {
            // 测试地址/192.168.1.12
            ROOT_URL = "http://172.16.0.31:8080";
            //172.16.13.101:8080/login/az?username=?&password=?
            /*ROOT_URL = "http://safe.higdata.com/Java_Nfc/";*/
//			ROOT_URL = "http://192.168.1.12/Java_Nfc";
        }
    }

    // TODO 用户相关
    /************************
     * 用户相关
     **************************/
   /* public static final String USER = "user/";

    *//**
     * 用户登录172.16.13.101:8080/YL_BigData/login?username=?&password=?
     *//*
    public static String APP_LOGIN = "login";

    public static String APP_GET_NFCBYID = "login";
    *//**
     * 用户注册
     *//*
    public static String APP_REG = "register";*/

    /************************ 用户相关 end **************************/

    // TODO app系统 相关
    /************************
     * app系统 相关
     **************************/
    public static final String SYSTEM = "j_appSystem/";

    /**
     * app启动页面信息
     */
    public static final String APP_START = SYSTEM + "appStart";

    /**
     * app引导页面信息
     */
    public static final String APP_GUIDE = SYSTEM + "appGuide";

    /**
     * app系统版本更新
     */
    public static final String APP_SYS_UPDATE = SYSTEM + "appUserUpdate";
    /************************ app系统 end **************************/
    /************************
     * 签到
     **************************/

    public static final String APP_QIANDAO = "/checking-in/CI/addChecking";

    /************************ 签到 end **************************/
    /************************

    /**
     * 上传错误日志到服务器
     */
    public static final String UPLOAD_ERROR_SERVER = "appError";


}
