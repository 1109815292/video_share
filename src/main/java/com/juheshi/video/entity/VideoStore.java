package com.juheshi.video.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class VideoStore {

    private Integer id;
    private Integer userId;
    private Integer storeId;
    private String title;
    private String brief;
    private Integer industryId;
    private String otherIndustry;
    private String coverImg;
    private Integer videoType;
    private String videoUrl;
    private Timestamp createdTime;
    private Integer checkState;
    private Integer commentCount;
    private Integer forwardCount;
    private Integer viewCount;
    private String stationCopyNo;
    private String sharedWords;
    private String copyNo;
    private Integer within48hours;
    private Integer isTop;

    private Integer notified;

    private List<VideoStoreImage> images = new ArrayList<>(0);

    private AppUser appUser;
    private List<VideoComment> videoCommentList;

    //************ 非数据库持久化字段 ************
    private String coverImgMediaId;
    private String storeImgMediaIds;


    public VideoStore(){}

    public VideoStore(Integer userId, Integer storeId, String title, String brief, Integer industryId, String otherIndustry,
                      String coverImg, Integer videoType, String videoUrl, Timestamp createdTime, Integer checkState, Integer commentCount,
                      Integer forwardCount, Integer viewCount, String stationCopyNo, String sharedWords, Integer within48hours, Integer isTop) {
        this.userId = userId;
        this.storeId = storeId;
        this.title = title;
        this.brief = brief;
        this.industryId = industryId;
        this.otherIndustry = otherIndustry;
        this.coverImg = coverImg;
        this.videoType = videoType;
        this.videoUrl = videoUrl;
        this.createdTime = createdTime;
        this.checkState = checkState;
        this.commentCount = commentCount;
        this.forwardCount = forwardCount;
        this.viewCount = viewCount;
        this.stationCopyNo = stationCopyNo;
        this.sharedWords = sharedWords;
        this.within48hours = within48hours;
        this.isTop = isTop;
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

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public Integer getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Integer industryId) {
        this.industryId = industryId;
    }

    public String getOtherIndustry() {
        return otherIndustry;
    }

    public void setOtherIndustry(String otherIndustry) {
        this.otherIndustry = otherIndustry;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public Integer getVideoType() {
        return videoType;
    }

    public void setVideoType(Integer videoType) {
        this.videoType = videoType;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getForwardCount() {
        return forwardCount;
    }

    public void setForwardCount(Integer forwardCount) {
        this.forwardCount = forwardCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public String getStationCopyNo() {
        return stationCopyNo;
    }

    public void setStationCopyNo(String stationCopyNo) {
        this.stationCopyNo = stationCopyNo;
    }

    public List<VideoComment> getVideoCommentList() {
        return videoCommentList;
    }

    public void setVideoCommentList(List<VideoComment> videoCommentList) {
        this.videoCommentList = videoCommentList;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public String getSharedWords() {
        return sharedWords;
    }

    public void setSharedWords(String sharedWords) {
        this.sharedWords = sharedWords;
    }

    public String getCopyNo() {
        return copyNo;
    }

    public void setCopyNo(String copyNo) {
        this.copyNo = copyNo;
    }

    public List<VideoStoreImage> getImages() {
        return images;
    }

    public void setImages(List<VideoStoreImage> images) {
        this.images = images;
    }

    public Integer getWithin48hours() {
        return within48hours;
    }

    public void setWithin48hours(Integer within48hours) {
        this.within48hours = within48hours;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public String getCoverImgMediaId() {
        return coverImgMediaId;
    }

    public void setCoverImgMediaId(String coverImgMediaId) {
        this.coverImgMediaId = coverImgMediaId;
    }

    public String getStoreImgMediaIds() {
        return storeImgMediaIds;
    }

    public void setStoreImgMediaIds(String storeImgMediaIds) {
        this.storeImgMediaIds = storeImgMediaIds;
    }

    public Integer getNotified() {
        return notified;
    }

    public void setNotified(Integer notified) {
        this.notified = notified;
    }
}
