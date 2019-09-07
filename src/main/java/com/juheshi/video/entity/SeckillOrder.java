package com.juheshi.video.entity;

import com.juheshi.video.common.PageBean;

import java.io.Serializable;
import java.sql.Timestamp;

public class SeckillOrder extends PageBean implements Serializable {

    private int id;
    private int seckillId;
    private Double money;
    private Double centreMoney;
    private Double shopMoney;
    private String userId;
    private int storeId;
    private Timestamp createTime;
    private Timestamp payTime;
    private String status;
    private String receiverAddress;
    private String receiverMobile;
    private String receiver;
    private String transactionId;
    private String examine;
    private Timestamp examineTime;
    private int isCost;
    private int mainOrder;

    //以下字段仅供查询
    private Timestamp createTimeBegin;
    private Timestamp createTimeEnd;
    private Timestamp examineTimeBegin;
    private Timestamp examineTimeEnd;
    private Timestamp consumeTimeBegin;
    private Timestamp consumeTimeEnd;

    //以下仅供展示
    private String smallPic;
    private Timestamp consumeTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(int seckillId) {
        this.seckillId = seckillId;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getCentreMoney() {
        return centreMoney;
    }

    public void setCentreMoney(Double centreMoney) {
        this.centreMoney = centreMoney;
    }

    public Double getShopMoney() {
        return shopMoney;
    }

    public void setShopMoney(Double shopMoney) {
        this.shopMoney = shopMoney;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getExamine() {
        return examine;
    }

    public void setExamine(String examine) {
        this.examine = examine;
    }

    public Timestamp getExamineTime() {
        return examineTime;
    }

    public void setExamineTime(Timestamp examineTime) {
        this.examineTime = examineTime;
    }

    public int getIsCost() {
        return isCost;
    }

    public void setIsCost(int isCost) {
        this.isCost = isCost;
    }

    public int getMainOrder() {
        return mainOrder;
    }

    public void setMainOrder(int mainOrder) {
        this.mainOrder = mainOrder;
    }

    public String getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(String smallPic) {
        this.smallPic = smallPic;
    }

    public Timestamp getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(Timestamp consumeTime) {
        this.consumeTime = consumeTime;
    }

    public Timestamp getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Timestamp createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Timestamp getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Timestamp createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Timestamp getExamineTimeBegin() {
        return examineTimeBegin;
    }

    public void setExamineTimeBegin(Timestamp examineTimeBegin) {
        this.examineTimeBegin = examineTimeBegin;
    }

    public Timestamp getExamineTimeEnd() {
        return examineTimeEnd;
    }

    public void setExamineTimeEnd(Timestamp examineTimeEnd) {
        this.examineTimeEnd = examineTimeEnd;
    }

    public Timestamp getConsumeTimeBegin() {
        return consumeTimeBegin;
    }

    public void setConsumeTimeBegin(Timestamp consumeTimeBegin) {
        this.consumeTimeBegin = consumeTimeBegin;
    }

    public Timestamp getConsumeTimeEnd() {
        return consumeTimeEnd;
    }

    public void setConsumeTimeEnd(Timestamp consumeTimeEnd) {
        this.consumeTimeEnd = consumeTimeEnd;
    }
}
