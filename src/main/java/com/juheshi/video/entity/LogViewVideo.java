package com.juheshi.video.entity;

import java.sql.Timestamp;

public class LogViewVideo {

    private Integer id;

    private Integer videoId;

    private Integer userId;

    private Timestamp createdTime;

    private Video video;

    public LogViewVideo() {
    }

    public LogViewVideo(Integer videoId, Integer userId, Timestamp createdTime) {
        this.videoId = videoId;
        this.userId = userId;
        this.createdTime = createdTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
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

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}
