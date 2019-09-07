package com.juheshi.video.entity;

public class DivideRate {

    private int id;
    private Integer divideLevel;
    private Double divideRate;

    public DivideRate(){}

    public DivideRate(Integer divideLevel, Double divideRate) {
        this.divideLevel = divideLevel;
        this.divideRate = divideRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getDivideLevel() {
        return divideLevel;
    }

    public void setDivideLevel(Integer divideLevel) {
        this.divideLevel = divideLevel;
    }

    public Double getDivideRate() {
        return divideRate;
    }

    public void setDivideRate(Double divideRate) {
        this.divideRate = divideRate;
    }
}
