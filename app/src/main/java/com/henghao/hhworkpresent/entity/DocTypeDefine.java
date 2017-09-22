package com.henghao.hhworkpresent.entity;

/**
 * 执法文书定义表
 * 关联执法文书对应的表的关联实体类
 * Created by ASUS on 2017/9/21.
 */
public class DocTypeDefine {

    private int dtid;
    private String docTypeName; //文书名称
    private int pid; //计划id
    private int wtid;  //文书id
    private String dataTableName;  //文书对应表名
    private String entityClass; //文书对应实体类
    private String htmlUrl;    //文书对应的web页面

    public int getDtid() {
        return dtid;
    }

    public void setDtid(int dtid) {
        this.dtid = dtid;
    }

    public String getDocTypeName() {
        return docTypeName;
    }

    public void setDocTypeName(String docTypeName) {
        this.docTypeName = docTypeName;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setWtid(int wtid) {
        this.wtid = wtid;
    }

    public int getWtid() {
        return wtid;
    }

    public String getDataTableName() {
        return dataTableName;
    }

    public void setDataTableName(String dataTableName) {
        this.dataTableName = dataTableName;
    }

    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    @Override
    public String toString() {
        return "DocTypeDefine{" +
                "dtid=" + dtid +
                ", docTypeName='" + docTypeName + '\'' +
                ", pid=" + pid +
                ", wtid=" + wtid +
                ", dataTableName='" + dataTableName + '\'' +
                ", entityClass='" + entityClass + '\'' +
                ", htmlUrl='" + htmlUrl + '\'' +
                '}';
    }
}
