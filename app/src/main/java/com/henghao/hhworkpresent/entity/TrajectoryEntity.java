package com.henghao.hhworkpresent.entity;

import java.util.List;

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

    private String eventName;
    private String eventAddress;
    private String eventTime;
    private List<String> eventImageNameList;

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

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public List<String> getEventImageNameList() {
        return eventImageNameList;
    }

    public void setEventImageNameList(List<String> eventImageNameList) {
        this.eventImageNameList = eventImageNameList;
    }

    @Override
    public String toString() {
        return "TrajectoryEntity{" +
                "eventName='" + eventName + '\'' +
                ", eventAddress='" + eventAddress + '\'' +
                ", eventTime='" + eventTime + '\'' +
                ", eventImageNameList=" + eventImageNameList +
                '}';
    }
}
