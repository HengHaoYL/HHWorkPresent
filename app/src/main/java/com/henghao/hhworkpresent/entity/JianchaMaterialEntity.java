package com.henghao.hhworkpresent.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * 检查材料、文件实体类 并且还包含隐患内容
 * Created by ASUS on 2017/9/8.
 */

public class JianchaMaterialEntity implements Serializable {

    @JSONField(name="title")
    private String title;
    @JSONField(name="descript")
    private String descript;
    @JSONField(name="findTime")
    private String findTime;    //发现时间
    @JSONField(name="checkDegree")
    private String checkDegree; //隐患级别
    @JSONField(name="checkPosition")
    private String checkPosition;   //隐患部位
    @JSONField(name="checkDescript")
    private String checkDescript;   //隐患描述
    @JSONField(name="selectImagePath")
    private List<String> selectImagePath;   //隐患图片

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getCheckPosition() {
        return checkPosition;
    }

    public void setCheckPosition(String checkPosition) {
        this.checkPosition = checkPosition;
    }

    public List<String> getSelectImagePath() {
        return selectImagePath;
    }

    public void setSelectImagePath(List<String> selectImagePath) {
        this.selectImagePath = selectImagePath;
    }

    public String getCheckDescript() {
        return checkDescript;
    }

    public void setCheckDescript(String checkDescript) {
        this.checkDescript = checkDescript;
    }

    public String getCheckDegree() {
        return checkDegree;
    }

    public void setCheckDegree(String checkDegree) {
        this.checkDegree = checkDegree;
    }

    public String getFindTime() {
        return findTime;
    }

    public void setFindTime(String findTime) {
        this.findTime = findTime;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
