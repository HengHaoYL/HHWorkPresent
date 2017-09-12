package com.henghao.hhworkpresent.entity;

/**
 * 执法队伍实体类
 * Created by ASUS on 2017/9/11.
 */

public class JianchaTeamEntity {

    /**
     * id : 4fc59e7b11944d0aac8d7fbab7cfba33
     * troopname : 职监科
     */

    private String id;
    private String troopname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTroopname() {
        return troopname;
    }

    public void setTroopname(String troopname) {
        this.troopname = troopname;
    }

    @Override
    public String toString() {
        return "JianchaTeamEntity{" +
                "id='" + id + '\'' +
                ", troopname='" + troopname + '\'' +
                '}';
    }
}
