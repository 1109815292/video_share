package com.juheshi.video.entity;

import java.sql.Timestamp;

public class RechargeDivide {

    private int id;
    private Integer userId;
    private Double rechargeAmount;
    private Integer divideUserId;
    private Double divideAmount;
    private Timestamp createdTime;
    private Integer fansId;
    private Integer payFlag;

    private AppUser appUser;

    private AppUser fansUser;

    private AppUser divideUser;


    public RechargeDivide(){}

    public RechargeDivide(Integer userId, Double rechargeAmount, Integer divideUserId, Double divideAmount, Timestamp createdTime,
                          Integer fansId, Integer payFlag) {
        this.userId = userId;
        this.rechargeAmount = rechargeAmount;
        this.divideUserId = divideUserId;
        this.divideAmount = divideAmount;
        this.createdTime = createdTime;
        this.fansId = fansId;
        this.payFlag = payFlag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(Double rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public Integer getDivideUserId() {
        return divideUserId;
    }

    public void setDivideUserId(Integer divideUserId) {
        this.divideUserId = divideUserId;
    }

    public Double getDivideAmount() {
        return divideAmount;
    }

    public void setDivideAmount(Double divideAmount) {
        this.divideAmount = divideAmount;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getFansId() {
        return fansId;
    }

    public void setFansId(Integer fansId) {
        this.fansId = fansId;
    }

    public Integer getPayFlag() {
        return payFlag;
    }

    public void setPayFlag(Integer payFlag) {
        this.payFlag = payFlag;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public AppUser getFansUser() {
        return fansUser;
    }

    public void setFansUser(AppUser fansUser) {
        this.fansUser = fansUser;
    }

    public AppUser getDivideUser() {
        return divideUser;
    }

    public void setDivideUser(AppUser divideUser) {
        this.divideUser = divideUser;
    }
}
