package com.juheshi.video.entity;

import java.sql.Timestamp;

public class AppUserClient {

    private Integer id;
    private Integer userId;
    private Integer clientUserId;
    private Integer viewCount;
    private Timestamp lastViewTime;
    private Integer videoViewCount;
    private Integer productViewCount;
    private Integer advViewCount;
    private Integer homePageViewCount;

    private AppUser clientUser;

    public AppUserClient() {
    }
    public AppUserClient(Integer userId, Integer clientUserId) {
        this.userId = userId;
        this.clientUserId = clientUserId;
    }
    public AppUserClient(Integer userId, Integer clientUserId, Integer viewCount, Timestamp lastViewTime, Integer videoViewCount,
                         Integer productViewCount, Integer advViewCount, Integer homePageViewCount) {
        this.userId = userId;
        this.clientUserId = clientUserId;
        this.viewCount = viewCount;
        this.lastViewTime = lastViewTime;
        this.videoViewCount = videoViewCount;
        this.productViewCount = productViewCount;
        this.advViewCount = advViewCount;
        this.homePageViewCount = homePageViewCount;
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

    public Integer getClientUserId() {
        return clientUserId;
    }

    public void setClientUserId(Integer clientUserId) {
        this.clientUserId = clientUserId;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Timestamp getLastViewTime() {
        return lastViewTime;
    }

    public void setLastViewTime(Timestamp lastViewTime) {
        this.lastViewTime = lastViewTime;
    }

    public AppUser getClientUser() {
        return clientUser;
    }

    public void setClientUser(AppUser clientUser) {
        this.clientUser = clientUser;
    }

    public Integer getVideoViewCount() {
        return videoViewCount;
    }

    public void setVideoViewCount(Integer videoViewCount) {
        this.videoViewCount = videoViewCount;
    }

    public Integer getProductViewCount() {
        return productViewCount;
    }

    public void setProductViewCount(Integer productViewCount) {
        this.productViewCount = productViewCount;
    }

    public Integer getAdvViewCount() {
        return advViewCount;
    }

    public void setAdvViewCount(Integer advViewCount) {
        this.advViewCount = advViewCount;
    }

    public Integer getHomePageViewCount() {
        return homePageViewCount;
    }

    public void setHomePageViewCount(Integer homePageViewCount) {
        this.homePageViewCount = homePageViewCount;
    }
}
