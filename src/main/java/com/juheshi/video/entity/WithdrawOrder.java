package com.juheshi.video.entity;

import java.sql.Timestamp;

public class WithdrawOrder {

    private int id;
    private String orderNo;
    private String openId;
    private Integer userId;
    private Double amount;
    private Integer rechargeId;
    private Timestamp createdTime;
    private Integer state;
    private String stateRemark;

    public WithdrawOrder(){}

    public WithdrawOrder(String orderNo, String openId, Integer userId, Double amount, Integer rechargeId, Timestamp createdTime, Integer state, String stateRemark) {
        this.orderNo = orderNo;
        this.openId = openId;
        this.userId = userId;
        this.amount = amount;
        this.rechargeId = rechargeId;
        this.createdTime = createdTime;
        this.state = state;
        this.stateRemark = stateRemark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(Integer rechargeId) {
        this.rechargeId = rechargeId;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getStateRemark() {
        return stateRemark;
    }

    public void setStateRemark(String stateRemark) {
        this.stateRemark = stateRemark;
    }
}
