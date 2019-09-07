package com.juheshi.video.dto;

import com.juheshi.video.entity.Video;

import java.util.List;

public class VideoBatchDTO {

    private Integer userId;
    private List<Video> videos;

    public VideoBatchDTO() {
    }

    public VideoBatchDTO(Integer userId, List<Video> videos) {
        this.userId = userId;
        this.videos = videos;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
