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

    // 用户ID
    @Expose
    private String id;
    // 用户名
    @Expose
    private String name;
    @Expose
    private String birth_DATE;
    //固定电话号码
    @Expose
    private String telephone;
    ///手机号码
    @Expose
    private String cellphone;
    // 性别   0:男 1：女
    @Expose
    private String sex;
    //工作所在地
    @Expose
    private String address;
    //职位
    @Expose
    private String position;
    //工作职责
    @Expose
    private String work_DESC;
    //部门名字
    @Expose
    private String dept_NAME;
    //员工编号
    @Expose
    private String emp_NUM;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getWork_DESC() {
        return work_DESC;
    }

    public void setWork_DESC(String work_DESC) {
        this.work_DESC = work_DESC;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDept_NAME() {
        return dept_NAME;
    }

    public void setDept_NAME(String dept_NAME) {
        this.dept_NAME = dept_NAME;
    }

    public String getEmp_NUM() {
        return emp_NUM;
    }

    public void setEmp_NUM(String emp_NUM) {
        this.emp_NUM = emp_NUM;
    }

    public String getBirth_DATE() {
        return birth_DATE;
    }
    public void setBirth_DATE(String birth_DATE) {
        this.birth_DATE = birth_DATE;
    }
}
