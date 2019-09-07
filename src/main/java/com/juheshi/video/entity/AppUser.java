package com.juheshi.video.entity;

import com.juheshi.video.common.Constants;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class AppUser {
    public static final int STATE_BLOCK = 0;
    public static final int STATE_NORMAL = 1;

    public static final int USER_LEVEL_PARTNER = 1;
    public static final int USER_LEVEL_CHANNEL = 2;

    public static final int VIP_TYPE_ONLINE = 1;
    public static final int VIP_TYPE_OFFLINE = 2;

    private Integer id;//自增主键
    private String copyNo;//用户copyNo
    private String openId;
    private String unionId;
    private String userName;
    private String headImg;
    private String gender;
    private Timestamp createdTime;
    private String vipFlag;
    private Timestamp vipDeadline;
    private Integer vipType;//vip类型（1=线上vip， 2=线下vip）
    private String publishFlag;
    private Integer state;
    private Integer inviteId;
    private String inviteCopyNo;
    private Double amount;
    private String adminFlag;
    private Date deletedAt;
    private String inviteQRCode;
    private Timestamp inviteQRCodeExpiresIn;
    private Timestamp lastViewTime;
    private Integer userLevel;
    private String subscribeFlag;
    private Integer fansCount;
    private Integer fansOfFansCount;
    private Integer fansVipCount;
    private Double income;
    private String city;
    private String stationFlag;

    private AppUserPage userPage;

    private VipTypeSetting vip;

    private AppUser inviter;


    public AppUser() {
    }

    public AppUser(String copyNo, String openId, String unionId, String userName, String headImg, String gender, Timestamp createdTime,
                   String vipFlag, Timestamp vipDeadline, String publishFlag, Integer state, Integer inviteId, String inviteCopyNo,
                   Double amount, Timestamp lastViewTime, String adminFlag, Integer userLevel, Integer fansCount, Integer fansOfFansCount,
                   Integer fansVipCount, Double income, String city) {
        this.copyNo = copyNo;
        this.openId = openId;
        this.unionId = unionId;
        this.userName = userName;
        this.headImg = headImg;
        this.gender = gender;
        this.createdTime = createdTime;
        this.vipFlag = vipFlag;
        this.vipDeadline = vipDeadline;
        this.publishFlag = publishFlag;
        this.state = state;
        this.inviteId = inviteId;
        this.inviteCopyNo = inviteCopyNo;
        this.amount = amount;
        this.lastViewTime = lastViewTime;
        this.adminFlag = adminFlag;
        this.userLevel = userLevel;
        this.fansCount = fansCount;
        this.fansOfFansCount = fansOfFansCount;
        this.fansVipCount = fansVipCount;
        this.income = income;
        this.city = city;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCopyNo() {
        return copyNo;
    }

    public void setCopyNo(String copyNo) {
        this.copyNo = copyNo;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public String getVipFlag() {
        if (vipFlag == null || "".equals(vipFlag))
            return Constants.NO;
        else if (vipFlag.toUpperCase().equals(Constants.NO))
            return vipFlag;
        else if (vipFlag.toUpperCase().equals(Constants.YES)) { //如果会员标识为Y
            //判断过期时间
            if (vipDeadline == null) { //如果过期时间为null 表示永不过期
                return vipFlag;
            } else { //如果有过期时间
                if (Calendar.getInstance().getTime().getTime() - vipDeadline.getTime() >= 0) {//当前时间与过期时间差值大于等于0，表示已经到了过期时间
                    return Constants.NO;
                } else { //当前时间与过期时间差值小于0 表示还未过期
                    return vipFlag;
                }
            }
        } else
            return Constants.NO;
    }

    public void setVipFlag(String vipFlag) {
        this.vipFlag = vipFlag;
    }

    public Timestamp getVipDeadline() {
        return vipDeadline;
    }

    public void setVipDeadline(Timestamp vipDeadline) {
        this.vipDeadline = vipDeadline;
    }

    public String getPublishFlag() {
        return publishFlag;
    }

    public void setPublishFlag(String publishFlag) {
        this.publishFlag = publishFlag;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getInviteId() {
        return inviteId;
    }

    public void setInviteId(Integer inviteId) {
        this.inviteId = inviteId;
    }

    public String getInviteCopyNo() {
        return inviteCopyNo;
    }

    public void setInviteCopyNo(String inviteCopyNo) {
        this.inviteCopyNo = inviteCopyNo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getAdminFlag() {
        return adminFlag;
    }

    public void setAdminFlag(String adminFlag) {
        this.adminFlag = adminFlag;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public AppUserPage getUserPage() {
        return userPage;
    }

    public void setUserPage(AppUserPage userPage) {
        this.userPage = userPage;
    }

    public VipTypeSetting getVip() {
        return vip;
    }

    public void setVip(VipTypeSetting vip) {
        this.vip = vip;
    }

    public String getInviteQRCode() {
        return inviteQRCode;
    }

    public void setInviteQRCode(String inviteQRCode) {
        this.inviteQRCode = inviteQRCode;
    }

    public Timestamp getInviteQRCodeExpiresIn() {
        return inviteQRCodeExpiresIn;
    }

    public void setInviteQRCodeExpiresIn(Timestamp inviteQRCodeExpiresIn) {
        this.inviteQRCodeExpiresIn = inviteQRCodeExpiresIn;
    }

    public Timestamp getLastViewTime() {
        return lastViewTime;
    }

    public void setLastViewTime(Timestamp lastViewTime) {
        this.lastViewTime = lastViewTime;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public String getSubscribeFlag() {
        return subscribeFlag;
    }

    public void setSubscribeFlag(String subscribeFlag) {
        this.subscribeFlag = subscribeFlag;
    }

    public Integer getFansCount() {
        return fansCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public Integer getFansOfFansCount() {
        return fansOfFansCount;
    }

    public void setFansOfFansCount(Integer fansOfFansCount) {
        this.fansOfFansCount = fansOfFansCount;
    }

    public Integer getFansVipCount() {
        return fansVipCount;
    }

    public void setFansVipCount(Integer fansVipCount) {
        this.fansVipCount = fansVipCount;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public AppUser getInviter() {
        return inviter;
    }

    public void setInviter(AppUser inviter) {
        this.inviter = inviter;
    }

    public String getStationFlag() {
        return stationFlag;
    }

    public void setStationFlag(String stationFlag) {
        this.stationFlag = stationFlag;
    }

    public Integer getVipType() {
        return vipType;
    }

    public void setVipType(Integer vipType) {
        this.vipType = vipType;
    }

}
