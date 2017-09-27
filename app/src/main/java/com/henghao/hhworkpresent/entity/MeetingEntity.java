package com.henghao.hhworkpresent.entity;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by ASUS on 2017/9/26.
 */

public class MeetingEntity {

    private int mid;    //会议id
    private String uid;     //发起会议人的id
    private String meetingTheme;
    private String meetingStartTime;
    private String meetingDuration;
    private List<PersonnelEntity> meetingPeople;    //参会人员
    private int whetherPass;   //是否通过审批   默认0未通过， 1：通过审批

    private String userIds;   //参会人员id

    private List<JPushToUser> jPushToUser;    //参会人员

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

    public static class PersonnelEntity {

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
    }


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}