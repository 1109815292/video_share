package com.juheshi.video.entity;

import java.sql.Timestamp;

public class SystemUser {

    public static final int TYPE_ADMIN = 1;
    public static final int TYPE_PARTNER = 2;
    public static final int TYPE_CHANNEL = 3;
    public static final int TYPE_STATION = 4;

    private Integer id;
    private Integer parentId;
    private String userName;
    private String fullName;
    private String mobile;
    private String password;
    private Integer roleId;
    private Integer state;
    private Integer type;
    private String copyNo;
    private Timestamp createdTime;

    private SystemUser parent;

    public SystemUser(){}

    public SystemUser(Integer parentId, String userName, String fullName, String mobile, String password, Integer roleId, Integer state, Timestamp createdTime) {
        this.parentId = parentId;
        this.userName = userName;
        this.fullName = fullName;
        this.mobile = mobile;
        this.password = password;
        this.roleId = roleId;
        this.state = state;
        this.createdTime = createdTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public SystemUser getParent() {
        return parent;
    }

    public void setParent(SystemUser parent) {
        this.parent = parent;
    }
}
