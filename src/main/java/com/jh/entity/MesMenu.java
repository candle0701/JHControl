package com.jh.entity;


import java.util.List;


public class MesMenu {
    private String id;
    private String menuName;
    private String ExtendsMenuId;
    private Integer sort;
    private String url;
    private String permission;
    private String imgclass;

    //menu_role_menu
    private MesRoleMenu mesRoleMenu;

    public MesRoleMenu getMesRoleMenu() {
        return mesRoleMenu;
    }

    public void setMesRoleMenu(MesRoleMenu mesRoleMenu) {
        this.mesRoleMenu = mesRoleMenu;
    }

    public String getMrmRoleId() {
        return mrmRoleId;
    }

    public void setMrmRoleId(String mrmRoleId) {
        this.mrmRoleId = mrmRoleId;
    }

    public String getMrmId() {
        return mrmId;
    }

    public void setMrmId(String mrmId) {
        this.mrmId = mrmId;
    }

    public String getMrmMenuId() {
        return mrmMenuId;
    }

    public void setMrmMenuId(String mrmMenuId) {
        this.mrmMenuId = mrmMenuId;
    }

    private String mrmStatus;
    private String mrmRoleId;
    private String mrmId;
    private String mrmMenuId;

    public String getMrmStatus() {
        return mrmStatus;
    }

    public void setMrmStatus(String mrmStatus) {
        this.mrmStatus = mrmStatus;
    }

    private List<MesButton> buttonList;

    public List<MesButton> getButtonList() {
        return buttonList;
    }

    public void setButtonList(List<MesButton> buttonList) {
        this.buttonList = buttonList;
    }

    public String getImgclass() {
        return imgclass;
    }

    public void setImgclass(String imgclass) {
        this.imgclass = imgclass;
    }

    // 子菜单
    private List<MesMenu> childMesMenus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getExtendsMenuId() {
        return ExtendsMenuId;
    }

    public void setExtendsMenuId(String extendsMenuId) {
        ExtendsMenuId = extendsMenuId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public List<MesMenu> getChildMesMenus() {
        return childMesMenus;
    }

    public void setChildMesMenus(List<MesMenu> childMesMenus) {
        this.childMesMenus = childMesMenus;
    }
}
