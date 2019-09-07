package com.juheshi.video.entity;

import java.sql.Date;
import java.sql.Timestamp;

public class AdvStoreImage {

    private Integer id;

    private Integer advStoreId;

    private String picUrl;

    private Integer sort;

    private Timestamp createdTime;

    private Date deletedAt;

    private Integer picType;

    public AdvStoreImage() {
    }

    public AdvStoreImage(Integer advStoreId, String picUrl, Integer sort, Timestamp createdTime, Date deletedAt, Integer picType) {
        this.advStoreId = advStoreId;
        this.picUrl = picUrl;
        this.sort = sort;
        this.createdTime = createdTime;
        this.deletedAt = deletedAt;
        this.picType = picType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdvStoreId() {
        return advStoreId;
    }

    public void setAdvStoreId(Integer advStoreId) {
        this.advStoreId = advStoreId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

    public Integer getPicType() {
        return picType;
    }

    public void setPicType(Integer picType) {
        this.picType = picType;
    }
}
