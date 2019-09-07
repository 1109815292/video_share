package com.juheshi.video.entity;

public class  AdvOther {

    public static final int TYPE_PUBLIC = 1;
    public static final int TYPE_APPLET = 2;
    public static final int TYPE_PERSON = 3;
    public static final int TYPE_GROUP = 4;

    public static final int TYPE_APP = 5;
    public static final int TYPE_WEBSITE = 6;
    public static final int TYPE_LINK = 7;
    public static final int TYPE_QR_CODE = 8;


    private Integer id;
    private Integer userId;
    private String name;
    private String url;
    private String desc;
    private String picUrl;
    private String qrcode;
    private Integer type; //5=APP, 6=官网,7=链接,8=二维码
    private Integer viewCount;//观看次数
    private Integer peopleCount;//观看人数

    public AdvOther() {
    }

    public AdvOther(Integer userId, String name, String url, String desc, String picUrl, String qrcode, Integer type, Integer viewCount, Integer peopleCount) {
        this.userId = userId;
        this.name = name;
        this.url = url;
        this.desc = desc;
        this.picUrl = picUrl;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
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
