package com.henghao.hhworkpresent.entity;

/**
 * Created by ASUS on 2017/9/26.
 */

public class PersonnelEntity {

    /**
     * headPath : null
     * dept : null
     * jurisdiction : null
     * id : HZ8bb0c95ce22e77015ce25ddd100319
     * password : null
     * name : 陆建
     */

    private Object headPath;
    private Object dept;
    private Object jurisdiction;
    private String id;
    private Object password;
    private String name;

    public Object getHeadPath() {
        return headPath;
    }

    public void setHeadPath(Object headPath) {
        this.headPath = headPath;
    }

    public Object getDept() {
        return dept;
    }

    public void setDept(Object dept) {
        this.dept = dept;
    }

    public Object getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(Object jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PersonnelEntity{" +
                "headPath=" + headPath +
                ", dept=" + dept +
                ", jurisdiction=" + jurisdiction +
                ", id='" + id + '\'' +
                ", password=" + password +
                ", name='" + name + '\'' +
                '}';
    }

}
