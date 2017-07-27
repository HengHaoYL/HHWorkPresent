package com.henghao.hhworkpresent.entity;

/**
 * Created by bryanrady on 2017/7/26.
 */

public class ChidaoTongjiaEntity {

    /**
     * userId : HZ9080955acfcfff015ad00776fd080d
     * name : 杨亚民
     * dept : 安监三处
     * late : 迟到1分钟
     * clockInTime : 09:01:10
     */

    private String userId;
    private String name;
    private String dept;
    private String late;
    private String clockInTime;

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

    @Override
    public String toString() {
        return "ChidaoTongjiaEntity{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", dept='" + dept + '\'' +
                ", late='" + late + '\'' +
                ", clockInTime='" + clockInTime + '\'' +
                '}';
    }
}
