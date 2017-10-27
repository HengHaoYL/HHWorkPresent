package com.henghao.hhworkpresent.entity;

/**
 * Created by bryanrady on 2017/10/23.
 */

public class MeetingTrajectoryEntity {

    private int mtid;
    private int mid;    //会议预约Id
    private String shouldAttendMetting;         //应该本身参加会议的人
    private String substitute;              //替代开会人
    private String startSignInCoordinates;  //进场签到坐标
    private String startSignInTime;         //进场签到时间
    private String endSignInCoordinates;    //退场签到坐标
    private String endSignInTime;           //退场签到时间
    private String meetingSummary;          //会议纪要
    private String meetingImagePath;      //会议现场所有图片的路径


    public int getMtid() {
        return mtid;
    }

    public void setMtid(int mtid) {
        this.mtid = mtid;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getEndSignInCoordinates() {
        return endSignInCoordinates;
    }

    public void setEndSignInCoordinates(String endSignInCoordinates) {
        this.endSignInCoordinates = endSignInCoordinates;
    }

    public String getSubstitute() {
        return substitute;
    }

    public void setSubstitute(String substitute) {
        this.substitute = substitute;
    }

    public String getShouldAttendMetting() {
        return shouldAttendMetting;
    }

    public void setShouldAttendMetting(String shouldAttendMetting) {
        this.shouldAttendMetting = shouldAttendMetting;
    }

    public String getStartSignInCoordinates() {
        return startSignInCoordinates;
    }

    public void setStartSignInCoordinates(String startSignInCoordinates) {
        this.startSignInCoordinates = startSignInCoordinates;
    }

    public String getStartSignInTime() {
        return startSignInTime;
    }

    public void setStartSignInTime(String startSignInTime) {
        this.startSignInTime = startSignInTime;
    }

    public String getEndSignInTime() {
        return endSignInTime;
    }

    public void setEndSignInTime(String endSignInTime) {
        this.endSignInTime = endSignInTime;
    }

    public String getMeetingSummary() {
        return meetingSummary;
    }

    public void setMeetingSummary(String meetingSummary) {
        this.meetingSummary = meetingSummary;
    }

    public String getMeetingImagePath() {
        return meetingImagePath;
    }

    public void setMeetingImagePath(String meetingImagePath) {
        this.meetingImagePath = meetingImagePath;
    }

    @Override
    public String toString() {
        return "MeetingTrajectoryEntity{" +
                "mtid=" + mtid +
                ", mid=" + mid +
                ", shouldAttendMetting='" + shouldAttendMetting + '\'' +
                ", substitute='" + substitute + '\'' +
                ", startSignInCoordinates='" + startSignInCoordinates + '\'' +
                ", startSignInTime='" + startSignInTime + '\'' +
                ", endSignInCoordinates='" + endSignInCoordinates + '\'' +
                ", endSignInTime='" + endSignInTime + '\'' +
                ", meetingSummary='" + meetingSummary + '\'' +
                ", meetingImagePath='" + meetingImagePath + '\'' +
                '}';
    }
}
