package com.juheshi.video.entity;

import java.sql.Timestamp;

public class PayOrder {

    public static final int OBJECT_TYPE_VIP = 1;//线上vip
    public static final int OBJECT_TYPE_STORE_VIP = 2;//实体店vip


    public static final int STATE_CANCEL = -1;
    public static final int STATE_PREPAY = 0;
    public static final int STATE_PAID = 1;
    public static final int STATE_FINISHED = 2;
    public static final int STATE_ERROR = 3;
    public static final int STATE_REFUND = 9;

    public static final int DIVIDE_STATE_NO = 0;
    public static final int DIVIDE_STATE_YES = 1;



    private Integer id;
    private String orderNo; //订单编号
    private String openId;//用户openid
    private Integer userId;//用户id
    private Double amount;//订单金额
    private Integer payType;//支付方式（1=微信支付，2=支付宝，3=...）
    private Timestamp payTime;//支付时间
    private Integer objectType; //订单对象类型（1=vip会员，2...）
    private Integer objectId; //订单对象id
    private String prepayId;//预支付id
    private String prepayRawData; //预付单原数据
    private String callbackRawData;//回调原数据
    private String IPAddress;//ip地址
    private Integer state; //-1.已取消，0.预生成，1.已支付，2.已完成，3.支付回调处理异常
    private Integer divideState;//分成状态 0：未分成，1：已分成
    private String stateRemark;
    private Timestamp createdTime;
    private String storeCopyNo;  //给店铺开通VIP，店铺用户的COPYNO
    private Integer industryId;  //给店铺开通VIP，店铺的行业标签

    private AppUser appUser;

    public PayOrder(){}

    public PayOrder(Integer id, String orderNo, String openId, Integer userId, Double amount, Integer payType, Timestamp payTime,
                    Integer objectType, Integer objectId, String prepayId, String prepayRawData, String callbackRawData,
                    String IPAddress, Integer state, Timestamp createdTime, String storeCopyNo, Integer industryId) {
        this.id = id;
        this.orderNo = orderNo;
        this.openId = openId;
        this.userId = userId;
        this.amount = amount;
        this.payType = payType;
        this.payTime = payTime;
        this.objectType = objectType;
        this.objectId = objectId;
        this.prepayId = prepayId;
        this.prepayRawData = prepayRawData;
        this.callbackRawData = callbackRawData;
        this.IPAddress = IPAddress;
        this.state = state;
        this.createdTime = createdTime;
        this.storeCopyNo = storeCopyNo;
        this.industryId = industryId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPrepayRawData() {
        return prepayRawData;
    }

    public void setPrepayRawData(String prepayRawData) {
        this.prepayRawData = prepayRawData;
    }

    public String getCallbackRawData() {
        return callbackRawData;
    }

    public void setCallbackRawData(String callbackRawData) {
        this.callbackRawData = callbackRawData;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
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

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }


    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Integer getDivideState() {
        return divideState;
    }

    public void setDivideState(Integer divideState) {
        this.divideState = divideState;
    }

    public String getStoreCopyNo() {
        return storeCopyNo;
    }

    public void setStoreCopyNo(String storeCopyNo) {
        this.storeCopyNo = storeCopyNo;
    }

    public Integer getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Integer industryId) {
        this.industryId = industryId;
    }
}
