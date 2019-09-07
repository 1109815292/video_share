package com.juheshi.video.entity;

import java.sql.Timestamp;

public class CfgWxMessage {

    public static final int TYPE_CUSTOMER_SERVICE_MESSAGE = 1;//客服消息
    public static final int TYPE_TEMPLATE_MESSAGE = 2;//模板消息

    public static final int TRIGGER_POSITION_SUBSCRIBE = 11;//触发位置-关注公众号
    public static final int TRIGGER_POSITION_SCAN = 12;//触发位置-扫二维码

    public static final int TRIGGER_POSITION_NEW_REGISTER = 21;//成员注册

    public static final int TRIGGER_POSITION_SAVE_VIDEO = 31;//吸粉视频保存

    public static final int TRIGGER_POSITION_SAVE_VIDEO_STORE = 41;//商品视频保存

    public static final int TRIGGER_POSITION_VIDEO_STORE_NOTIFY_ERROR = 51;//站长审核视频通知发送失败时触发

    public static final int TRIGGER_POSITION_ACTIVE = 61;//收到用户发送的【激活】后触发


    public static final int MSG_TYPE_TEXT = 1;//客服消息-文本消息
    public static final int MSG_TYPE_IMAGE = 2;//客服消息-图片消息
    public static final int MSG_TYPE_NEWS = 3;//客服消息-图文消息


    private Integer id;
    private String title;
    private Integer type;
    private String triggerPosition;
    private String content;
    private String templateId;
    private String templateData;
    private String templateUrl;
    private Integer msgType;
    private String mediaId;
    private String newsDesc;
    private String newsUrl;
    private String newsPicUrl;
    private Integer sort;
    private Timestamp createdTime;

    public CfgWxMessage() {
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTriggerPosition() {
        return triggerPosition;
    }

    public void setTriggerPosition(String triggerPosition) {
        this.triggerPosition = triggerPosition;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateData() {
        return templateData;
    }

    public void setTemplateData(String templateData) {
        this.templateData = templateData;
    }

    public String getTemplateUrl() {
        return templateUrl;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getNewsDesc() {
        return newsDesc;
    }

    public void setNewsDesc(String newsDesc) {
        this.newsDesc = newsDesc;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getNewsPicUrl() {
        return newsPicUrl;
    }

    public void setNewsPicUrl(String newsPicUrl) {
        this.newsPicUrl = newsPicUrl;
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
}
