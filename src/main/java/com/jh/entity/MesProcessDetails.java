package com.jh.entity;

public class MesProcessDetails {
    private String id;

    private String name;

    private String numbers;

    private String unit;

    private String del;

    private String wmcId;


    private String price;
    private String realityDate;

    public String getRealityDate() {
        return realityDate;
    }

    public void setRealityDate(String realityDate) {
        this.realityDate = realityDate;
    }

    public String getAssistDate() {
        return assistDate;
    }

    public void setAssistDate(String assistDate) {
        this.assistDate = assistDate;
    }

    private String assistDate;

    public String getWinId() {
        return winId;
    }

    public void setWinId(String winId) {
        this.winId = winId;
    }

    private String winId;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers == null ? null : numbers.trim();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del == null ? null : del.trim();
    }

    public String getWmcId() {
        return wmcId;
    }

    public void setWmcId(String wmcId) {
        this.wmcId = wmcId == null ? null : wmcId.trim();
    }
}