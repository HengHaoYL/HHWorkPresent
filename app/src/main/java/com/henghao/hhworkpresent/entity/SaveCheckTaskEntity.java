package com.henghao.hhworkpresent.entity;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 添加检查任务界面点击保存后要提交的数据对象  再把我要检查界面需要提交的数据封装
 * Created by ASUS on 2017/9/15.
 */

public class SaveCheckTaskEntity {

    private String company_name;        //公司名称
    private String checkPeople1;        //检查人员1  也就是系统登录人员
    private String checkPeople2;        //检查人员2  也就是被选中的执法人员
    private String checkTime;           //检查时间
    private List<JianchaMaterialEntity> jianchaMaterialEntityList;  //被选中的隐患列表
    private String checkSite;           //检查现场
    private String siteResponse;        //企业现场负责人
    private String siteImagePath;       //巡查现场合照

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

    public String getCheckSite() {
        return checkSite;
    }

    public void setCheckSite(String checkSite) {
        this.checkSite = checkSite;
    }

    public String getSiteImagePath() {
        return siteImagePath;
    }

    public void setSiteImagePath(String siteImagePath) {
        this.siteImagePath = siteImagePath;
    }

    public String getSiteResponse() {
        return siteResponse;
    }

    public void setSiteResponse(String siteResponse) {
        this.siteResponse = siteResponse;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
