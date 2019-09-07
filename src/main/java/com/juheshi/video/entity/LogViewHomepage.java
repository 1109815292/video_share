package com.juheshi.video.entity;

import java.sql.Timestamp;

public class LogViewHomepage {

    private Integer id;

    private Integer userPageId;

    private Integer userId;

    private Timestamp createdTime;

    public LogViewHomepage() {
    }

    public LogViewHomepage(Integer userPageId, Integer userId) {
        this.userPageId = userPageId;
        this.userId = userId;
    }

    public LogViewHomepage(Integer userPageId, Integer userId, Timestamp createdTime) {
        this.userPageId = userPageId;
        this.userId = userId;
        this.createdTime = createdTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserPageId() {
        return userPageId;
    }

    public void setUserPageId(Integer userPageId) {
        this.userPageId = userPageId;
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
