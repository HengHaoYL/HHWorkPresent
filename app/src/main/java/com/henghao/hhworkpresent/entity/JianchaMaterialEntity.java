package com.henghao.hhworkpresent.entity;

import java.io.Serializable;

/**
 * 检查材料、文件实体类
 * Created by ASUS on 2017/9/8.
 */

public class JianchaMaterialEntity implements Serializable {

    private String title;
    private String descript;

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

    @Override
    public String toString() {
        return "JianchaMaterialEntity{" +
                "title='" + title + '\'' +
                ", descript='" + descript + '\'' +
                '}';
    }
}
