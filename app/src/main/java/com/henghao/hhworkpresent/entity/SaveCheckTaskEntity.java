package com.henghao.hhworkpresent.entity;

import java.util.List;

/**
 * 添加检查任务界面点击保存后要提交的数据对象
 * Created by ASUS on 2017/9/15.
 */

public class SaveCheckTaskEntity {

    private String company_name;
    private String checkPeople1;
    private String checkPeople2;
    private String checkTime;
    private List<JianchaMaterialEntity> jianchaMaterialEntityList;

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCheckPeople1() {
        return checkPeople1;
    }

    public void setCheckPeople1(String checkPeople1) {
        this.checkPeople1 = checkPeople1;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckPeople2() {
        return checkPeople2;
    }

    public void setCheckPeople2(String checkPeople2) {
        this.checkPeople2 = checkPeople2;
    }

    public List<JianchaMaterialEntity> getJianchaMaterialEntityList() {
        return jianchaMaterialEntityList;
    }

    public void setJianchaMaterialEntityList(List<JianchaMaterialEntity> jianchaMaterialEntityList) {
        this.jianchaMaterialEntityList = jianchaMaterialEntityList;
    }

    @Override
    public String toString() {
        return "SaveCheckTaskEntity{" +
                "company_name='" + company_name + '\'' +
                ", checkPeople1='" + checkPeople1 + '\'' +
                ", checkPeople2='" + checkPeople2 + '\'' +
                ", checkTime='" + checkTime + '\'' +
                ", jianchaMaterialEntityList=" + jianchaMaterialEntityList +
                '}';
    }
}
