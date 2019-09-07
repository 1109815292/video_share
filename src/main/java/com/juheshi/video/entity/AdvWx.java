package com.juheshi.video.entity;

public class AdvWx {

    public static final int TYPE_PUBLIC = 1;
    public static final int TYPE_APPLET = 2;
    public static final int TYPE_PERSON = 3;
    public static final int TYPE_GROUP = 4;

    private Integer id;
    private Integer userId;
    private String headImg;
    private String advName;
    private String advDesc;
    private String qrcode;
    private Integer type;
    private Integer viewCount;
    private Integer peopleCount;//观看人数

    public AdvWx(){}

    public AdvWx(Integer userId, String headImg, String advName, String advDesc, String qrcode, Integer type, Integer viewCount, Integer peopleCount) {
        this.userId = userId;
        this.headImg = headImg;
        this.advName = advName;
        this.advDesc = advDesc;
        this.qrcode = qrcode;
        this.type = type;
        this.viewCount = viewCount;
        this.peopleCount = peopleCount;
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

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getAdvName() {
        return advName;
    }

    public void setAdvName(String advName) {
        this.advName = advName;
    }

    public String getAdvDesc() {
        return advDesc;
    }

    public void setAdvDesc(String advDesc) {
        this.advDesc = advDesc;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
}
