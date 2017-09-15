package com.henghao.hhworkpresent.entity;

import java.io.Serializable;

/**
 * 检查人员实体类
 * Created by ASUS on 2017/9/8.
 */

public class JianchaPersonalEntity implements Serializable {

    /**
     * id : HZ8bb0c95c19c59a015c1a7cf0ee091f
     * name : 测试贰
     * emp_NUM :
     * loginid : gyajj.test2
     * phone :
     * troopname : 贵阳市安监局
     * password : E7F5C4BB10F6CA72
     */

    private String id;
    private String name;
    private String emp_NUM;
    private String loginid;
    private String phone;
    private String troopname;
    private String password;

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

    public String getEmp_NUM() {
        return emp_NUM;
    }

    public void setEmp_NUM(String emp_NUM) {
        this.emp_NUM = emp_NUM;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTroopname() {
        return troopname;
    }

    public void setTroopname(String troopname) {
        this.troopname = troopname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "JianchaPersonalEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", emp_NUM='" + emp_NUM + '\'' +
                ", loginid='" + loginid + '\'' +
                ", phone='" + phone + '\'' +
                ", troopname='" + troopname + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
