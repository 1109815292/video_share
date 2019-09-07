package com.juheshi.video.entity;

import java.sql.Timestamp;
import java.util.Date;

public class Video {

    public static final int CHECK_STATE_DRAFT = 0;
    public static final int CHECK_STATE_PASS = 1;
    public static final int CHECK_STATE_REJECT = -1;

    private Integer id;
    private Integer userId;
    private String copyNo;
    private String coverImg;
    private String sourceUrl;
    private String videoUrl;
    private String title;
    private String tag;
    private String publicFlag;
    private Integer sort;
    private Timestamp createdTime;
    private Integer viewCount;
    private Integer forwardCount;
    private Integer peopleCount;
    private String cachedFlag;
    private String sharedWords;
    private Integer type;//1=APP上传，2=APP替换，3=后台上传
    private Integer isTop;//是否置顶
    private Integer advType;
    private String advIds;
    private Date deletedAt;
    private Integer showBtnType;
    private Integer checkState;
    private String checkStateRemark;
    private Integer categoryId;//视频分类ID


    //非数据库字段，用于传递参数
    private AppUser appUser;

    private Category category;//分类

    public Video() {
    }

    public Video(Integer userId, String copyNo, String coverImg, String sourceUrl, String videoUrl, String title,
                 String tag, String publicFlag, Integer sort, Timestamp createdTime, Integer viewCount, Integer forwardCount,
                 Integer peopleCount, String cachedFlag, String sharedWords, Integer type, Integer isTop, Integer advType,
                 String advIds, Date deletedAt, Integer showBtnType, AppUser appUser, Integer checkState, String checkStateRemark) {
        this.userId = userId;
        this.copyNo = copyNo;
        this.coverImg = coverImg;
        this.sourceUrl = sourceUrl;
        this.videoUrl = videoUrl;
        this.title = title;
        this.tag = tag;
        this.publicFlag = publicFlag;
        this.sort = sort;
        this.createdTime = createdTime;
        this.viewCount = viewCount;
        this.forwardCount = forwardCount;
        this.peopleCount = peopleCount;
        this.cachedFlag = cachedFlag;
        this.sharedWords = sharedWords;
        this.type = type;
        this.isTop = isTop;
        this.advType = advType;
        this.advIds = advIds;
        this.deletedAt = deletedAt;
        this.showBtnType = showBtnType;
        this.appUser = appUser;
        this.checkState = checkState;
        this.checkStateRemark = checkStateRemark;
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

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPublicFlag() {
        return publicFlag;
    }

    public void setPublicFlag(String publicFlag) {
        this.publicFlag = publicFlag;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getForwardCount() {
        return forwardCount;
    }

    public void setForwardCount(Integer forwardCount) {
        this.forwardCount = forwardCount;
    }

    public Integer getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
    }

    public String getCachedFlag() {
        return cachedFlag;
    }

    public void setCachedFlag(String cachedFlag) {
        this.cachedFlag = cachedFlag;
    }

    public String getSharedWords() {
        return sharedWords;
    }

    public void setSharedWords(String sharedWords) {
        this.sharedWords = sharedWords;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCopyNo() {
        return copyNo;
    }

    public void setCopyNo(String copyNo) {
        this.copyNo = copyNo;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Integer getAdvType() {
        return advType;
    }

    public void setAdvType(Integer advType) {
        this.advType = advType;
    }

    public String getAdvIds() {
        return advIds;
    }

    public void setAdvIds(String advIds) {
        this.advIds = advIds;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Integer getShowBtnType() {
        return showBtnType;
    }

    public void setShowBtnType(Integer showBtnType) {
        this.showBtnType = showBtnType;
    }

    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    public String getCheckStateRemark() {
        return checkStateRemark;
    }

    public void setCheckStateRemark(String checkStateRemark) {
        this.checkStateRemark = checkStateRemark;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
