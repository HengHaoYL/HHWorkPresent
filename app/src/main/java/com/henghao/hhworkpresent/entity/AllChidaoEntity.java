package com.henghao.hhworkpresent.entity;

/**
 * Created by bryanrady on 2017/7/27.
 */

public class AllChidaoEntity {

    /**
     * userId : HZ9080955acfcfff015acfe883040409
     * name : 兰天
     * dept : 局领导
     * late : 迟到17分钟
     * clockInTime : 09:17:50
     * currentDate : 2017-06-09
     */

    private String userId;
    private String name;
    private String dept;
    private String late;
    private String clockInTime;
    private String currentDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getLate() {
        return late;
    }

    public void setLate(String late) {
        this.late = late;
    }

    public String getClockInTime() {
        return clockInTime;
    }

    public void setClockInTime(String clockInTime) {
        this.clockInTime = clockInTime;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
