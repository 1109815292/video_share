package com.juheshi.video.entity;

import java.sql.Timestamp;

public class VideoStoreImage {

    private Integer id;
    private Integer videoId;
    private String picUrl;
    private Integer sort;
    private Timestamp createdTime;

    public VideoStoreImage(){}

    public VideoStoreImage(Integer videoId, String picUrl, Integer sort, Timestamp createdTime) {
        this.videoId = videoId;
        this.picUrl = picUrl;
        this.sort = sort;
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
}
