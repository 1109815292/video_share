package com.juheshi.video.entity;

public class AdvProduct {

    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_JD = 2;
    public static final int TYPE_WEI_DIAN = 3;
    public static final int TYPE_YOU_ZAN = 4;
    public static final int TYPE_TAO_BAO = 5;


    private Integer id;
    private Integer userId;
    private String productName;
    private Double price;
    private String productUrl;
    private String picUrl;
    private String watchword;
    private Integer type;
    private Integer sort;
    private Integer viewCount;
    private Integer peopleCount;//观看人数
    private Double couponPrice;

    public AdvProduct(){}

    public AdvProduct(Integer userId, String productName, Double price, String productUrl, String picUrl, String watchword,
                      Integer type, Integer sort, Integer viewCount, Integer peopleCount, Double couponPrice) {
        this.userId = userId;
        this.productName = productName;
        this.price = price;
        this.productUrl = productUrl;
        this.picUrl = picUrl;
        this.watchword = watchword;
        this.type = type;
        this.sort = sort;
        this.viewCount = viewCount;
        this.peopleCount = peopleCount;
        this.couponPrice = couponPrice;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getWatchword() {
        return watchword;
    }

    public void setWatchword(String watchword) {
        this.watchword = watchword;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

    public Double getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(Double couponPrice) {
        this.couponPrice = couponPrice;
    }
}
