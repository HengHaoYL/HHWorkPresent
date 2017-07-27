package com.henghao.hhworkpresent.entity;

/**
 * Created by bryanrady on 2017/7/26.
 */

public class WeiQiandaoEntity {

    /**
     * userId : admin
     * name : 管理员
     * dept : 管理员
     * noSignIn : 未签到
     */

    private String userId;
    private String name;
    private String dept;
    private String noSignIn;

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

    public String getNoSignIn() {
        return noSignIn;
    }

    public void setNoSignIn(String noSignIn) {
        this.noSignIn = noSignIn;
    }
}
