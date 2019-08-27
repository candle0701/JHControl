package com.jh.entity;

public class MesWinModel {
    private String winId;

    private String winName;

    private String winDictionariesId;

    private String winUrl;

    private String createDate;

    private String del;

    private String creator;

    private String dicId;

    private String dicName;

    private String divValue;
    private String modelStatus;
    public String getModelStatus() {
        return modelStatus;
    }

    public void setModelStatus(String modelStatus) {
        this.modelStatus = modelStatus;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    private String code;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAuditBy() {
        return auditBy;
    }

    public void setAuditBy(String auditBy) {
        this.auditBy = auditBy;
    }

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }
    private String numebrs;

    public String getNumebrs() {
        return numebrs;
    }

    public void setNumebrs(String numebrs) {
        this.numebrs = numebrs;
    }

    private String status;
    private String remark;
    private String auditBy;
    private String auditDate;
    public String getDivValue() {
        return divValue;
    }

    public void setDivValue(String divValue) {
        this.divValue = divValue;
    }



    public String getDicId() {
        return dicId;
    }

    public void setDicId(String dicId) {
        this.dicId = dicId;
    }

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }




    public String getWinId() {
        return winId;
    }

    public void setWinId(String winId) {
        this.winId = winId == null ? null : winId.trim();
    }

    public String getWinName() {
        return winName;
    }

    public void setWinName(String winName) {
        this.winName = winName == null ? null : winName.trim();
    }

    public String getWinDictionariesId() {
        return winDictionariesId;
    }

    public void setWinDictionariesId(String winDictionariesId) {
        this.winDictionariesId = winDictionariesId == null ? null : winDictionariesId.trim();
    }

    public String getWinUrl() {
        return winUrl;
    }

    public void setWinUrl(String winUrl) {
        this.winUrl = winUrl == null ? null : winUrl.trim();
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate == null ? null : createDate.trim();
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del == null ? null : del.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }
}