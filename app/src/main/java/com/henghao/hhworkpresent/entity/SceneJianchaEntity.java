package com.henghao.hhworkpresent.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 现场检查文书里面的标签字段
 * Created by ASUS on 2017/9/14.
 */

public class SceneJianchaEntity implements Serializable {

    private String checkUnit;      //被检查单位
    private String checkAddress;      //单位地址
    private String legalRepresentative;   //法定代表人
    private String legalDuty;           //法定代表人职务
    private String contactNumber;    //联系电话
    private String checkSite;     //检查场所
    private String checkTime;    //检查时间
    private String cityName;    //市名字
    private String checkPeople1;    //检查人员1
    private String checkPeople2;    //检查人员2
    private String documentsId1;    //证件号码1
    private String documentsId2;    //证件号码2
    private String checkCase;       //检查情况
    private List<JianchaYinhuanEntity> checkYinhuanList;  //检查隐患的集合
    private String checkSignature11; //检查人员签名1
    private String checkSignature12; //检查人员签名2
    private String beCheckedPeople; //被检查负责人
    private String recordingTime;   //文书记录时间

    public String getCheckUnit() {
        return checkUnit;
    }

    public void setCheckUnit(String checkUnit) {
        this.checkUnit = checkUnit;
    }

    public String getCheckAddress() {
        return checkAddress;
    }

    public void setCheckAddress(String checkAddress) {
        this.checkAddress = checkAddress;
    }

    public String getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
    }

    public String getCheckSite() {
        return checkSite;
    }

    public void setCheckSite(String checkSite) {
        this.checkSite = checkSite;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCheckPeople1() {
        return checkPeople1;
    }

    public void setCheckPeople1(String checkPeople1) {
        this.checkPeople1 = checkPeople1;
    }

    public String getCheckPeople2() {
        return checkPeople2;
    }

    public void setCheckPeople2(String checkPeople2) {
        this.checkPeople2 = checkPeople2;
    }

    public String getDocumentsId1() {
        return documentsId1;
    }

    public void setDocumentsId1(String documentsId1) {
        this.documentsId1 = documentsId1;
    }

    public String getCheckCase() {
        return checkCase;
    }

    public void setCheckCase(String checkCase) {
        this.checkCase = checkCase;
    }

    public String getDocumentsId2() {
        return documentsId2;
    }

    public void setDocumentsId2(String documentsId2) {
        this.documentsId2 = documentsId2;
    }

    public List<JianchaYinhuanEntity> getCheckYinhuanList() {
        return checkYinhuanList;
    }

    public void setCheckYinhuanList(List<JianchaYinhuanEntity> checkYinhuanList) {
        this.checkYinhuanList = checkYinhuanList;
    }

    public String getCheckSignature11() {
        return checkSignature11;
    }

    public void setCheckSignature11(String checkSignature11) {
        this.checkSignature11 = checkSignature11;
    }

    public String getCheckSignature12() {
        return checkSignature12;
    }

    public void setCheckSignature12(String checkSignature12) {
        this.checkSignature12 = checkSignature12;
    }

    public String getBeCheckedPeople() {
        return beCheckedPeople;
    }

    public void setBeCheckedPeople(String beCheckedPeople) {
        this.beCheckedPeople = beCheckedPeople;
    }

    public String getRecordingTime() {
        return recordingTime;
    }

    public void setRecordingTime(String recordingTime) {
        this.recordingTime = recordingTime;
    }

    public String getLegalDuty() {
        return legalDuty;
    }

    public void setLegalDuty(String legalDuty) {
        this.legalDuty = legalDuty;
    }

    @Override
    public String toString() {
        return "SceneJianchaEntity{" +
                "checkUnit='" + checkUnit + '\'' +
                ", checkAddress='" + checkAddress + '\'' +
                ", legalRepresentative='" + legalRepresentative + '\'' +
                ", legalDuty='" + legalDuty + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", checkSite='" + checkSite + '\'' +
                ", checkTime='" + checkTime + '\'' +
                ", cityName='" + cityName + '\'' +
                ", checkPeople1='" + checkPeople1 + '\'' +
                ", checkPeople2='" + checkPeople2 + '\'' +
                ", documentsId1='" + documentsId1 + '\'' +
                ", documentsId2='" + documentsId2 + '\'' +
                ", checkCase='" + checkCase + '\'' +
                ", checkYinhuanList=" + checkYinhuanList +
                ", checkSignature11='" + checkSignature11 + '\'' +
                ", checkSignature12='" + checkSignature12 + '\'' +
                ", beCheckedPeople='" + beCheckedPeople + '\'' +
                ", recordingTime='" + recordingTime + '\'' +
                '}';
    }
}
