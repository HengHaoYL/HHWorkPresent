package com.henghao.hhworkpresent.entity;

/**
 * Created by bryanrady on 2017/7/27.
 */

public class AllKuanggongEntity {

    /**
     * userID : HZ8bb0c95ce22e77015ce712f4f13842
     * name : 龙玉国
     * dept : 安监三处
     * absenteeismDate : 2017-07-06
     * absenteeism : 旷工
     */

    private String userID;
    private String name;
    private String dept;
    private String absenteeismDate;
    private String absenteeism;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getAbsenteeismDate() {
        return absenteeismDate;
    }

    public void setAbsenteeismDate(String absenteeismDate) {
        this.absenteeismDate = absenteeismDate;
    }

    public String getAbsenteeism() {
        return absenteeism;
    }

    public void setAbsenteeism(String absenteeism) {
        this.absenteeism = absenteeism;
    }
}
