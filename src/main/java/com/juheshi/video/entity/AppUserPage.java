package com.juheshi.video.entity;

public class AppUserPage {

    private Integer id;

    private Integer userId;

    private String pageName; //主页名称
    private String companyName; //公司名称

    private String name;//姓名
    private String mobile;//手机号
    private String wx;//微信号
    private String sign;//签名

    private String wxQRCode;//个人微信二维码
    private String pageBackgroundColor;//主页背景色

    private Integer viewCount;
    private Integer peopleCount;

    //******* 非持久化属性
    private String headImg;//头像
    private String userName;//昵称
    private String copyNo;//视推号
    private String vipFlag;//是否会员

    public AppUserPage() {
    }

    public AppUserPage(Integer userId) {
        this.userId = userId;
        this.viewCount = 0;
        this.peopleCount = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getWxQRCode() {
        return wxQRCode;
    }

    public void setWxQRCode(String wxQRCode) {
        this.wxQRCode = wxQRCode;
    }

    public String getPageBackgroundColor() {
        return pageBackgroundColor;
    }

    public void setPageBackgroundColor(String pageBackgroundColor) {
        this.pageBackgroundColor = pageBackgroundColor;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCopyNo() {
        return copyNo;
    }

    public void setCopyNo(String copyNo) {
        this.copyNo = copyNo;
    }

    public String getVipFlag() {
        return vipFlag;
    }

    public void setVipFlag(String vipFlag) {
        this.vipFlag = vipFlag;
    }
}
