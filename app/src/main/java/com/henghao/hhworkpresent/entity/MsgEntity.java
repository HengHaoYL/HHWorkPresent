package com.henghao.hhworkpresent.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by bryanrady on 2017/4/17.
 */

public class MsgEntity extends IdEntity {

    @Expose
    private String sendusername;

    @Expose
    private String sendtime;

    @Expose
    private String title;

    public String getSendusername() {
        return sendusername;
    }

    public void setSendusername(String sendusername) {
        this.sendusername = sendusername;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
