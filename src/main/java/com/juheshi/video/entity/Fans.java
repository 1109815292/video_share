package com.juheshi.video.entity;

import java.sql.Timestamp;

public class Fans {

    private Integer id;
    private Integer userId;
    private Integer fansId;
    private Timestamp createdTime;
    private Double payAmount;
    private Double profitAmount;

    public Fans(){}

    public Fans(Integer userId, Integer fansId, Timestamp createdTime, Double payAmount, Double profitAmount) {
        this.userId = userId;
        this.fansId = fansId;
        this.createdTime = createdTime;
        this.payAmount = payAmount;
        this.profitAmount = profitAmount;
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

    public Integer getFansId() {
        return fansId;
    }

    public void setFansId(Integer fansId) {
        this.fansId = fansId;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public Double getProfitAmount() {
        return profitAmount;
    }

    public void setProfitAmount(Double profitAmount) {
        this.profitAmount = profitAmount;
    }
}
