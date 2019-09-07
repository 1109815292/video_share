package com.juheshi.video.entity;

import java.sql.Timestamp;

public class Stationmaster {

    public static final int STATE_NORMAL = 1;//正常
    public static final int STATE_BLOCK = 0;//封禁

    private Integer id;

    private Integer userId;

    private String copyNo;

    private String stationName;

    private String publicWx;

    private String publicImg;

    private String publicQRCode;

    private Integer stationDistrictId;

    private Integer state;

    private Timestamp createdTime;

    private District district;

    private String districtStr;

    public Stationmaster() {
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

    public String getCopyNo() {
        return copyNo;
    }

    public void setCopyNo(String copyNo) {
        this.copyNo = copyNo;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getPublicWx() {
        return publicWx;
    }

    public void setPublicWx(String publicWx) {
        this.publicWx = publicWx;
    }

    public String getPublicImg() {
        return publicImg;
    }

    public void setPublicImg(String publicImg) {
        this.publicImg = publicImg;
    }

    public String getPublicQRCode() {
        return publicQRCode;
    }

    public void setPublicQRCode(String publicQRCode) {
        this.publicQRCode = publicQRCode;
    }

    public Integer getStationDistrictId() {
        return stationDistrictId;
    }

    public void setStationDistrictId(Integer stationDistrictId) {
        this.stationDistrictId = stationDistrictId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public String getDistrictStr() {
        return districtStr;
    }

    public void setDistrictStr(String districtStr) {
        this.districtStr = districtStr;
    }
}
