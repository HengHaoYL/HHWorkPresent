package com.henghao.hhworkpresent.entity;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * 添加检查任务界面点击保存后要提交的数据对象  再把我要检查界面需要提交的数据封装
 * Created by ASUS on 2017/9/15.
 */

public class SaveCheckTaskEntity implements Serializable {

    /**
     * pid : 3
     * company_name : 中国石油天然气股份有限公司贵州贵阳销售分公司东站路加油四站
     * checkPeople1 : 朱智刚
     * checkPeople2 : 姜宇
     * checkTime : 2017-09-17
     * createPlanTime : null
     * jianchaMaterialEntityList : [{"cid":5,"title":"1营业执照","descript":"1申请人凭批准书向工商行政管理部门办理登记注册手续。","findTime":null,"checkDegree":null,"checkPosition":null,"checkDescript":null,"selectImagePath":null},{"cid":6,"title":"1安全生产相关资质","descript":"1是否有安全生产相关资质证照，是否超期。如果不需取得相关资质，请注明。","findTime":null,"checkDegree":null,"checkPosition":null,"checkDescript":null,"selectImagePath":null}]
     * checkSite : null
     * siteResponse : null
     * siteImagePath : null
     * finishTime : null
     */

    private int pid;
    private String company_name;
    private String checkPeople1;
    private String checkPeople2;
    private String checkTime;
    private String createPlanTime;
    private String checkSite;
    private String siteResponse;
    private String siteImagePath;
    private String finishTime;
    private List<JianchaMaterialEntityListBean> jianchaMaterialEntityList;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
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

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getCreatePlanTime() {
        return createPlanTime;
    }

    public void setCreatePlanTime(String createPlanTime) {
        this.createPlanTime = createPlanTime;
    }

    public String getCheckSite() {
        return checkSite;
    }

    public void setCheckSite(String checkSite) {
        this.checkSite = checkSite;
    }

    public String getSiteResponse() {
        return siteResponse;
    }

    public void setSiteResponse(String siteResponse) {
        this.siteResponse = siteResponse;
    }

    public String getSiteImagePath() {
        return siteImagePath;
    }

    public void setSiteImagePath(String siteImagePath) {
        this.siteImagePath = siteImagePath;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public List<JianchaMaterialEntityListBean> getJianchaMaterialEntityList() {
        return jianchaMaterialEntityList;
    }

    public void setJianchaMaterialEntityList(List<JianchaMaterialEntityListBean> jianchaMaterialEntityList) {
        this.jianchaMaterialEntityList = jianchaMaterialEntityList;
    }

    public static class JianchaMaterialEntityListBean implements Serializable {
        /**
         * cid : 5
         * title : 1营业执照
         * descript : 1申请人凭批准书向工商行政管理部门办理登记注册手续。
         * findTime : null
         * checkDegree : null
         * checkPosition : null
         * checkDescript : null
         * selectImagePath : null
         */

        private int cid;
        private String title;
        private String descript;
        private String findTime;
        private String checkDegree;
        private String checkPosition;
        private String checkDescript;
        private String selectImagePath;

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

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

        public String getFindTime() {
            return findTime;
        }

        public void setFindTime(String findTime) {
            this.findTime = findTime;
        }

        public String getCheckDegree() {
            return checkDegree;
        }

        public void setCheckDegree(String checkDegree) {
            this.checkDegree = checkDegree;
        }

        public String getCheckPosition() {
            return checkPosition;
        }

        public void setCheckPosition(String checkPosition) {
            this.checkPosition = checkPosition;
        }

        public String getCheckDescript() {
            return checkDescript;
        }

        public void setCheckDescript(String checkDescript) {
            this.checkDescript = checkDescript;
        }

        public String getSelectImagePath() {
            return selectImagePath;
        }

        public void setSelectImagePath(String selectImagePath) {
            this.selectImagePath = selectImagePath;
        }
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
