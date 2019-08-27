package com.jh.entity;

public class MesProcessQuota {
    private String id;

    private String code;

    private String name;

    private String unit;

    private String price;

    private String unitPrice;

    private String realityDate;

    private String assistDate;

    private String type;

    private String status;

    private String del ="0";

    private String teamGroup;

    private String codeNo;

    public String getCodeNo() {
        return codeNo;
    }

    public void setCodeNo(String codeNo) {
        this.codeNo = codeNo;
    }

    public String getMilestoneClass() {
        return milestoneClass;
    }

    public void setMilestoneClass(String milestoneClass) {
        this.milestoneClass = milestoneClass;
    }

    private String milestoneClass;

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    private String milestone;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice == null ? null : unitPrice.trim();
    }

    public String getRealityDate() {
        return realityDate;
    }

    public void setRealityDate(String realityDate) {
        this.realityDate = realityDate == null ? null : realityDate.trim();
    }

    public String getAssistDate() {
        return assistDate;
    }

    public void setAssistDate(String assistDate) {
        this.assistDate = assistDate == null ? null : assistDate.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del == null ? null : del.trim();
    }

    public String getTeamGroup() {
        return teamGroup;
    }

    public void setTeamGroup(String teamGroup) {
        this.teamGroup = teamGroup == null ? null : teamGroup.trim();
    }
}