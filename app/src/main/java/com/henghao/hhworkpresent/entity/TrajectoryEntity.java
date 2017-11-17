package com.henghao.hhworkpresent.entity;

/**
 * 工作轨迹实体类
 * Created by bryanrady on 2017/7/18.
 */

public class TrajectoryEntity {

    /**
     * eventName : hhn
     * eventAddress : 中国贵州省贵阳市乌当区G210(包南线)
     * eventTime : 16:52:28
     * eventImageNameList : []
     */

    private int id;
    private String userId;
    // * 日期
    private String eventDate;
    // * 事件名称
    private String eventName;
    // * 地点
    private String eventAddress;
    // * 上传时间
    private String eventTime;
    //图片地址
    private String eventImagePath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public String getEventImagePath() {
        return eventImagePath;
    }

    public void setEventImagePath(String eventImagePath) {
        this.eventImagePath = eventImagePath;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public String toString() {
        return "TrajectoryEntity{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventAddress='" + eventAddress + '\'' +
                ", eventTime='" + eventTime + '\'' +
                ", eventImagePath='" + eventImagePath + '\'' +
                '}';
    }
}
