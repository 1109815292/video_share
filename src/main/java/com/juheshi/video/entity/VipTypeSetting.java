package com.juheshi.video.entity;

import java.sql.Timestamp;
import java.util.Date;

public class VipTypeSetting {

    public static final int VIP_TYPE_ONLINE = 1;
    public static final int VIP_TYPE_OFFLINE = 2;

    private Integer id;
    private String title;
    private String tag;
    private String description;
    private Integer originalPrice;
    private Integer presentPrice;
    private Integer days;
    private Integer giftDays;
    private String tips;
    private Integer visibleType;
    private Integer sort;
    private String enabledFlag;
    private Timestamp createdTime;
    private Date deletedAt;
    private String defaultType;
    private Integer type;

    public VipTypeSetting() {
    }

    public VipTypeSetting(Integer id, String title, String tag, String description, Integer originalPrice, Integer presentPrice,
                          Integer days, Integer giftDays, String tips, Integer visibleType, Integer sort, String enabledFlag,
                          Timestamp createdTime, Date deletedAt, String defaultType, Integer type) {
        this.id = id;
        this.title = title;
        this.tag = tag;
        this.description = description;
        this.originalPrice = originalPrice;
        this.presentPrice = presentPrice;
        this.days = days;
        this.giftDays = giftDays;
        this.tips = tips;
        this.visibleType = visibleType;
        this.sort = sort;
        this.enabledFlag = enabledFlag;
        this.createdTime = createdTime;
        this.deletedAt = deletedAt;
        this.defaultType = defaultType;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Integer originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Integer getPresentPrice() {
        return presentPrice;
    }

    public void setPresentPrice(Integer presentPrice) {
        this.presentPrice = presentPrice;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getGiftDays() {
        return giftDays;
    }

    public void setGiftDays(Integer giftDays) {
        this.giftDays = giftDays;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public Integer getVisibleType() {
        return visibleType;
    }

    public void setVisibleType(Integer visibleType) {
        this.visibleType = visibleType;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDefaultType() {
        return defaultType;
    }

    public void setDefaultType(String defaultType) {
        this.defaultType = defaultType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
