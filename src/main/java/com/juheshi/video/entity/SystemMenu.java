package com.juheshi.video.entity;

public class SystemMenu {

    private int id;
    private String menuDesc;
    private String actionName;
    private Integer state;

    public SystemMenu(){}

    public SystemMenu(String menuDesc, String actionName, Integer state) {
        this.menuDesc = menuDesc;
        this.actionName = actionName;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
