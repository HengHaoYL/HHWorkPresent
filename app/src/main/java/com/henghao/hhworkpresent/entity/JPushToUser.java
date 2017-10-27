package com.henghao.hhworkpresent.entity;

import java.io.Serializable;

/**
 * Created by ASUS on 2017/9/27.
 */

public class JPushToUser implements Serializable {

    private int cid;
    private int mid;    //会议Id
    private String uid; //消息接收人id
    private int isRead; //0 未查看 ，1已查看
    private int type; //1 代表发给领导，2 发给自己， 3代表通知大家开会
    private long msg_id;  //推送消息唯一标示
    private String messageTitle;    //消息头
    private String messageContent;	//消息内容
    private String messageSendTime; //发起会议审批时间
    private String messageSendPeople; //发起会议人名字

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessageSendPeople() {
        return messageSendPeople;
    }

    public void setMessageSendPeople(String messageSendPeople) {
        this.messageSendPeople = messageSendPeople;
    }

    public String getMessageSendTime() {
        return messageSendTime;
    }

    public void setMessageSendTime(String messageSendTime) {
        this.messageSendTime = messageSendTime;
    }

    public long getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(long msg_id) {
        this.msg_id = msg_id;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    @Override
    public String toString() {
        return "JPushToUser{" +
                "cid=" + cid +
                ", mid=" + mid +
                ", uid='" + uid + '\'' +
                ", isRead=" + isRead +
                ", type=" + type +
                ", msg_id=" + msg_id +
                ", messageTitle='" + messageTitle + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", messageSendTime='" + messageSendTime + '\'' +
                ", messageSendPeople='" + messageSendPeople + '\'' +
                '}';
    }
}
