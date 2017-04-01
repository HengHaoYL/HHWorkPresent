package com.henghao.hhworkpresent.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by bryanrady on 2017/3/27.
 *  考勤签到实体类
 */

public class KaoqingEntity extends IdEntity {

    @Expose
    private String id;

    //用户的id
    @Expose
    private String userId;

    @Expose
    private String userName;

    //上班打卡时间
    @Expose
    private String clockInTime;

    //下班打卡时间
    @Expose
    private String clockOutTime;

    //上午考勤地点
    @Expose
    private String checkLocation;

    //上午打卡的经度
    @Expose
    private String lon;

    //上午打卡的纬度
    @Expose
    private String lat;

    //下午考勤地点
    @Expose
    private String afterLocation;

    //下午打卡的经度
    @Expose
    private String afterLon;

    //下午打卡的纬度
    @Expose
    private String afterLat;

    //是否补签，1为正常考勤，2为补签
    @Expose
    private String status;

    //考勤类型，1为正常考勤，2为外勤签到
    @Expose
    private String checkType;

    //补签描述
    @Expose
    private String repairDescr;

    //补签时间段,1为早上，2为下午，3为全天
    @Expose
    private String repairSection;

    //补签时间
    @Expose
    private String repairDate;

    //补签审批领导
    @Expose
    private int leaderId;

    //领导是否同意，1为同意，2为不同意，0为未审批
    @Expose
    private String leaderOpinion;

    //领导审批时间
    @Expose
    private String approvalTime;

    //当天上午打卡的次数
    @Expose
    private int morningCount;

    //当天下午打卡的次数
    @Expose
    private int afterCount;

    //当前日期
    @Expose
    private String currentDate;

    //日期工作日（星期几）
    @Expose
    private String workDay;

    //部门
    @Expose
    private String dept;

    //岗位、
    @Expose
    private String job;


    public String getDept() {
        return dept;
    }
    public void setDept(String dept) {
        this.dept = dept;
    }
    public String getWorkDay() {
        return workDay;
    }
    public void setWorkDay(String workDay) {
        this.workDay = workDay;
    }
    public String getJob() {
        return job;
    }
    public void setJob(String job) {
        this.job = job;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getClockInTime() {
        return clockInTime;
    }
    public void setClockInTime(String clockInTime) {
        this.clockInTime = clockInTime;
    }
    public String getClockOutTime() {
        return clockOutTime;
    }
    public void setClockOutTime(String clockOutTime) {
        this.clockOutTime = clockOutTime;
    }
    public String getCheckLocation() {
        return checkLocation;
    }
    public void setCheckLocation(String checkLocation) {
        this.checkLocation = checkLocation;
    }
    public String getLon() {
        return lon;
    }
    public void setLon(String lon) {
        this.lon = lon;
    }
    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getAfterLocation() {
        return afterLocation;
    }
    public void setAfterLocation(String afterLocation) {
        this.afterLocation = afterLocation;
    }
    public String getAfterLon() {
        return afterLon;
    }
    public void setAfterLon(String afterLon) {
        this.afterLon = afterLon;
    }
    public String getAfterLat() {
        return afterLat;
    }
    public void setAfterLat(String afterLat) {
        this.afterLat = afterLat;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getCheckType() {
        return checkType;
    }
    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }
    public String getRepairDescr() {
        return repairDescr;
    }
    public void setRepairDescr(String repairDescr) {
        this.repairDescr = repairDescr;
    }
    public String getRepairSection() {
        return repairSection;
    }
    public void setRepairSection(String repairSection) {
        this.repairSection = repairSection;
    }
    public String getRepairDate() {
        return repairDate;
    }
    public void setRepairDate(String repairDate) {
        this.repairDate = repairDate;
    }
    public int getLeaderId() {
        return leaderId;
    }
    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }
    public String getLeaderOpinion() {
        return leaderOpinion;
    }
    public void setLeaderOpinion(String leaderOpinion) {
        this.leaderOpinion = leaderOpinion;
    }
    public String getApprovalTime() {
        return approvalTime;
    }
    public void setApprovalTime(String approvalTime) {
        this.approvalTime = approvalTime;
    }
    public int getMorningCount() {
        return morningCount;
    }
    public void setMorningCount(int morningCount) {
        this.morningCount = morningCount;
    }
    public int getAfterCount() {
        return afterCount;
    }
    public void setAfterCount(int afterCount) {
        this.afterCount = afterCount;
    }
    public String getCurrentDate() {
        return currentDate;
    }
    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }


}
