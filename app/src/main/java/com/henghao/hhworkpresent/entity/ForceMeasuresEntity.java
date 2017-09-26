package com.henghao.hhworkpresent.entity;

/**
 * 强制措施决定书
 * Created by ASUS on 2017/9/21.
 */

public class ForceMeasuresEntity {

    private int wtid;
    private String checkUnit;       //被检查单位
    private String checkYinhuanList;  //检查隐患的集合 里面显示描述信息 改成字符串
    private String measuresBasics;      //强制措施依据
    private String measures;            //强制措施
    private String governmentName1;  //政府名字
    private String mechanism;        //什么机构
    private String courtName;        //法院名字
    private String recordingTime;   //文书记录时间

    public int getWtid() {
        return wtid;
    }

    public void setWtid(int wtid) {
        this.wtid = wtid;
    }

    public String getCheckYinhuanList() {
        return checkYinhuanList;
    }

    public void setCheckYinhuanList(String checkYinhuanList) {
        this.checkYinhuanList = checkYinhuanList;
    }

    public String getCheckUnit() {
        return checkUnit;
    }

    public void setCheckUnit(String checkUnit) {
        this.checkUnit = checkUnit;
    }

    public String getMeasuresBasics() {
        return measuresBasics;
    }

    public void setMeasuresBasics(String measuresBasics) {
        this.measuresBasics = measuresBasics;
    }

    public String getMeasures() {
        return measures;
    }

    public void setMeasures(String measures) {
        this.measures = measures;
    }

    public String getGovernmentName1() {
        return governmentName1;
    }

    public void setGovernmentName1(String governmentName1) {
        this.governmentName1 = governmentName1;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public String getMechanism() {
        return mechanism;
    }

    public void setMechanism(String mechanism) {
        this.mechanism = mechanism;
    }

    public String getRecordingTime() {
        return recordingTime;
    }

    public void setRecordingTime(String recordingTime) {
        this.recordingTime = recordingTime;
    }

    @Override
    public String toString() {
        return "ForceMeasuresEntity{" +
                "wtid=" + wtid +
                ", checkUnit='" + checkUnit + '\'' +
                ", checkYinhuanList='" + checkYinhuanList + '\'' +
                ", measuresBasics='" + measuresBasics + '\'' +
                ", measures='" + measures + '\'' +
                ", governmentName1='" + governmentName1 + '\'' +
                ", mechanism='" + mechanism + '\'' +
                ", courtName='" + courtName + '\'' +
                ", recordingTime='" + recordingTime + '\'' +
                '}';
    }
}
