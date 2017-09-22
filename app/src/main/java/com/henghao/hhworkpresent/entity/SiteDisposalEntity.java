package com.henghao.hhworkpresent.entity;

import java.util.List;

/**
 * 现场处置决定文书实体类
 * Created by ASUS on 2017/9/21.
 */

public class SiteDisposalEntity {

    private String checkUnit;       //被检查单位
    private String checkTime;       //现场检查时间
    private List<SaveCheckTaskEntity.JianchaMaterialEntityListBean> checkYinhuanList;  //检查隐患的集合 里面显示描述信息
    private String measuresBasics;      //现场处理措施依据
    private String measuresDecision;      //处理措施决定
    private String governmentName1;  //政府名字
    private String mechanism;        //什么机构
    private String courtName;        //法院名字
    private String checkPeople1;     //检查执法人员1
    private String documentsId1;    //证件号码1
    private String checkPeople2;     //检查执法人员2
    private String documentsId2;    //证件号码2
    private String beCheckedPeople; //被检查负责人
    private String recordingTime;   //文书记录时间

}
