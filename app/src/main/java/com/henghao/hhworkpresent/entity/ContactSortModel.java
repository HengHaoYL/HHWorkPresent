package com.henghao.hhworkpresent.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by bryanrady on 2017/4/1.
 */

public class ContactSortModel {

  /*  @Expose
    private String name;//显示的数据*/

    @Expose
    private String sortLetters;//显示数据拼音的首字母

 /*   public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }*/

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    /**
     * 通讯录名字
     */
    @Expose
    private String username;  //显示的数据*/

    /**
     * 通讯录  岗位
     */
    @Expose
    private String sysname;

    /**
     * 通讯录  政治面貌
     */
    @Expose
    private String political;

    /**
     * 通讯录 性别
     */
    @Expose
    private String sex;

    /**
     * 通讯录  部门名字
     */
    @Expose
    private String orgname;

    /**
     * 通讯录  电话号码
     */
    @Expose
    private String mobilephone;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSysname() {
        return sysname;
    }

    public void setSysname(String sysname) {
        this.sysname = sysname;
    }

    public String getPolitical() {
        return political;
    }

    public void setPolitical(String political) {
        this.political = political;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }
}
