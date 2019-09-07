package com.juheshi.video.entity;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AdvStore {

    private Integer id;
    private Integer userId;
    private String storeName;
    private String address;
    private String addressName;
    private Double longitude;
    private Double latitude;
    private String tel;
    private String storeUrl;
    private String picUrl;
    private String storeDesc;
    private Integer viewCount;
    private Integer peopleCount;//观看人数
    private Integer industryId;  //店铺行业标签
    private String stationCopyNo;  //站长的copyNo
    private Timestamp createdTime;
    private String wxQRCode;
    private String wx;

    private Industry industry;
    private List<AdvStoreImage> images = new ArrayList<>(0);

    private String deletedStoreImageIds;//记录删除的image的Id

    public AdvStore(){}

    public AdvStore(Integer userId, String storeName, String address, String addressName, Double longitude, Double latitude, String tel, String storeUrl,
                    String picUrl, String storeDesc, Integer viewCount, Integer peopleCount, Integer industryId, String stationCopyNo,
                    Timestamp createdTime, String wxQRCode, String wx) {
        this.userId = userId;
        this.storeName = storeName;
        this.address = address;
        this.addressName = addressName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.tel = tel;
        this.storeUrl = storeUrl;
        this.picUrl = picUrl;
        this.storeDesc = storeDesc;
        this.viewCount = viewCount;
        this.peopleCount = peopleCount;
        this.industryId = industryId;
        this.stationCopyNo = stationCopyNo;
        this.createdTime = createdTime;
        this.wxQRCode = wxQRCode;
        this.wx = wx;
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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getStoreDesc() {
        return storeDesc;
    }

    public void setStoreDesc(String storeDesc) {
        this.storeDesc = storeDesc;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
    }

    public List<AdvStoreImage> getImages() {
        return images;
    }

    public void setImages(List<AdvStoreImage> images) {
        this.images = images;
    }

    public String getDeletedStoreImageIds() {
        return deletedStoreImageIds;
    }

    public void setDeletedStoreImageIds(String deletedStoreImageIds) {
        this.deletedStoreImageIds = deletedStoreImageIds;
    }

    public Integer getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Integer industryId) {
        this.industryId = industryId;
    }

    public String getStationCopyNo() {
        return stationCopyNo;
    }

    public void setStationCopyNo(String stationCopyNo) {
        this.stationCopyNo = stationCopyNo;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public String getWxQRCode() {
        return wxQRCode;
    }

    public void setWxQRCode(String wxQRCode) {
        this.wxQRCode = wxQRCode;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public Industry getIndustry() {
        return industry;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }
}
