package com.henghao.hhworkpresent.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by bryanrady on 2017/3/15.
 *
 * 公告实体类
 */

public class GonggaoEntity extends IdEntity {

    /**
     * 公告id
     */
    @Expose
    private Integer gid;

    @Expose
    private String gonggao_title;

    @Expose
    private String gonggao_author;

    @Expose
    private String gonggao_content;

    /**
     * 公告封面图片
     */
    @Expose
    private String gonggao_imageUrl;

    /**
     * 公告发布日期
     */
    @Expose
    private String gonggao_sendDate;

    /**
     * 公告是否加密  0:不加密  1:加密
     */
    @Expose
    private String isEncrpt;

    /**
     * 公告是否已读  0:未读  1:已读
     */
    @Expose
    private String isRead;


    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getGonggao_titile() {
        return gonggao_title;
    }

    public void setGonggao_titile(String gonggao_titile) {
        this.gonggao_title = gonggao_titile;
    }

    public String getGonggao_content() {
        return gonggao_content;
    }

    public void setGonggao_content(String gonggao_content) {
        this.gonggao_content = gonggao_content;
    }

    public String getGonggao_author() {
        return gonggao_author;
    }

    public void setGonggao_author(String gonggao_author) {
        this.gonggao_author = gonggao_author;
    }

    public String getGonggao_imageUrl() {
        return gonggao_imageUrl;
    }

    public void setGonggao_imageUrl(String gongao_imageUrl) {
        this.gonggao_imageUrl = gongao_imageUrl;
    }

    public String getGonggao_sendDate() {
        return gonggao_sendDate;
    }

    public void setGonggao_sendDate(String gonggao_date) {
        this.gonggao_sendDate = gonggao_date;
    }

    public String getIsEncrpt() {
        return isEncrpt;
    }

    public void setIsEncrpt(String isEncrpt) {
        this.isEncrpt = isEncrpt;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}
