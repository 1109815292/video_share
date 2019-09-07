package com.juheshi.video.entity;

public class SysParam {
    private Integer id;
    private String paramKey;
    private String paramType;
    private String paramValue;
    private String paramDesc;

    public SysParam() {
    }

    public SysParam(String paramKey, String paramType, String paramValue, String paramDesc) {
        this.paramKey = paramKey;
        this.paramType = paramType;
        this.paramValue = paramValue;
        this.paramDesc = paramDesc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }
}
