package com.henghao.hhworkpresent.entity;

/**
 * 用户实体类
 * Created by bryanrady on 2017/4/7.
 */

public class UserInfoEntity {


    /**
     * status : 1
     * msg : 登录成功
     * data : {"dn":"","id":"HZ9080955acfcfff015acfe883040409","ip":"","name":"兰天","login_NAME":"gyajj.lt","email":"","active":1,"order_NO":73,"user_STYLE":"default","delete_TIME":"","register_TIME":"2017-03-15 11:00:06","password":"D4F7DDF6D2B9A0E7","firstname":"兰","givenname":"天","emp_NUM":"","certificate":"password","sex":"1","birth_DATE":"","has_IMAGE":1,"telephone":"","cellphone":"","cert_TYPE":"","cert_NO":"","postcode":"","address":"","comments":"","sec_ID":"","engname":"","position":"副局长","fax":"","work_DESC":"","f1":"","f2":"","f3":"","f4":"","f5":"","f6":"","f7":"","f8":"","f9":"lan","f10":"L","telephone_HOME":"","other_POSITION":"","ldap_UNID":""}
     */

    private int status;
    private String msg;
    private UserInfo data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }

    public static class UserInfo {
        /**
         * dn :
         * id : HZ9080955acfcfff015acfe883040409
         * ip :
         * name : 兰天
         * login_NAME : gyajj.lt
         * email :
         * active : 1
         * order_NO : 73
         * user_STYLE : default
         * delete_TIME :
         * register_TIME : 2017-03-15 11:00:06
         * password : D4F7DDF6D2B9A0E7
         * firstname : 兰
         * givenname : 天
         * emp_NUM :
         * certificate : password
         * sex : 1
         * birth_DATE :
         * has_IMAGE : 1
         * telephone :
         * cellphone :
         * cert_TYPE :
         * cert_NO :
         * postcode :
         * address :
         * comments :
         * sec_ID :
         * engname :
         * position : 副局长
         * fax :
         * work_DESC :
         * f1 :
         * f2 :
         * f3 :
         * f4 :
         * f5 :
         * f6 :
         * f7 :
         * f8 :
         * f9 : lan
         * f10 : L
         * telephone_HOME :
         * other_POSITION :
         * ldap_UNID :
         */

        private String dn;
        private String id;
        private String ip;
        private String name;
        private String login_NAME;
        private String email;
        private int active;
        private int order_NO;
        private String user_STYLE;
        private String delete_TIME;
        private String register_TIME;
        private String password;
        private String firstname;
        private String givenname;
        private String emp_NUM;
        private String certificate;
        private String sex;
        private String birth_DATE;
        private int has_IMAGE;
        private String telephone;
        private String cellphone;
        private String cert_TYPE;
        private String cert_NO;
        private String postcode;
        private String address;
        private String comments;
        private String sec_ID;
        private String engname;
        private String position;
        private String fax;
        private String work_DESC;
        private String f1;
        private String f2;
        private String f3;
        private String f4;
        private String f5;
        private String f6;
        private String f7;
        private String f8;
        private String f9;
        private String f10;
        private String telephone_HOME;
        private String other_POSITION;
        private String ldap_UNID;

        public String getDn() {
            return dn;
        }

        public void setDn(String dn) {
            this.dn = dn;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogin_NAME() {
            return login_NAME;
        }

        public void setLogin_NAME(String login_NAME) {
            this.login_NAME = login_NAME;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getActive() {
            return active;
        }

        public void setActive(int active) {
            this.active = active;
        }

        public int getOrder_NO() {
            return order_NO;
        }

        public void setOrder_NO(int order_NO) {
            this.order_NO = order_NO;
        }

        public String getUser_STYLE() {
            return user_STYLE;
        }

        public void setUser_STYLE(String user_STYLE) {
            this.user_STYLE = user_STYLE;
        }

        public String getDelete_TIME() {
            return delete_TIME;
        }

        public void setDelete_TIME(String delete_TIME) {
            this.delete_TIME = delete_TIME;
        }

        public String getRegister_TIME() {
            return register_TIME;
        }

        public void setRegister_TIME(String register_TIME) {
            this.register_TIME = register_TIME;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getGivenname() {
            return givenname;
        }

        public void setGivenname(String givenname) {
            this.givenname = givenname;
        }

        public String getEmp_NUM() {
            return emp_NUM;
        }

        public void setEmp_NUM(String emp_NUM) {
            this.emp_NUM = emp_NUM;
        }

        public String getCertificate() {
            return certificate;
        }

        public void setCertificate(String certificate) {
            this.certificate = certificate;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirth_DATE() {
            return birth_DATE;
        }

        public void setBirth_DATE(String birth_DATE) {
            this.birth_DATE = birth_DATE;
        }

        public int getHas_IMAGE() {
            return has_IMAGE;
        }

        public void setHas_IMAGE(int has_IMAGE) {
            this.has_IMAGE = has_IMAGE;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getCellphone() {
            return cellphone;
        }

        public void setCellphone(String cellphone) {
            this.cellphone = cellphone;
        }

        public String getCert_TYPE() {
            return cert_TYPE;
        }

        public void setCert_TYPE(String cert_TYPE) {
            this.cert_TYPE = cert_TYPE;
        }

        public String getCert_NO() {
            return cert_NO;
        }

        public void setCert_NO(String cert_NO) {
            this.cert_NO = cert_NO;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public String getSec_ID() {
            return sec_ID;
        }

        public void setSec_ID(String sec_ID) {
            this.sec_ID = sec_ID;
        }

        public String getEngname() {
            return engname;
        }

        public void setEngname(String engname) {
            this.engname = engname;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getFax() {
            return fax;
        }

        public void setFax(String fax) {
            this.fax = fax;
        }

        public String getWork_DESC() {
            return work_DESC;
        }

        public void setWork_DESC(String work_DESC) {
            this.work_DESC = work_DESC;
        }

        public String getF1() {
            return f1;
        }

        public void setF1(String f1) {
            this.f1 = f1;
        }

        public String getF2() {
            return f2;
        }

        public void setF2(String f2) {
            this.f2 = f2;
        }

        public String getF3() {
            return f3;
        }

        public void setF3(String f3) {
            this.f3 = f3;
        }

        public String getF4() {
            return f4;
        }

        public void setF4(String f4) {
            this.f4 = f4;
        }

        public String getF5() {
            return f5;
        }

        public void setF5(String f5) {
            this.f5 = f5;
        }

        public String getF6() {
            return f6;
        }

        public void setF6(String f6) {
            this.f6 = f6;
        }

        public String getF7() {
            return f7;
        }

        public void setF7(String f7) {
            this.f7 = f7;
        }

        public String getF8() {
            return f8;
        }

        public void setF8(String f8) {
            this.f8 = f8;
        }

        public String getF9() {
            return f9;
        }

        public void setF9(String f9) {
            this.f9 = f9;
        }

        public String getF10() {
            return f10;
        }

        public void setF10(String f10) {
            this.f10 = f10;
        }

        public String getTelephone_HOME() {
            return telephone_HOME;
        }

        public void setTelephone_HOME(String telephone_HOME) {
            this.telephone_HOME = telephone_HOME;
        }

        public String getOther_POSITION() {
            return other_POSITION;
        }

        public void setOther_POSITION(String other_POSITION) {
            this.other_POSITION = other_POSITION;
        }

        public String getLdap_UNID() {
            return ldap_UNID;
        }

        public void setLdap_UNID(String ldap_UNID) {
            this.ldap_UNID = ldap_UNID;
        }
    }
}
