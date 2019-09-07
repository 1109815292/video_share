package com.juheshi.video.entity;

import java.sql.Timestamp;

public class AppUserVip {

    private Integer id;

    private Integer userId;

    private Integer vipTypeId;

    private Timestamp vipEffectiveTime;

    private Timestamp vipDeadline;

    private Timestamp createdTime;

    public AppUserVip() {

    }

    public AppUserVip(Integer userId, Integer vipTypeId, Timestamp vipEffectiveTime, Timestamp vipDeadline, Timestamp createdTime) {
        this.userId = userId;
        this.vipTypeId = vipTypeId;
        this.vipEffectiveTime = vipEffectiveTime;
        this.vipDeadline = vipDeadline;
        this.createdTime = createdTime;
    }

    public AppUserVip(Integer id, Integer userId, Integer vipTypeId, Timestamp vipEffectiveTime, Timestamp vipDeadline, Timestamp createdTime) {
        this.id = id;
        this.userId = userId;
        this.vipTypeId = vipTypeId;
        this.vipEffectiveTime = vipEffectiveTime;
        this.vipDeadline = vipDeadline;
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

    public Timestamp getVipEffectiveTime() {
        return vipEffectiveTime;
    }

    public void setVipEffectiveTime(Timestamp vipEffectiveTime) {
        this.vipEffectiveTime = vipEffectiveTime;
    }

    public Timestamp getVipDeadline() {
        return vipDeadline;
    }

    public void setVipDeadline(Timestamp vipDeadline) {
        this.vipDeadline = vipDeadline;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }
}
