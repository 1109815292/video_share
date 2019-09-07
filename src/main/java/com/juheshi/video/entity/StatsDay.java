package com.juheshi.video.entity;

import java.sql.Timestamp;
import java.util.Calendar;

public class StatsDay {
    public static final int TYPE_NEW_USER = 1;
    public static final int TYPE_DAILY_FLOW = 2;
    public static final int TYPE_CASH_AMOUNT = 3;
    public static final int TYPE_ACTIVE_USER = 4;


    private Integer statsYear;
    private Integer statsMonth;
    private Integer statsDay;
    private Integer newUserCount;//新用户数
    private Double dailyFlow;//日流水
    private Double cashAmount;//提现金额
    private Integer activeUserCount;//活跃用户数

    private Timestamp createdTime;

    public StatsDay() {
    }

    public StatsDay(int type, Object value) {
        Calendar cal = Calendar.getInstance();
        this.statsYear = cal.get(Calendar.YEAR);
        this.statsMonth = cal.get(Calendar.MONTH) + 1;
        this.statsDay = cal.get(Calendar.DAY_OF_MONTH);
        switch (type) {
            case TYPE_NEW_USER:
                this.newUserCount = 1;
                this.dailyFlow = 0.0;
                this.cashAmount=0.0;
                break;
            case TYPE_DAILY_FLOW:
                this.dailyFlow = (Double) value;
                this.cashAmount=0.0;
                this.newUserCount = 0;
                break;
            case TYPE_CASH_AMOUNT:
                this.cashAmount = (Double) value;
                this.dailyFlow = 0.0;
                this.cashAmount=0.0;
                this.newUserCount = 0;
                break;
            case TYPE_ACTIVE_USER:
                this.activeUserCount = 1;
                this.dailyFlow = 0.0;
                this.cashAmount=0.0;
                this.newUserCount = 0;
                break;
        }
    }


    public StatsDay(Integer statsYear, Integer statsMonth, Integer statsDay) {
        this.statsYear = statsYear;
        this.statsMonth = statsMonth;
        this.statsDay = statsDay;
        this.newUserCount = 0;
        this.dailyFlow = 0.0;
        this.activeUserCount = 0;
        this.cashAmount =0.0;
    }

    public StatsDay(Integer statsYear, Integer statsMonth, Integer statsDay, Integer newUserCount, Double dailyFlow, Integer activeUserCount, Double cashAmount, Timestamp createdTime) {
        this.statsYear = statsYear;
        this.statsMonth = statsMonth;
        this.statsDay = statsDay;
        this.newUserCount = newUserCount;
        this.dailyFlow = dailyFlow;
        this.activeUserCount = activeUserCount;
        this.cashAmount = cashAmount;
        this.createdTime = createdTime;
    }

    public Integer getStatsYear() {
        return statsYear;
    }

    public void setStatsYear(Integer statsYear) {
        this.statsYear = statsYear;
    }

    public Integer getStatsMonth() {
        return statsMonth;
    }

    public void setStatsMonth(Integer statsMonth) {
        this.statsMonth = statsMonth;
    }

    public Integer getStatsDay() {
        return statsDay;
    }

    public void setStatsDay(Integer statsDay) {
        this.statsDay = statsDay;
    }

    public Integer getNewUserCount() {
        return newUserCount;
    }

    public void setNewUserCount(Integer newUserCount) {
        this.newUserCount = newUserCount;
    }

    public Double getDailyFlow() {
        return dailyFlow;
    }

    public void setDailyFlow(Double dailyFlow) {
        this.dailyFlow = dailyFlow;
    }


    public Integer getActiveUserCount() {
        return activeUserCount;
    }

    public void setActiveUserCount(Integer activeUserCount) {
        this.activeUserCount = activeUserCount;
    }

    public Double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }
}
