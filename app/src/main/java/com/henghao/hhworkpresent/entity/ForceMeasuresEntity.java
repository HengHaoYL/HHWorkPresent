package com.henghao.hhworkpresent.entity;

import java.util.List;

/**
 * 强制措施决定书
 * Created by ASUS on 2017/9/21.
 */

public class ForceMeasuresEntity {

    private String checkUnit;       //被检查单位
    private List<SaveCheckTaskEntity.JianchaMaterialEntityListBean> checkYinhuanList;  //检查隐患的集合 里面显示描述信息
    private String measuresBasics;      //强制措施依据
    private String measures;            //强制措施
    private String governmentName1;  //政府名字
    private String mechanism;        //什么机构
    private String courtName;        //法院名字
    private String recordingTime;   //文书记录时间

}
