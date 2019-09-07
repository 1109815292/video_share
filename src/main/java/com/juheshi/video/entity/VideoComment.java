package com.juheshi.video.entity;

import java.sql.Timestamp;

public class VideoComment {

    private Integer id;
    private Integer videoId;
    private Integer userId;
    private String commentCont;
    private Timestamp createdTime;
    private Integer likeCount;

    private AppUser appUser;
    private String likeFlag;

    public VideoComment(){}

    public VideoComment(Integer videoId, Integer userId, String commentCont, Timestamp createdTime, Integer likeCount) {
        this.videoId = videoId;
        this.userId = userId;
        this.commentCont = commentCont;
        this.createdTime = createdTime;
        this.likeCount = likeCount;
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

    public String getCommentCont() {
        return commentCont;
    }

    public void setCommentCont(String commentCont) {
        this.commentCont = commentCont;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public String getLikeFlag() {
        return likeFlag;
    }

    public void setLikeFlag(String likeFlag) {
        this.likeFlag = likeFlag;
    }
}
