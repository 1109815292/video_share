package com.juheshi.video.entity;

public class SystemRole {

    private Integer id;
    private String roleName;
    private String roleDesc;
    private Integer state;

    private String stateStr;

    public SystemRole(){}

    public SystemRole(String roleName, String roleDesc, Integer state) {
        this.roleName = roleName;
        this.roleDesc = roleDesc;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getStateStr() {
        String result = "";
        switch (this.state){
            case 1: result = "正常"; break;
            case 9: result = "停用"; break;
        }
        return result;
    }

    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }
}
