package com.juheshi.video.entity;

import com.juheshi.video.common.PageBean;

import java.io.Serializable;

public class SeckillGroup extends PageBean implements Serializable {

    private String id;
    private int seckillId;
    private Double costPrice;
    private int num;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(int seckillId) {
        this.seckillId = seckillId;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
