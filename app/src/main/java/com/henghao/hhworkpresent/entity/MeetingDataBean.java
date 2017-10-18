package com.henghao.hhworkpresent.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 会议上传实体类
 * Created by ASUS on 2017/9/29.
 */
public class MeetingDataBean implements Serializable{
    /**
     * total : 6
     * list : [{"muid":2,"mid":2,"meetingUploadTheme":"ax","meetingUploadPeople":"aas","meetingUploadStartTime":"afd","meetingUploadDuration":"aa","meetingUploadPlace":"asd","meetingUploadJoinPeople":"aa","meetingUploadQiandaoPeople":"aswd","meetingUploadContent":"aa","meetingUploadSummary":"avcx","meetingUploadImagePath":"aa.jpg"},{"muid":1,"mid":1,"meetingUploadTheme":"qeq","meetingUploadPeople":"eqwe","meetingUploadStartTime":"qwe","meetingUploadDuration":"qwe","meetingUploadPlace":"qw","meetingUploadJoinPeople":"qweqw","meetingUploadQiandaoPeople":"eqe","meetingUploadContent":"qeq","meetingUploadSummary":"qwe","meetingUploadImagePath":"aa.jpg"}]
     */

    private int total;
    private List<MeetingUploadEntity> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<MeetingUploadEntity> getList() {
        return list;
    }

    public void setList(List<MeetingUploadEntity> list) {
        this.list = list;
    }

    public static class MeetingUploadEntity implements Serializable{
        private int muid;        //上传会议的id
        private int mid;        //预约会议id
        private String meetingUploadTheme;      //会议上传主题
        private String meetingUploadPeople;      //会议组织人 发起人
        private String meetingUploadStartTime;      //会议举行开始时间
        private String meetingUploadDuration;      //会议举行时长
        private String meetingUploadPlace;      //会议举行地点
        private String meetingUploadJoinPeople;     //会议应到场人员
        private String meetingUploadQiandaoPeople;      //会议到场签到人员
        private String meetingUploadContent;      //会议的内容
        private String meetingUploadSummary;      //会议总结
        private String meetingUploadImagePath;      //会议现场所有图片的路径

        public int getMuid() {
            return muid;
        }

        public void setMuid(int muid) {
            this.muid = muid;
        }

        public String getMeetingUploadImagePath() {
            return meetingUploadImagePath;
        }

        public void setMeetingUploadImagePath(String meetingUploadImagePath) {
            this.meetingUploadImagePath = meetingUploadImagePath;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public String getMeetingUploadTheme() {
            return meetingUploadTheme;
        }

        public void setMeetingUploadTheme(String meetingUploadTheme) {
            this.meetingUploadTheme = meetingUploadTheme;
        }

        public String getMeetingUploadPeople() {
            return meetingUploadPeople;
        }

        public void setMeetingUploadPeople(String meetingUploadPeople) {
            this.meetingUploadPeople = meetingUploadPeople;
        }

        public String getMeetingUploadStartTime() {
            return meetingUploadStartTime;
        }

        public void setMeetingUploadStartTime(String meetingUploadStartTime) {
            this.meetingUploadStartTime = meetingUploadStartTime;
        }

        public String getMeetingUploadPlace() {
            return meetingUploadPlace;
        }

        public void setMeetingUploadPlace(String meetingUploadPlace) {
            this.meetingUploadPlace = meetingUploadPlace;
        }

        public String getMeetingUploadDuration() {
            return meetingUploadDuration;
        }

        public void setMeetingUploadDuration(String meetingUploadDuration) {
            this.meetingUploadDuration = meetingUploadDuration;
        }

        public String getMeetingUploadQiandaoPeople() {
            return meetingUploadQiandaoPeople;
        }

        public void setMeetingUploadQiandaoPeople(String meetingUploadQiandaoPeople) {
            this.meetingUploadQiandaoPeople = meetingUploadQiandaoPeople;
        }

        public String getMeetingUploadJoinPeople() {
            return meetingUploadJoinPeople;
        }

        public void setMeetingUploadJoinPeople(String meetingUploadJoinPeople) {
            this.meetingUploadJoinPeople = meetingUploadJoinPeople;
        }

        public String getMeetingUploadContent() {
            return meetingUploadContent;
        }

        public void setMeetingUploadContent(String meetingUploadContent) {
            this.meetingUploadContent = meetingUploadContent;
        }

        public String getMeetingUploadSummary() {
            return meetingUploadSummary;
        }

        public void setMeetingUploadSummary(String meetingUploadSummary) {
            this.meetingUploadSummary = meetingUploadSummary;
        }

        @Override
        public String toString() {
            return "MeetingUploadEntity{" +
                    "muid=" + muid +
                    ", mid=" + mid +
                    ", meetingUploadTheme='" + meetingUploadTheme + '\'' +
                    ", meetingUploadPeople='" + meetingUploadPeople + '\'' +
                    ", meetingUploadStartTime='" + meetingUploadStartTime + '\'' +
                    ", meetingUploadDuration='" + meetingUploadDuration + '\'' +
                    ", meetingUploadPlace='" + meetingUploadPlace + '\'' +
                    ", meetingUploadJoinPeople='" + meetingUploadJoinPeople + '\'' +
                    ", meetingUploadQiandaoPeople='" + meetingUploadQiandaoPeople + '\'' +
                    ", meetingUploadContent='" + meetingUploadContent + '\'' +
                    ", meetingUploadSummary='" + meetingUploadSummary + '\'' +
                    ", meetingUploadImagePath='" + meetingUploadImagePath + '\'' +
                    '}';
        }
    }
}
