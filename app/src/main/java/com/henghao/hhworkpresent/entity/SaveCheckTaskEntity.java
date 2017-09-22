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
     * pid : 1
     * enterpriseid : 32c46df86b594cb08b7fb7b6faf9d287
     * checkPeople1 : null
     * checkPeople2 : 测试壹
     * checkTime : null
     * createPlanTime : null
     * checkSite : null
     * siteResponse : null
     * siteImagePath : null
     * finishTime : null
     * troopemp : {"id":"HZ8bb0c95c19c59a015c1a7c61390914","troopname":"贵阳市安监局","emp_NUM":"","phone":"12346","loginid":"gyajj.test1","name":"测试壹","password":"abc123."}
     * enterprise : {"enterpriseid":"32c46df86b594cb08b7fb7b6faf9d287","entname":"中国石油天然气股份有限公司贵州贵阳销售分公司东站路加油四站","regnum":"91520102MA6DJQTE3D","district1":"贵州省","district2":"贵阳市","district3":"贵州双龙航空港经济区","district4":null,"hascompany":2,"pcompanyregcode":null,"industry1":"黑色金属矿采选企业(露天)","industry2":"加油站","department":"安监部门","relevel":"县（区）属","enterprisecode":null,"state":1,"createtime":1489562413000,"checktime":1489563408000,"checker":"slgajj.zl","scale":"大型","regaddress":"贵州省贵阳市南明区秦棋村","regtime":1432051200000,"employeenum":"9","longitude":"106.786601","latitude":"26.353003","businesscope":"油品、食品、烟草、日用百货、汽车零配件及农用物资；汽车美容。","post":"550000","legalpeople":"常鸿斌","legalmobilephone":"18585856789","linkman":"刘大权","linkmobilephone":"18684105797","productaddress":"贵州省贵阳市南明区秦棋村","sczch":null,"officeaddress":"贵州省贵阳市南明区秦棋村","zczb":500,"nyysr":1600,"qyzclx":"11","scjycsmj":1000,"zzjgt":null,"dwpmt":null,"superviseclassify":"A006","isabove":1,"sdorgan":"520131","updatetime":1489653776000,"isplan":"有"}
     * jianchaMaterialEntityList : [{"cid":1,"title":"da","descript":"dasdasd ","findTime":null,"checkDegree":null,"checkPosition":null,"checkDescript":null,"selectImagePath":null}]
     */

    private int pid;
    private String enterpriseid;
    private String checkPeople1;
    private String checkPeople2;
    private String checkTime;
    private String createPlanTime;
    private String checkSite;
    private String siteResponse;
    private String siteImagePath;
    private String finishTime;
    private TroopempBean troopemp;
    private EnterpriseBean enterprise;
    private List<JianchaMaterialEntityListBean> jianchaMaterialEntityList;
    private int resultStatus; //结果状态  默认0：我要检查列表， 1：无隐患归档 ，2：转到复查，3：转到调查取证 ，4：

    public void setResultStatus(int resultStatus) {
        this.resultStatus = resultStatus;
    }

    public int getResultStatus() {
        return resultStatus;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getEnterpriseid() {
        return enterpriseid;
    }

    public void setEnterpriseid(String enterpriseid) {
        this.enterpriseid = enterpriseid;
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

    public TroopempBean getTroopemp() {
        return troopemp;
    }

    public void setTroopemp(TroopempBean troopemp) {
        this.troopemp = troopemp;
    }

    public EnterpriseBean getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(EnterpriseBean enterprise) {
        this.enterprise = enterprise;
    }

    public List<JianchaMaterialEntityListBean> getJianchaMaterialEntityList() {
        return jianchaMaterialEntityList;
    }

    public void setJianchaMaterialEntityList(List<JianchaMaterialEntityListBean> jianchaMaterialEntityList) {
        this.jianchaMaterialEntityList = jianchaMaterialEntityList;
    }

    public static class TroopempBean implements Serializable {
        /**
         * id : HZ8bb0c95c19c59a015c1a7c61390914
         * troopname : 贵阳市安监局
         * emp_NUM :
         * phone : 12346
         * loginid : gyajj.test1
         * name : 测试壹
         * password : abc123.
         */

        private String id;
        private String troopname;
        private String emp_NUM;
        private String phone;
        private String loginid;
        private String name;
        private String password;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTroopname() {
            return troopname;
        }

        public void setTroopname(String troopname) {
            this.troopname = troopname;
        }

        public String getEmp_NUM() {
            return emp_NUM;
        }

        public void setEmp_NUM(String emp_NUM) {
            this.emp_NUM = emp_NUM;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getLoginid() {
            return loginid;
        }

        public void setLoginid(String loginid) {
            this.loginid = loginid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return JSONObject.toJSONString(this);
        }
    }

    public static class EnterpriseBean implements Serializable {
        /**
         * enterpriseid : 32c46df86b594cb08b7fb7b6faf9d287
         * entname : 中国石油天然气股份有限公司贵州贵阳销售分公司东站路加油四站
         * regnum : 91520102MA6DJQTE3D
         * district1 : 贵州省
         * district2 : 贵阳市
         * district3 : 贵州双龙航空港经济区
         * district4 : null
         * hascompany : 2
         * pcompanyregcode : null
         * industry1 : 黑色金属矿采选企业(露天)
         * industry2 : 加油站
         * department : 安监部门
         * relevel : 县（区）属
         * enterprisecode : null
         * state : 1
         * createtime : 1489562413000
         * checktime : 1489563408000
         * checker : slgajj.zl
         * scale : 大型
         * regaddress : 贵州省贵阳市南明区秦棋村
         * regtime : 1432051200000
         * employeenum : 9
         * longitude : 106.786601
         * latitude : 26.353003
         * businesscope : 油品、食品、烟草、日用百货、汽车零配件及农用物资；汽车美容。
         * post : 550000
         * legalpeople : 常鸿斌
         * legalmobilephone : 18585856789
         * linkman : 刘大权
         * linkmobilephone : 18684105797
         * productaddress : 贵州省贵阳市南明区秦棋村
         * sczch : null
         * officeaddress : 贵州省贵阳市南明区秦棋村
         * zczb : 500
         * nyysr : 1600
         * qyzclx : 11
         * scjycsmj : 1000
         * zzjgt : null
         * dwpmt : null
         * superviseclassify : A006
         * isabove : 1
         * sdorgan : 520131
         * updatetime : 1489653776000
         * isplan : 有
         */

        private String enterpriseid;
        private String entname;
        private String regnum;
        private String district1;
        private String district2;
        private String district3;
        private String district4;
        private int hascompany;
        private String pcompanyregcode;
        private String industry1;
        private String industry2;
        private String department;
        private String relevel;
        private String enterprisecode;
        private int state;
        private long createtime;
        private long checktime;
        private String checker;
        private String scale;
        private String regaddress;
        private long regtime;
        private String employeenum;
        private String longitude;
        private String latitude;
        private String businesscope;
        private String post;
        private String legalpeople;
        private String legalmobilephone;
        private String linkman;
        private String linkmobilephone;
        private String productaddress;
        private String sczch;
        private String officeaddress;
        private int zczb;
        private int nyysr;
        private String qyzclx;
        private int scjycsmj;
        private String zzjgt;
        private String dwpmt;
        private String superviseclassify;
        private int isabove;
        private String sdorgan;
        private long updatetime;
        private String isplan;

        public String getEnterpriseid() {
            return enterpriseid;
        }

        public void setEnterpriseid(String enterpriseid) {
            this.enterpriseid = enterpriseid;
        }

        public String getEntname() {
            return entname;
        }

        public void setEntname(String entname) {
            this.entname = entname;
        }

        public String getRegnum() {
            return regnum;
        }

        public void setRegnum(String regnum) {
            this.regnum = regnum;
        }

        public String getDistrict1() {
            return district1;
        }

        public void setDistrict1(String district1) {
            this.district1 = district1;
        }

        public String getDistrict2() {
            return district2;
        }

        public void setDistrict2(String district2) {
            this.district2 = district2;
        }

        public String getDistrict3() {
            return district3;
        }

        public void setDistrict3(String district3) {
            this.district3 = district3;
        }

        public String getDistrict4() {
            return district4;
        }

        public void setDistrict4(String district4) {
            this.district4 = district4;
        }

        public int getHascompany() {
            return hascompany;
        }

        public void setHascompany(int hascompany) {
            this.hascompany = hascompany;
        }

        public String getPcompanyregcode() {
            return pcompanyregcode;
        }

        public void setPcompanyregcode(String pcompanyregcode) {
            this.pcompanyregcode = pcompanyregcode;
        }

        public String getIndustry1() {
            return industry1;
        }

        public void setIndustry1(String industry1) {
            this.industry1 = industry1;
        }

        public String getIndustry2() {
            return industry2;
        }

        public void setIndustry2(String industry2) {
            this.industry2 = industry2;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getRelevel() {
            return relevel;
        }

        public void setRelevel(String relevel) {
            this.relevel = relevel;
        }

        public String getEnterprisecode() {
            return enterprisecode;
        }

        public void setEnterprisecode(String enterprisecode) {
            this.enterprisecode = enterprisecode;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public long getChecktime() {
            return checktime;
        }

        public void setChecktime(long checktime) {
            this.checktime = checktime;
        }

        public String getChecker() {
            return checker;
        }

        public void setChecker(String checker) {
            this.checker = checker;
        }

        public String getScale() {
            return scale;
        }

        public void setScale(String scale) {
            this.scale = scale;
        }

        public String getRegaddress() {
            return regaddress;
        }

        public void setRegaddress(String regaddress) {
            this.regaddress = regaddress;
        }

        public long getRegtime() {
            return regtime;
        }

        public void setRegtime(long regtime) {
            this.regtime = regtime;
        }

        public String getEmployeenum() {
            return employeenum;
        }

        public void setEmployeenum(String employeenum) {
            this.employeenum = employeenum;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getBusinesscope() {
            return businesscope;
        }

        public void setBusinesscope(String businesscope) {
            this.businesscope = businesscope;
        }

        public String getPost() {
            return post;
        }

        public void setPost(String post) {
            this.post = post;
        }

        public String getLegalpeople() {
            return legalpeople;
        }

        public void setLegalpeople(String legalpeople) {
            this.legalpeople = legalpeople;
        }

        public String getLegalmobilephone() {
            return legalmobilephone;
        }

        public void setLegalmobilephone(String legalmobilephone) {
            this.legalmobilephone = legalmobilephone;
        }

        public String getLinkman() {
            return linkman;
        }

        public void setLinkman(String linkman) {
            this.linkman = linkman;
        }

        public String getLinkmobilephone() {
            return linkmobilephone;
        }

        public void setLinkmobilephone(String linkmobilephone) {
            this.linkmobilephone = linkmobilephone;
        }

        public String getProductaddress() {
            return productaddress;
        }

        public void setProductaddress(String productaddress) {
            this.productaddress = productaddress;
        }

        public String getSczch() {
            return sczch;
        }

        public void setSczch(String sczch) {
            this.sczch = sczch;
        }

        public String getOfficeaddress() {
            return officeaddress;
        }

        public void setOfficeaddress(String officeaddress) {
            this.officeaddress = officeaddress;
        }

        public int getZczb() {
            return zczb;
        }

        public void setZczb(int zczb) {
            this.zczb = zczb;
        }

        public int getNyysr() {
            return nyysr;
        }

        public void setNyysr(int nyysr) {
            this.nyysr = nyysr;
        }

        public String getQyzclx() {
            return qyzclx;
        }

        public void setQyzclx(String qyzclx) {
            this.qyzclx = qyzclx;
        }

        public int getScjycsmj() {
            return scjycsmj;
        }

        public void setScjycsmj(int scjycsmj) {
            this.scjycsmj = scjycsmj;
        }

        public String getZzjgt() {
            return zzjgt;
        }

        public void setZzjgt(String zzjgt) {
            this.zzjgt = zzjgt;
        }

        public String getDwpmt() {
            return dwpmt;
        }

        public void setDwpmt(String dwpmt) {
            this.dwpmt = dwpmt;
        }

        public String getSuperviseclassify() {
            return superviseclassify;
        }

        public void setSuperviseclassify(String superviseclassify) {
            this.superviseclassify = superviseclassify;
        }

        public int getIsabove() {
            return isabove;
        }

        public void setIsabove(int isabove) {
            this.isabove = isabove;
        }

        public String getSdorgan() {
            return sdorgan;
        }

        public void setSdorgan(String sdorgan) {
            this.sdorgan = sdorgan;
        }

        public long getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(long updatetime) {
            this.updatetime = updatetime;
        }

        public String getIsplan() {
            return isplan;
        }

        public void setIsplan(String isplan) {
            this.isplan = isplan;
        }

        @Override
        public String toString() {
            return JSONObject.toJSONString(this);
        }
    }

    public static class JianchaMaterialEntityListBean implements Serializable {
        /**
         * cid : 1
         * title : da
         * descript : dasdasd
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
