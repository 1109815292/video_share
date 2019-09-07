package com.juheshi.video.entity;

public class Industry {

    private Integer id;
    private String industry;
    private String channelShow;
    private String labelShow;
    private String imgUrl;

    public Industry(){}

    public Industry(String industry, String channelShow, String labelShow, String imgUrl) {
        this.industry = industry;
        this.channelShow = channelShow;
        this.labelShow = labelShow;
        this.imgUrl = imgUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getChannelShow() {
        return channelShow;
    }

    public void setChannelShow(String channelShow) {
        this.channelShow = channelShow;
    }

    public String getLabelShow() {
        return labelShow;
    }

    public void setLabelShow(String labelShow) {
        this.labelShow = labelShow;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
