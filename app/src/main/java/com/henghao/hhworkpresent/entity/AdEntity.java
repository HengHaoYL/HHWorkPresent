/* 
 * 文件名：AdEntity.java
 * 版权：Copyright 2009-2010 companyName MediaNet. Co. Ltd. All Rights Reserved. 
 * 描述： 
 * 修改人：
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.henghao.hhworkpresent.entity;

import com.google.gson.annotations.Expose;

/**
 * 〈一句话功能简述〉 〈功能详细描述〉
 * @author qyl
 * @version HDMNV100R001, 2015年5月10日
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class AdEntity extends IdEntity {

    /** 点击类型 */
    @Expose
    private Integer adType;

    /** 广告名称 */
    @Expose
    private String adName;

    /** 广告链接 */
    @Expose
    private String adLink;

    /** 广告图片 */
    @Expose
    private String adImage;

    /** 广告描述 */
    @Expose
    private String adDescribe;

    @Expose
    private String adImageUrl;

    public String getAdImageUrl() {
        return adImageUrl;
    }

    public void setAdImageUrl(String adImageUrl) {
        this.adImageUrl = adImageUrl;
    }

    public Integer getAdType() {
        return adType;
    }

    public void setAdType(Integer adType) {
        this.adType = adType;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getAdLink() {
        return adLink;
    }

    public void setAdLink(String adLink) {
        this.adLink = adLink;
    }

    public String getAdImage() {
        return adImage;
    }

    public void setAdImage(String adImage) {
        this.adImage = adImage;
    }

    public String getAdDescribe() {
        return adDescribe;
    }

    public void setAdDescribe(String adDescribe) {
        this.adDescribe = adDescribe;
    }
}
