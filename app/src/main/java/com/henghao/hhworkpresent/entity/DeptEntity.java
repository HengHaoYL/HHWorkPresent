package com.henghao.hhworkpresent.entity;

/**
 * Created by ASUS on 2017/9/26.
 */

public class DeptEntity {

    /**
     * id : HZ28e7f51e816d28011e816da7f1001f
     * dept_NAME : 管理员
     */

    private String id;
    private String dept_NAME;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDept_NAME() {
        return dept_NAME;
    }

    public void setDept_NAME(String dept_NAME) {
        this.dept_NAME = dept_NAME;
    }

    @Override
    public String toString() {
        return "DeptEntity{" +
                "id='" + id + '\'' +
                ", dept_NAME='" + dept_NAME + '\'' +
                '}';
    }
}
