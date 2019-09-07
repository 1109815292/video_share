package com.juheshi.video.entity;

import java.sql.Timestamp;

public class LogViewAdv {

    private Integer id;
    private Integer advType;
    private Integer advId;
    private Integer userId;
    private Timestamp createdTime;

    public LogViewAdv() {
    }

    public LogViewAdv(Integer advType, Integer advId, Integer userId, Timestamp createdTime) {
        this.advType = advType;
        this.advId = advId;
        this.userId = userId;
        this.createdTime = createdTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdvType() {
        return advType;
    }

    public void setAdvType(Integer advType) {
        this.advType = advType;
    }

    
    public Integer getAdvId() {
        return advId;
    }

    public void setAdvId(Integer advId) {
        this.advId = advId;
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }
}
