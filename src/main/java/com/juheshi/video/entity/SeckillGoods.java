package com.juheshi.video.entity;

import com.juheshi.video.common.PageBean;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import java.io.Serializable;
import java.sql.Timestamp;

public class SeckillGoods extends PageBean implements Serializable {

    private int id;
    private String name;
    @Max ( value = 2000 )
    private String introduction;
    @Max ( value = 150 )
    private String smallPic;
    @Digits(integer = 10, fraction = 2)
    private Double price;
    @Digits(integer = 10, fraction = 2)
    private Double costPrice;
    @Max(value = 100)
    private int separate;
    private int storeId;
    private String storeName;
    private Timestamp createTime;
    @Future
    private Timestamp checkTime;
    private String status;
    private Timestamp startTime;
    @Future
    private Timestamp endTime;
    private Timestamp consumeTime;
    private int num;
    private int quota;
    private String phone;
    private int groups;
    private String createUser;
    private SeckillGroup seckillGroup;
    private SeckillStatis seckillStatis;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(String smallPic) {
        this.smallPic = smallPic;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public int getSeparate() {
        return separate;
    }

    public void setSeparate(int separate) {
        this.separate = separate;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Timestamp checkTime) {
        this.checkTime = checkTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Timestamp getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(Timestamp consumeTime) {
        this.consumeTime = consumeTime;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getGroups() {
        return groups;
    }

    public void setGroups(int group) {
        this.groups = group;
    }

    public SeckillGroup getSeckillGroup() {
        return seckillGroup;
    }

    public void setSeckillGroup(SeckillGroup seckillGroup) {
        this.seckillGroup = seckillGroup;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public SeckillStatis getSeckillStatis() {
        return seckillStatis;
    }

    public void setSeckillStatis(SeckillStatis seckillStatis) {
        this.seckillStatis = seckillStatis;
    }
}
