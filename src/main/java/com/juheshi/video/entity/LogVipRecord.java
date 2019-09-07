package com.juheshi.video.entity;

import java.sql.Timestamp;

public class LogVipRecord {

    private Integer id;
    private Integer userId;
    private Integer vipTypeId;
    private Integer type;
    private Integer price;
    private String remark;
    private Integer sysUserId;
    private Timestamp createdTime;

    public LogVipRecord() {
    }

    public LogVipRecord(Integer userId, Integer vipTypeId, Integer type, Integer price, String remark) {
        this.userId = userId;
        this.vipTypeId = vipTypeId;
        this.type = type;
        this.price = price;
        this.remark = remark;
    }

    public LogVipRecord(Integer userId, Integer vipTypeId, Integer type, Integer price, String remark, Integer sysUserId) {
        this.userId = userId;
        this.vipTypeId = vipTypeId;
        this.type = type;
        this.price = price;
        this.remark = remark;
        this.sysUserId = sysUserId;
    }

    public LogVipRecord(Integer id, Integer userId, Integer vipTypeId, Integer type, Integer price, String remark, Integer sysUserId, Timestamp createdTime) {
        this.id = id;
        this.userId = userId;
        this.vipTypeId = vipTypeId;
        this.type = type;
        this.price = price;
        this.remark = remark;
        this.sysUserId = sysUserId;
        this.createdTime = createdTime;
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

    public Integer getVipTypeId() {
        return vipTypeId;
    }

    public void setVipTypeId(Integer vipTypeId) {
        this.vipTypeId = vipTypeId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }
}
