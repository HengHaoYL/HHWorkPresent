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
            // 测试地址
            ROOT_URL = "http://172.16.0.117:8080";
        }
    }

    // TODO 用户相关
    /************************
     * 用户相关
     **************************/
    public static final String USER = "user/";

    /**
     * 用户登录172.16.13.101:8080/YL_BigData/login?username=?&password=?
     */
    public static String APP_LOGIN = "login";

    public static String APP_GET_NFCBYID = "login";
    /**
     * 用户注册
     */
    public static String APP_REG = "register";

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


    /**
     * 上传错误日志到服务器
     */
    public static final String UPLOAD_ERROR_SERVER = "appError";





    /************************* 签到 start **************************/

    /**
     * 签到提交接口
     */
    public static final String APP_QIANDAO = "/checking-in/CI/addChecking";

    /************************ 签到 end **************************/



    /**
     * 用户登录验证接口
     */
    public static final String APP_USER_LOGIN = "/safetySuperviseApp/user/loginCheck";



    /************************* 考勤 start **************************/

    /**
     * 查询考勤界面信息
     */
    public static final String APP_QUERY_MOUNTH_KAOQING = "/checking-in/CI/findMouthAllByUserId";

    /**
     * 查询当月迟到记录
     */
    public static final String APP_QUERY_MONTH_CHIDAO = "/checking-in/CI/cidaoCkLists";

    /**
     * 查询当月早退记录
     */
    public static final String APP_QUERY_MONTH_ZAOTUI = "/checking-in/CI/zaotuiCkLists";

    /**
     * 查询当月缺卡记录
     */
    public static final String APP_QUERY_MONTH_QUEKA = "/checking-in/CI/forgetCkLists";

    /**
     * 查询当月旷工记录
     */
    public static final String APP_QUERY_MONTH_KUANGGONG = "/checking-in/CI/kuanggongLists";


    /**
     * 查询某天考勤记录
     */
    public static final String APP_QUERY_DAY_OF_KAOQING = "/checking-in/CI/findToAppByUserId";


    /**
     * 查询迟到记录单条详细记录
     */
    public static final String APP_QUERY_DAY_OF_CHIDAO = "/checking-in/CI/chidaoToApp";


    /**
     * 查询早退记录单条详细记录
     */
    public static final String APP_QUERY_DAY_OF_ZAOTUI = "/checking-in/CI/zaotuiToApp";


    /**
     * 查询缺卡记录单条详细记录
     */
    public static final String APP_QUERY_DAY_OF_QUEKA = "/checking-in/CI/queKaToApp";



    /************************ 考勤 end **************************/





    /************************* 公告 start **************************/

    /**
     * 发送公告接口
     */
    public static final String APP_SEND_GONGGAO = "/safetySuperviseApp/notice/addNotice";

    /**
     * 查询公告封面图片
     */
    public static final String APP_QUERY_GONGGAO_IMAGE = "/safetySuperviseApp/upload/";

    /**
     * 查询未读公告接口
     */
    public static final String APP_QUERY_UNREAD_GONGGAO = "/safetySuperviseApp/notice/findUnRead";

    /**
     * 查询已读公告接口
     */
    public static final String APP_QUERY_READ_GONGGAO = "/safetySuperviseApp/notice/findReadAlready";

    /**
     * 将一条未读公告变为已读接口
     */
    public static final String APP_ADD_READ_GONGGAO = "/safetySuperviseApp/notice/MarkAsRead";

    /**
     * 将全部未读公告变为已读接口
     */
    public static final String APP_ADD_ALL_READ_GONGGAO = "/safetySuperviseApp/notice/ReadAll";

    /**
     * 删除公告
     */
    public static final String APP_DELETE_GONGGAO = "/safetySuperviseApp/notice/deleteNotice";

    /************************ 公告 end **************************/

    /************************ 个人资料界面 start **************************/

    /**
     * 个人详细资料查询
     */
    public static final String APP_QUERY_MYSELF_ZILIAO = "/safetySuperviseApp/user/findPersonal";

    /**
     * 通讯录列表查询
     */
    public static final String APP_QUERY_TONGXUNLU = "/safetySuperviseApp/user/findAllUser";

    /************************ 个人资料界面 end **************************/
}
