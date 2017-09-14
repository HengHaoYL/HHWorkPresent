package com.henghao.hhworkpresent.entity;

import java.io.Serializable;

/**
 * 被选中的隐患排查
 * Created by ASUS on 2017/9/14.
 */

public class JianchaYinhuanEntity implements Serializable {

    private String threat_time;
    private String threat_degree;
    private String threat_position;
    private String threat_description;
    private String threat_imagepath;

    public String getThreat_time() {
        return threat_time;
    }

    public void setThreat_time(String threat_time) {
        this.threat_time = threat_time;
    }

    public String getThreat_degree() {
        return threat_degree;
    }

    public void setThreat_degree(String threat_degree) {
        this.threat_degree = threat_degree;
    }

    public String getThreat_description() {
        return threat_description;
    }

    public void setThreat_description(String threat_description) {
        this.threat_description = threat_description;
    }

    public String getThreat_imagepath() {
        return threat_imagepath;
    }

    public void setThreat_imagepath(String threat_imagepath) {
        this.threat_imagepath = threat_imagepath;
    }

    public String getThreat_position() {
        return threat_position;
    }

    public void setThreat_position(String threat_position) {
        this.threat_position = threat_position;
    }

    @Override
    public String toString() {
        return "JianchaYinhuanEntity{" +
                "threat_time='" + threat_time + '\'' +
                ", threat_degree='" + threat_degree + '\'' +
                ", threat_position='" + threat_position + '\'' +
                ", threat_description='" + threat_description + '\'' +
                ", threat_imagepath='" + threat_imagepath + '\'' +
                '}';
    }
}
