package com.jh.entity;

public class MesProjectDetailLog {


    private String id;

    private String winNo;

    private String winTypeId;

    private String num;

    private String blueprintWidth;

    private String drawingHeight;

    private String winWidth;

    private String winHeight;

    private String preTotal;

    private String price;

    private String createDate;

    private String projectId;

    private String status;

    private String createBy;

    private String partyWinNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getWinNo() {
        return winNo;
    }

    public void setWinNo(String winNo) {
        this.winNo = winNo == null ? null : winNo.trim();
    }

    public String getWinTypeId() {
        return winTypeId;
    }

    public void setWinTypeId(String winTypeId) {
        this.winTypeId = winTypeId == null ? null : winTypeId.trim();
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num == null ? null : num.trim();
    }

    public String getBlueprintWidth() {
        return blueprintWidth;
    }

    public void setBlueprintWidth(String blueprintWidth) {
        this.blueprintWidth = blueprintWidth == null ? null : blueprintWidth.trim();
    }

    public String getDrawingHeight() {
        return drawingHeight;
    }

    public void setDrawingHeight(String drawingHeight) {
        this.drawingHeight = drawingHeight == null ? null : drawingHeight.trim();
    }

    public String getWinWidth() {
        return winWidth;
    }

    public void setWinWidth(String winWidth) {
        this.winWidth = winWidth == null ? null : winWidth.trim();
    }

    public String getWinHeight() {
        return winHeight;
    }

    public void setWinHeight(String winHeight) {
        this.winHeight = winHeight == null ? null : winHeight.trim();
    }

    public String getPreTotal() {
        return preTotal;
    }

    public void setPreTotal(String preTotal) {
        this.preTotal = preTotal == null ? null : preTotal.trim();
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate == null ? null : createDate.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public String getPartyWinNo() {
        return partyWinNo;
    }

    public void setPartyWinNo(String partyWinNo) {
        this.partyWinNo = partyWinNo == null ? null : partyWinNo.trim();
    }
}