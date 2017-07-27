package com.henghao.hhworkpresent.entity;

/**
 * Created by bryanrady on 2017/7/27.
 */

public class AllZaotuiEntity {


    /**
     * userId : HZ9080955acfcfff015acff9606c067a
     * name : 朱智刚
     * dept : 局办公室
     * early : 早退92分钟
     * clockOutTime : 15:27:31
     * currentDate : 2017-06-14
     */

    private String userId;
    private String name;
    private String dept;
    private String early;
    private String clockOutTime;
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

    public String getEarly() {
        return early;
    }

    public void setEarly(String early) {
        this.early = early;
    }

    public String getClockOutTime() {
        return clockOutTime;
    }

    public void setClockOutTime(String clockOutTime) {
        this.clockOutTime = clockOutTime;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
