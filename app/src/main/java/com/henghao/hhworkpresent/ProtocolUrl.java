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

public class ProtocolUrl {

    /**
     * 服务端根地址
     */
    public static String ROOT_URL = "";

    static {
      ROOT_URL = "http://222.85.156.33:8082";
    }

    /************************ 用户相关 end **************************/

    /**
     * 上传错误日志到服务器
     */
    public static final String UPLOAD_ERROR_SERVER = "appError";


    /**
     * 实时上传经纬度接口
     */
    public static final String APP_REALTIME_UPLOAD_LATLON = "/statisticsInfo/user/LongAndLat";

    /************************* 工作流 start **************************/

    /**
     * 工作流接口  添加参数 免登陆
     */
    public static final String APP_WORKFLOW_NOLOGIN = "/hz7/horizon/basics/getBasics.wf?loginName=";

    /************************* 工作流 end **************************/

   /************************* 会议管理 start **************************/

    /**
     * 进场签到更新数据
     */
    public static final String APP_UPDATE_MEETING_ONLICK_START = "/istration/meetingTrajectory/updateMteStartSignInCoordinates";

    /**
     * 退场签到更新数据
     */
    public static final String APP_UPDATE_MEETING_ONLICK_END = "/istration/meetingTrajectory/updateMteEndSignInCoordinates";

    /**
     * 上传工作纪要和现场图片
     */
    public static final String APP_UPDATE_MEETING_SUMMARY_FILE = "/istration/meetingTrajectory/updateMeetingImagePath";

    /**
    * 添加预约会议内容到服务器
    */
    public static final String APP_ADD_MEETING_CONTENT = "/istration/JPush/addMeetingEntity";

    /**
     * 会议上传
     */
    public static final String APP_ADD_MEETING_UPLOAD = "/istration/JPush/addMeetingUploadEntity";

    /**
     * 查询会议记录列表
     */
    public static final String APP_QUERT_MEETING_TRAJECTORY_LIST = "/istration/meetingTrajectory/queryMeetingTrajectoryEntityByUserIdList";

    /**
     * 查询会议详情的图片
     */
    public static final String APP_QUERT_MEETING_TRAJECTORY_DETAIL_IMAGEPATH = "/meetingImage/";

    /**
    * 查询部门集合列表
    */
    public static final String APP_QUERY_DEPT_LIST = "/istration/firmdate/queryDeptAll";

    /**
    * 根据部门查询人员列表
    */
    public static final String APP_QUERY_PERSONAL_LIST = "/istration/firmdate/queryDeptByIdUser";


    /**
     * 将未读消息变为已读
     */
    public static final String APP_SET_UNREAD_TO_READ = "/istration/JPush/updateJPushToUserIsRead";

    /**
     * 根据uid查询别人推送给自己的消息列表
     */
    public static final String APP_QUERY_TUI_SONG_MESSAGE_LIST = "/istration/JPush/queryMeetingEntityByUserIdAll";

    /**
     * 根据uid查询审批通过的会议
     */
    public static final String APP_QUERY_MEETING_PASS_LIST = "/istration/JPush/queryMeetingEntityByUserList";

    /**
     * 根据uid和msg_id查询别人推送给自己的消息的那一条详细消息
     */
    public static final String APP_QUERY_TUI_SONG_MESSAGE = "/istration/JPush/queryMeetingEntityByMsgid";


    /**
     * 根据uid查询未读的推送消息
     */
    public static final String APP_QUERY_UNREAD_MESSAGE = "/istration/JPush/queryJPushToUserUnRead";


    /**
     * 选择代替人开会的接口
     */
    public static final String APP_CHOOSE_REPLACE_PEOPLE = "/istration/meetingTrajectory/updateMeetingTrajectoryEntity";


    /**
     * 点击同意或取消会走的接口并且上传理由
     */
    public static final String APP_ONCLICK_AGREE_OR_REJECT = "/istration/JPush/updateMeetingEntityWhetherPass";

    /************************* 会议管理 end **************************/



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

   /**
    * 未签到人数情况查询
    */
    public static final String APP_QUERY_WEIQIANDAO = "/deletenum/attendance/noSignIn";

    /**
     * 已签到人数情况查询
     */
    public static final String APP_QUERY_YIQIANDAO = "/deletenum/attendance/signedIn";

   /**
    * 迟到情况查询
    */
    public static final String APP_QUERY_CHIDAO = "/deletenum/attendance/late";

   /**
    * 早退情况查询
    */
    public static final String APP_QUERY_ZAOTUI = "/deletenum/attendance/early";

   /**
    * 请假情况查询
    */
    public static final String APP_QUERY_QINGJIA = "/deletenum/attendance/leave";

   /**
    * 出差情况查询
    */
    public static final String APP_QUERY_CHUCHAI = "/deletenum/attendance/evection";

   /**
    * 按月份查询所有人的迟到记录
    */
    public static final String APP_QUERY_ALL_CHIDAOLIST="/deletenum/attendance/monthLate";

   /**
    * 按月份查询所有人的早退记录
    */
    public static final String APP_QUERY_ALL_ZAOTUILIST="/deletenum/attendance/monthEarly";

    /**
     * 按月份查询所有人的缺卡记录
     */
    public static final String APP_QUERY_ALL_QUEKALIST="/deletenum/attendance/missingCard";


    /**
     * 按月份查询所有人的旷工记录
     */
     public static final String APP_QUERY_ALL_KUANGGONGLIST="/deletenum/attendance/absenteeism";


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
     * 上传工作轨迹接口
     */
    public static final String APP_UPLOAD_WORK_TRAJECTORY = "/deletenum/userEventTrack/addUetEntity";


   /**
    *下载工作轨迹接口
    */
    public static final String APP_DOWNLOAD_WORK_TRAJECTORY = "/deletenum/userEventTrack/queryByUserIdAndDate";

   /**
    *下载工作轨迹事件对应图片的接口
    */
    public static final String APP_DOWNLOAD_WORK_TRAJECTORY_IMAGE = "/eventTrack/";

    /**
     * 上传用户头像接口
     */
    public static final String APP_REQUEST_HEAD_IMAGE = "/statisticsInfo/user/imageUpLoad";

    /**
     * 个人详细资料查询
     */
    public static final String APP_QUERY_MYSELF_ZILIAO = "/statisticsInfo/user/findByUid";

    /**
     * 查询个人部门id
     */
    public static final String APP_QUERY_MYSELF_DEPT_ID = "/safetysupervision/user/selectDeptIdByUserId";

   /**
    * 上传个人资料
    */
    public static final String APP_UPLOAD_MYSELF_ZILIAO = "/safetysupervision/user/updatePersonal";

    /**
     * 通讯录列表查询
     */
    public static final String APP_QUERY_TONGXUNLU = "/statisticsInfo/user/findAllUser";

    /************************ 个人资料界面 end **************************/

   /**
    * webview下载文件链接  后面拼接上文件ID
    */
   public static final String APP_DOWNLOAD_FILE = "/istration/appload/files?id=";
}
