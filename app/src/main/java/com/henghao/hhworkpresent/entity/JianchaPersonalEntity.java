package com.henghao.hhworkpresent.entity;

import java.io.Serializable;

/**
 * 检查人员实体类
 * Created by ASUS on 2017/9/8.
 */

public class JianchaPersonalEntity implements Serializable {

    /**
     * id : e25aa36a288c435597d6f2bebffcc0fe
     * cidno : 111111
     * troopname : 应急救援处(协调办)
     * role : 3
     * name : 李俊德
     * loginid : lijunde
     * password : ec52449df1457e38ae45a873b3165456
     * phone : null
     */

    private String id;
    private String cidno;
    private String troopname;
    private int role;
    private String name;
    private String loginid;
    private String password;
    private String phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCidno() {
        return cidno;
    }

    public void setCidno(String cidno) {
        this.cidno = cidno;
    }

    public String getTroopname() {
        return troopname;
    }

    public void setTroopname(String troopname) {
        this.troopname = troopname;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "JianchaPersonalEntity{" +
                "id='" + id + '\'' +
                ", cidno='" + cidno + '\'' +
                ", troopname='" + troopname + '\'' +
                ", role=" + role +
                ", name='" + name + '\'' +
                ", loginid='" + loginid + '\'' +
                ", password='" + password + '\'' +
                ", phone=" + phone +
                '}';
    }
}
