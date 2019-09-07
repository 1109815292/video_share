package com.juheshi.video.entity;

public class Category {

    public static final int GROUP_FANS_VIDEO = 1; //吸粉视频

    private Integer id;

    private String name;

    private Integer group;

    private String img;

    private String enabledFlag;

    private Integer sort;

    private Integer videoCount;

    public Category() {
    }

    public Category(String name, Integer group, String img, String enabledFlag, Integer sort, Integer videoCount) {
        this.name = name;
        this.group = group;
        this.img = img;
        this.enabledFlag = enabledFlag;
        this.sort = sort;
        this.videoCount = videoCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(Integer videoCount) {
        this.videoCount = videoCount;
    }
}
