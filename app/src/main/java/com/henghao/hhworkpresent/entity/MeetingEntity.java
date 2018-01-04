package com.henghao.hhworkpresent.entity;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ASUS on 2017/9/26.
 */

public class MeetingEntity implements Serializable {

    private int mid;    //会议id
    private String uid;     //发起会议人的id
    private String meetingTheme;
    private String meetingStartTime;
    private String meetingDuration;
    private String meetingPlace;
    private String meetingType;
    private String wifiSSID;
    private List<PersonnelEntity> meetingPeople;    //参会人员
    private int whetherPass;   //是否通过审批   数据库默认是0   拒绝通过审批:2， 通过审批：1
    private String noPassReason;  //审批不通过理由
    private String leadId;      //审批人的id
    private String leadName;    //审批领导名字  也就是审批人
    private String userIds;   //参会人员id
    private List<JPushToUser> jPushToUser;    //推送消息中间类
    private MeetingTrajectoryEntity meetingTrajectoryEntity;
    private int isEnd;  //表示会议是否结束 1表示已经结束 0表示未结束


    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setMeetingType(String meetingType) {
        this.meetingType = meetingType;
    }

    public String getMeetingType() {
        return meetingType;
    }

    public void setMeetingTrajectoryEntity(MeetingTrajectoryEntity meetingTrajectoryEntity) {
        this.meetingTrajectoryEntity = meetingTrajectoryEntity;
    }

    public MeetingTrajectoryEntity getMeetingTrajectoryEntity() {
        return meetingTrajectoryEntity;
    }

    public String getWifiSSID() {
        return wifiSSID;
    }

    public void setWifiSSID(String wifiSSID) {
        this.wifiSSID = wifiSSID;
    }

    public String getLeadName() {
        return leadName;
    }

    public void setLeadName(String leadName) {
        this.leadName = leadName;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public List<JPushToUser> getjPushToUser() {
        return jPushToUser;
    }

    public void setjPushToUser(List<JPushToUser> jPushToUser) {
        this.jPushToUser = jPushToUser;
    }

    public String getMeetingDuration() {
        return meetingDuration;
    }

    public void setMeetingDuration(String meetingDuration) {
        this.meetingDuration = meetingDuration;
    }

    public void setMeetingPlace(String meetingPlace) {
        this.meetingPlace = meetingPlace;
    }

    public String getMeetingPlace() {
        return meetingPlace;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMeetingStartTime() {
        return meetingStartTime;
    }

    public void setMeetingStartTime(String meetingStartTime) {
        this.meetingStartTime = meetingStartTime;
    }

    public String getMeetingTheme() {
        return meetingTheme;
    }

    public void setMeetingTheme(String meetingTheme) {
        this.meetingTheme = meetingTheme;
    }

    public List<PersonnelEntity> getMeetingPeople() {
        return meetingPeople;
    }

    public void setMeetingPeople(List<PersonnelEntity> meetingPeople) {
        this.meetingPeople = meetingPeople;
    }

    public void setNoPassReason(String noPassReason) {
        this.noPassReason = noPassReason;
    }

    public String getNoPassReason() {
        return noPassReason;
    }

    public int getWhetherPass() {
        return whetherPass;
    }

    public void setWhetherPass(int whetherPass) {
        this.whetherPass = whetherPass;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public int getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(int isEnd) {
        this.isEnd = isEnd;
    }

    public static class PersonnelEntity implements Serializable {

        /**
         * headPath : null
         * dept : null
         * jurisdiction : null
         * id : HZ8bb0c95ce22e77015ce25ddd100319
         * password : null
         * name : 陆建
         */

        private String id;
        private String name;
        private String password;
        private String headPath;
        private String dept;
        private String login_name;

        public String getHeadPath() {
            return headPath;
        }

        public void setHeadPath(String headPath) {
            this.headPath = headPath;
        }

        public String getDept() {
            return dept;
        }

        public void setDept(String dept) {
            this.dept = dept;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogin_name() {
            return login_name;
        }

        public void setLogin_name(String login_name) {
            this.login_name = login_name;
        }

        @Override
        public String toString() {
            return JSONObject.toJSONString(this);
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null){
                return false;
            }
            if(obj == this){
                return true;
            }
            if(obj instanceof PersonnelEntity){
                PersonnelEntity p = (PersonnelEntity) obj;
                if(p.id.equals(this.id) && p.name.equals(this.name)){
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            return id.hashCode() * name.hashCode();
        }
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
