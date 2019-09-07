package com.juheshi.video.entity;

public class SystemRolePermission {

    private int id;
    private Integer roleId;
    private Integer menuId;
    private String isGrant;
    private String isQuery;
    private String isInsert;
    private String idUpdate;
    private String isDelete;
    private String isPrint;
    private String isExport;
    private String isAffirm;
    private String isCancel;

    public SystemRolePermission(){}

    public SystemRolePermission(Integer roleId, Integer menuId, String isGrant, String isQuery, String isInsert, String idUpdate, String isDelete, String isPrint, String isExport, String isAffirm, String isCancel) {
        this.roleId = roleId;
        this.menuId = menuId;
        this.isGrant = isGrant;
        this.isQuery = isQuery;
        this.isInsert = isInsert;
        this.idUpdate = idUpdate;
        this.isDelete = isDelete;
        this.isPrint = isPrint;
        this.isExport = isExport;
        this.isAffirm = isAffirm;
        this.isCancel = isCancel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getIsGrant() {
        return isGrant;
    }

    public void setIsGrant(String isGrant) {
        this.isGrant = isGrant;
    }

    public String getIsQuery() {
        return isQuery;
    }

    public void setIsQuery(String isQuery) {
        this.isQuery = isQuery;
    }

    public String getIsInsert() {
        return isInsert;
    }

    public void setIsInsert(String isInsert) {
        this.isInsert = isInsert;
    }

    public String getIdUpdate() {
        return idUpdate;
    }

    public void setIdUpdate(String idUpdate) {
        this.idUpdate = idUpdate;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(String isPrint) {
        this.isPrint = isPrint;
    }

    public String getIsExport() {
        return isExport;
    }

    public void setIsExport(String isExport) {
        this.isExport = isExport;
    }

    public String getIsAffirm() {
        return isAffirm;
    }

    public void setIsAffirm(String isAffirm) {
        this.isAffirm = isAffirm;
    }

    public String getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(String isCancel) {
        this.isCancel = isCancel;
    }
}
