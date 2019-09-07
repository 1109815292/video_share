package com.juheshi.video.entity;

import java.sql.Timestamp;

public class VarParam {
    private Integer id;
    private String varName;
    private String varValue;
    private Integer varExpiresIn;
    private String varDesc;
    private String remark;
    private Timestamp createdTime;
    private Timestamp updatedTime;

    public VarParam() {

    }

    public VarParam(Integer id, String varName, String varValue, Integer varExpiresIn, String varDesc, String remark, Timestamp createdTime, Timestamp updatedTime) {
        this.id = id;
        this.varName = varName;
        this.varValue = varValue;
        this.varExpiresIn = varExpiresIn;
        this.varDesc = varDesc;
        this.remark = remark;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getVarValue() {
        return varValue;
    }

    public void setVarValue(String varValue) {
        this.varValue = varValue;
    }

    public Integer getVarExpiresIn() {
        return varExpiresIn;
    }

    public void setVarExpiresIn(Integer varExpiresIn) {
        this.varExpiresIn = varExpiresIn;
    }

    public String getVarDesc() {
        return varDesc;
    }

    public void setVarDesc(String varDesc) {
        this.varDesc = varDesc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }
}
