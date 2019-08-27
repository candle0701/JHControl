package com.jh.entity;

public class MesTaskDetail {
    private String id;

    private String taskId;

    private String num;

    private String taskWinType;

    private String unitType;

    private String winNo;

    private String del;

    private String taskWinName;

    public String getDicId() {
        return dicId;
    }

    public void setDicId(String dicId) {
        this.dicId = dicId;
    }

    private String dicId;

    public String getCountDate() {
        return countDate;
    }

    public void setCountDate(String countDate) {
        this.countDate = countDate;
    }

    private String countDate;


    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    private String beginDate;

    private String endDate;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    private String projectId;

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    private String buildingNo;

    public String getTaskWinName() {
        return taskWinName;
    }

    public void setTaskWinName(String taskWinName) {
        this.taskWinName = taskWinName;
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num == null ? null : num.trim();
    }

    public String getTaskWinType() {
        return taskWinType;
    }

    public void setTaskWinType(String taskWinType) {
        this.taskWinType = taskWinType == null ? null : taskWinType.trim();
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType == null ? null : unitType.trim();
    }

    public String getWinNo() {
        return winNo;
    }

    public void setWinNo(String winNo) {
        this.winNo = winNo == null ? null : winNo.trim();
    }
}