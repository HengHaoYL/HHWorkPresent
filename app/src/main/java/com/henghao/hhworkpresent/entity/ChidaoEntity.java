package com.henghao.hhworkpresent.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by bryanrady on 2017/3/29.
 */

public class ChidaoEntity extends IdEntity {


    /**
     * 迟到日期
     */
    @Expose
    private String chidao_Date;

    /**
     * 迟到星期
     */
    @Expose
    private String chidao_Week;

    /**
     * 迟到上班打卡时间
     */
    @Expose
    private String chidao_checkTime;

    /**
     * 迟到日期
     */
    @Expose
    private String should_checkTime;


    public String getChidao_Date() {
        return chidao_Date;
    }

    public void setChidao_Date(String chidao_Date) {
        this.chidao_Date = chidao_Date;
    }

    public String getChidao_Week() {
        return chidao_Week;
    }

    public void setChidao_Week(String chidao_Week) {
        this.chidao_Week = chidao_Week;
    }

    public String getChidao_checkTime() {
        return chidao_checkTime;
    }

    public void setChidao_checkTime(String chidao_checkTime) {
        this.chidao_checkTime = chidao_checkTime;
    }

    public String getShould_checkTime() {
        return should_checkTime;
    }

    public void setShould_checkTime(String should_checkTime) {
        this.should_checkTime = should_checkTime;
    }
}
