package com.jh.entity;

public class MesSettlemen {
    private String id;

    private String userId;

    private String userName;

    private String price;

    private String datemonth;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    public String getDatemonth() {
        return datemonth;
    }

    public void setDatemonth(String datemonth) {
        this.datemonth = datemonth == null ? null : datemonth.trim();
    }
}