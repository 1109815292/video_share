package com.juheshi.video.entity;

public class VideoAdv {

    private Integer id;
    private Integer videoId;
    private Integer advId;
    private Integer advType;

    public VideoAdv() {
    }

    public VideoAdv(Integer videoId, Integer advId, Integer advType) {
        this.videoId = videoId;
        this.advId = advId;
        this.advType = advType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public Integer getAdvId() {
        return advId;
    }

    public void setAdvId(Integer advId) {
        this.advId = advId;
    }

    public Integer getAdvType() {
        return advType;
    }

    public void setAdvType(Integer advType) {
        this.advType = advType;
    }
}
