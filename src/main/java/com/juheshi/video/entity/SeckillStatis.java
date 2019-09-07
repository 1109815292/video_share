package com.juheshi.video.entity;

import com.juheshi.video.common.PageBean;

import java.io.Serializable;

public class SeckillStatis extends PageBean implements Serializable {

    private int id;
    private int seckillId;
    private int salaNum;
    private int examNum;
    private Double totelPrice;

    public SeckillStatis() {
    }

    public SeckillStatis(int seckillId) {
        this.seckillId = seckillId;
        this.salaNum = 0;
        this.examNum = 0;
        this.totelPrice = 0D;
    }

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

    public int getSalaNum() {
        return salaNum;
    }

    public void setSalaNum(int salaNum) {
        this.salaNum = salaNum;
    }

    public int getExamNum() {
        return examNum;
    }

    public void setExamNum(int examNum) {
        this.examNum = examNum;
    }

    public Double getTotelPrice() {
        return totelPrice;
    }

    public void setTotelPrice(Double totelPrice) {
        this.totelPrice = totelPrice;
    }
}
