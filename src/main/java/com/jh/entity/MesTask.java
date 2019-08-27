package com.jh.entity;

import java.util.List;

public class MesTask {
    private String id;

    private String projectId;

    private String taskType;

    private String taskName;

    private String buildingNo;

    private String floorNo;

    private String downTime;

    private String deliveryTime;

    private String creater;

    private String createTime;

    private String checker;

    private String checkTime;

    private String urgentStatus;

    private String taskStatus;
    private String taskWinTypeName;
    private String milestoneClass;
    private String num;
    private String doneNum;

    private MesProject mesProject;
    private MesUsers mesUsers;

    public MesUsers getMesUsers() {
        return mesUsers;
    }

    public void setMesUsers(MesUsers mesUsers) {
        this.mesUsers = mesUsers;
    }

    private String measureArea;
    private String totalNum;
    private String isSupply;
    private String floorWinType;
    private String winType;
    private String winTypeName;

    private String checkReason;
    private String del;

    private String projectWinNamenum;
    private String name;
    private String procedureId;
    private String taskId;
    private String winNo;

    public String getTaskWinTypeName() {
        return taskWinTypeName;
    }

    public void setTaskWinTypeName(String taskWinTypeName) {
        this.taskWinTypeName = taskWinTypeName;
    }

    public String getMilestoneClass() {
        return milestoneClass;
    }

    public void setMilestoneClass(String milestoneClass) {
        this.milestoneClass = milestoneClass;
    }

    private String processLevel;

    public String getWinTypeName() {
        return winTypeName;
    }

    public void setWinTypeName(String winTypeName) {
        this.winTypeName = winTypeName;
    }

    public String getWinType() {
        return winType;
    }

    public void setWinType(String winType) {
        this.winType = winType;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getProjectWinNamenum() {
        return projectWinNamenum;
    }

    public void setProjectWinNamenum(String projectWinNamenum) {
        this.projectWinNamenum = projectWinNamenum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getWinNo() {
        return winNo;
    }

    public void setWinNo(String winNo) {
        this.winNo = winNo;
    }

    public String getProcessLevel() {
        return processLevel;
    }

    public void setProcessLevel(String processLevel) {
        this.processLevel = processLevel;
    }

    public String getDoneNum() {
        return doneNum;
    }

    public void setDoneNum(String doneNum) {
        this.doneNum = doneNum;
    }

    public String getCheckReason() {
        return checkReason;
    }

    public void setCheckReason(String checkReason) {
        this.checkReason = checkReason;
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }

    private MesProjectDetail mesProjectDetail;

    public MesProjectDetail getMesProjectDetail() {
        return mesProjectDetail;
    }

    public void setMesProjectDetail(MesProjectDetail mesProjectDetail) {
        this.mesProjectDetail = mesProjectDetail;
    }

    private MesDictionaries mesDictionaries;

    public MesDictionaries getMesDictionaries() {
        return mesDictionaries;
    }

    public void setMesDictionaries(MesDictionaries mesDictionaries) {
        this.mesDictionaries = mesDictionaries;
    }

    public String getMeasureArea() {
        return measureArea;
    }

    public void setMeasureArea(String measureArea) {
        this.measureArea = measureArea;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getIsSupply() {
        return isSupply;
    }

    public void setIsSupply(String isSupply) {
        this.isSupply = isSupply;
    }

    public String getFloorWinType() {
        return floorWinType;
    }

    public void setFloorWinType(String floorWinType) {
        this.floorWinType = floorWinType;
    }

    public MesProject getMesProject() {
        return mesProject;
    }

    public void setMesProject(MesProject mesProject) {
        this.mesProject = mesProject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType == null ? null : taskType.trim();
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo == null ? null : buildingNo.trim();
    }

    public String getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(String floorNo) {
        this.floorNo = floorNo == null ? null : floorNo.trim();
    }

    public String getDownTime() {
        return downTime;
    }

    public void setDownTime(String downTime) {
        this.downTime = downTime == null ? null : downTime.trim();
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime == null ? null : deliveryTime.trim();
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker == null ? null : checker.trim();
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime == null ? null : checkTime.trim();
    }

    public String getUrgentStatus() {
        return urgentStatus;
    }

    public void setUrgentStatus(String urgentStatus) {
        this.urgentStatus = urgentStatus == null ? null : urgentStatus.trim();
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus == null ? null : taskStatus.trim();
    }
    private String processDate;
    private String biginDate;
    private String endDate;
    public String getProcessDate() {
        return processDate;
    }

    public void setProcessDate(String processDate) {
        this.processDate = processDate;
    }

    public String getBiginDate() {
        return biginDate;
    }

    public void setBiginDate(String biginDate) {
        this.biginDate = biginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    private String typeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getProceeName() {
        return proceeName;
    }

    public void setProceeName(String proceeName) {
        this.proceeName = proceeName;
    }

    public String getProceeId() {
        return proceeId;
    }

    public void setProceeId(String proceeId) {
        this.proceeId = proceeId;
    }

    private String typeId;
    private String proceeName;
    private String proceeId;

}