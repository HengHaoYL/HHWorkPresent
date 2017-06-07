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
       //     ROOT_URL = "222.85.156.33:8082";
        } else {
            // 测试地址
      //      ROOT_URL = "http://222.85.156.33:8082";
         ROOT_URL = "http://172.16.13.118:8080";
        }
    }

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


    /**
     * 实时上传经纬度接口
     */
    public static final String APP_REALTIME_UPLOAD_LATLON = "/statisticsInfo/user/ LongAndLat";

    /************************* 工作流 start **************************/

    /**
     * 工作流接口  添加参数 免登陆
     */
    public static final String APP_WORKFLOW_NOLOGIN = "/hz7/horizon/basics/getBasics.wf?loginName=";

    /************************* 工作流 end **************************/


    /************************* 消息 start **************************/

    /**
     * 查询消息列表（我要办理的消息列表）
     */
    public static final String APP_REQUEST_MSG_LIST_COUNTS = "/statisticsInfo/user/myMessages";

    /************************ 消息 end **************************/



    /************************* 签到 start **************************/

    /**
     * 签到提交接口
     */
    public static final String APP_QIANDAO = "/check_workflow/CI/addChecking";

    /************************ 签到 end **************************/



    /**
     * 用户登录验证接口
     */
    public static final String APP_USER_LOGIN = "/statisticsInfo/user/loginCheck";



    /************************* 考勤 start **************************/

    /**
     * 查询考勤界面信息
     */
    public static final String APP_QUERY_MOUNTH_KAOQING = "/check_workflow/CI/findAllNumByUserId";

    /**
     * 查询当月迟到记录
     */
    public static final String APP_QUERY_MONTH_CHIDAO = "/check_workflow/CI/findCdListByUserId";

    /**
     * 查询当月早退记录
     */
    public static final String APP_QUERY_MONTH_ZAOTUI = "/check_workflow/CI/findZtListByUserId";

    /**
     * 查询当月缺卡记录
     */
    public static final String APP_QUERY_MONTH_QUEKA = "/check_workflow/CI/findQkListByUserId";

    /**
     * 查询当月旷工记录
     */
    public static final String APP_QUERY_MONTH_KUANGGONG = "/check_workflow/CI/findKgListByUserId";


    /**
     * 查询某天考勤记录
     */
    public static final String APP_QUERY_DAY_OF_KAOQING = "/check_workflow/CI/findByUserId";


    /**
     * 查询某天是不是国家法定假日 和周末不上班的日子
     */
    public static final String APP_QUERY_DAY_OF_HOLIDAY = "/check_workflow/restday/findRestDay";



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
    public static final String APP_QUERY_GONGGAO_IMAGE = "/safetySuperviseApp/uploadImage/";

    /**
     * 查询未读公告接口
     */
    public static final String APP_QUERY_UNREAD_GONGGAO = "/statisticsInfo/notice/findUnRead";

    /**
     * 查询已读公告接口
     */
    public static final String APP_QUERY_READ_GONGGAO = "/statisticsInfo/notice/findReadAlready";

    /**
     * 将一条未读公告变为已读接口
     */
    public static final String APP_ADD_READ_GONGGAO = "/statisticsInfo/notice/MarkAsRead";

    /**
     * 将全部未读公告变为已读接口
     */
    public static final String APP_ADD_ALL_READ_GONGGAO = "/statisticsInfo/notice/ReadAll";

    /**
     * 删除公告
     */
    public static final String APP_DELETE_GONGGAO = "/statisticsInfo/notice/deleteNotice";

    /************************ 公告 end **************************/

    /************************ 个人资料界面 start **************************/

    /**
     * 获取所有人员用户id
     */
    public static final String APP_GET_ALL_UERID = "/statisticsInfo/user/getIds";

    /**
     * 下载用户头像Uri  图片下载路径url
     */
    public static final String APP_LODAING_HEAD_IMAGE_URI = "/uploadImage/";


    /**
     * 下载用户头像图片名字接口
     */
    public static final String APP_LODAING_HEAD_IMAGE = "/statisticsInfo/user/showUserImage";

    /**
     * 上传用户头像接口
     */
    public static final String APP_REQUEST_HEAD_IMAGE = "/statisticsInfo/user/imageUpLoad";

    /**
     * 个人详细资料查询
     */
    public static final String APP_QUERY_MYSELF_ZILIAO = "/statisticsInfo/user/findByUid";

   /**
    * 上传个人资料
    */
    public static final String APP_UPLOAD_MYSELF_ZILIAO = "/statisticsInfo/user/updatePersonal";

    /**
     * 通讯录列表查询
     */
    public static final String APP_QUERY_TONGXUNLU = "/statisticsInfo/user/findAllUser";

    /************************ 个人资料界面 end **************************/
}
