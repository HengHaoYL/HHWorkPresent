package com.henghao.hhworkpresent.entity;

/**
 * Created by bryanrady on 2017/7/26.
 */

public class ZaotuiTongjiaEntity {

    /**
     * userId : HZ8bb0c95c19c59a015c1a7cf0ee091f
     * name : 测试贰
     * dept : 管理员
     * early : 早退4分钟
     * clockOutTime : 16:55:05
     */

    private String userId;
    private String name;
    private String dept;
    private String early;
    private String clockOutTime;

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

    @Override
    public String toString() {
        return "ZaotuiTongjiaEntity{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", dept='" + dept + '\'' +
                ", early='" + early + '\'' +
                ", clockOutTime='" + clockOutTime + '\'' +
                '}';
    }
}
