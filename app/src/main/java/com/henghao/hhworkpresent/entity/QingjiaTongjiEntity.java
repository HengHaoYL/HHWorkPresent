package com.henghao.hhworkpresent.entity;

/**
 * Created by bryanrady on 2017/7/26.
 */

public class QingjiaTongjiEntity {
    /**
     * userId : HZ9080955acfcfff015acfe9cb0f044d
     * name : 胡正康
     * dept : 局领导
     * leave : 请假
     */

    private String userId;
    private String name;
    private String dept;
    private String leave;

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

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }

    @Override
    public String toString() {
        return "QingjiaTongjiEntity{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", dept='" + dept + '\'' +
                ", leave='" + leave + '\'' +
                '}';
    }
}
