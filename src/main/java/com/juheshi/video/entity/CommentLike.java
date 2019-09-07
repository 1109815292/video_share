package com.juheshi.video.entity;

import java.sql.Timestamp;

public class CommentLike {

    private Integer id;
    private Integer commentId;
    private Integer userId;
    private Timestamp createdTime;

    public CommentLike(){}

    public CommentLike(Integer commentId, Integer userId, Timestamp createdTime) {
        this.commentId = commentId;
        this.userId = userId;
        this.createdTime = createdTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
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
