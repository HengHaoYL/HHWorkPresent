package com.henghao.hhworkpresent.entity;

/**
 * Created by bryanrady on 2017/7/26.
 */

public class ChuchaiTongjiEntity {

    /**
     * userId : HZ9080955acfcfff015ad012f4870975
     * name : 梁龙江
     * dept : 安监执法四大队
     * evection : 出差
     */

    private String userId;
    private String name;
    private String dept;
    private String evection;

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

    public String getEvection() {
        return evection;
    }

    public void setEvection(String evection) {
        this.evection = evection;
    }

    @Override
    public String toString() {
        return "ChuchaiTongjiEntity{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", dept='" + dept + '\'' +
                ", evection='" + evection + '\'' +
                '}';
    }
}
