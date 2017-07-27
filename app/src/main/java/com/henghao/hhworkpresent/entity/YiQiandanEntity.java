package com.henghao.hhworkpresent.entity;

/**
 * Created by bryanrady on 2017/7/26.
 */

public class YiQiandanEntity {


    /**
     * userId : HZ9080955acfcfff015acfe883040409
     * name : 兰天
     * dept : 局领导
     * signedIn : 已签到
     */

    private String userId;
    private String name;
    private String dept;
    private String signedIn;

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

    public String getSignedIn() {
        return signedIn;
    }

    public void setSignedIn(String signedIn) {
        this.signedIn = signedIn;
    }

    @Override
    public String toString() {
        return "YiQiandanEntity{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", dept='" + dept + '\'' +
                ", signedIn='" + signedIn + '\'' +
                '}';
    }
}
