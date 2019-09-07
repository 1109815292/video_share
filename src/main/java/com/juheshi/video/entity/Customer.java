package com.juheshi.video.entity;

import java.sql.Timestamp;

public class Customer {

    private Integer id;
    private Integer userId;
    private Integer videoId;
    private Timestamp viewTime;

    public Customer(){}

    public Customer(Integer userId, Integer videoId, Timestamp viewTime) {
        this.userId = userId;
        this.videoId = videoId;
        this.viewTime = viewTime;
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

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public Timestamp getViewTime() {
        return viewTime;
    }

    public void setViewTime(Timestamp viewTime) {
        this.viewTime = viewTime;
    }
}
