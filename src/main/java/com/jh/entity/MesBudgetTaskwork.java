package com.jh.entity;

import java.util.List;

public class MesBudgetTaskwork {
    private String id;

    private String taskId;

    private String procedureId;

    private String taskmanId;

    private String doneNum;

    private String reportTime;

    private String checker;

    private String checkStatus;

    private String winNo;

    private String processGroup;

    private MesUsers mesUsers;

    private String nickName;

    public String getMbtNumbers() {
        return mbtNumbers;
    }

    public void setMbtNumbers(String mbtNumbers) {
        this.mbtNumbers = mbtNumbers;
    }

    private String biginDate;
    private String endDate;
    private String mbtNumbers;

    public String getTaskWinTypeName() {
        return taskWinTypeName;
    }

    public void setTaskWinTypeName(String taskWinTypeName) {
        this.taskWinTypeName = taskWinTypeName;
    }

    private String taskWinTypeName;

    public String getWinCode() {
        return winCode;
    }

    public void setWinCode(String winCode) {
        this.winCode = winCode;
    }

    private String winCode;

    public String getBiginDate() {
        return biginDate;
    }

    public void setBiginDate(String biginDate) {
        this.biginDate = biginDate;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    private String groupName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    private MesProcessQuota mesProcessQuota;

    private String taskNum;

    private String reason;

    private String milestone;

    private String milestoneClass;
    private String projectId;

    public String getDicId() {
        return dicId;
    }

    public void setDicId(String dicId) {
        this.dicId = dicId;
    }

    private String dicId;

    public String getSumCount() {
        return sumCount;
    }

    public void setSumCount(String sumCount) {
        this.sumCount = sumCount;
    }

    private String sumCount;
    public String getMilestoneClass() {
        return milestoneClass;
    }

    public void setMilestoneClass(String milestoneClass) {
        this.milestoneClass = milestoneClass;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProcessLevel() {
        return processLevel;
    }

    public void setProcessLevel(String processLevel) {
        this.processLevel = processLevel;
    }

    private String processLevel;

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    //=============表外字段begin=============
    private String donePercent;
    private String taskName;
    private String name;
    private String downTime;
    private String deliveryTime;
    private String leftNum;
    private String price;
    private String taskWinType;
    private String mdName;
    private String mpqName;
    private String username;
    private String nickname;
    private String totalNum;



    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getTaskWinType() {
        return taskWinType;
    }

    public void setTaskWinType(String taskWinType) {
        this.taskWinType = taskWinType;
    }

    public String getMdName() {
        return mdName;
    }

    public void setMdName(String mdName) {
        this.mdName = mdName;
    }

    public String getMpqName() {
        return mpqName;
    }

    public void setMpqName(String mpqName) {
        this.mpqName = mpqName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLeftNum() {
        return leftNum;
    }

    public void setLeftNum(String leftNum) {
        this.leftNum = leftNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownTime() {
        return downTime;
    }

    public void setDownTime(String downTime) {
        this.downTime = downTime;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
//=============表外字段end=============

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDonePercent() {
        return donePercent;
    }

    public void setDonePercent(String donePercent) {
        this.donePercent = donePercent;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public MesUsers getMesUsers() {
        return mesUsers;
    }

    public void setMesUsers(MesUsers mesUsers) {
        this.mesUsers = mesUsers;
    }

    public String getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(String taskNum) {
        this.taskNum = taskNum;
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

    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId == null ? null : procedureId.trim();
    }

    public MesProcessQuota getMesProcessQuota() {
        return mesProcessQuota;
    }

    public void setMesProcessQuota(MesProcessQuota mesProcessQuota) {
        this.mesProcessQuota = mesProcessQuota;
    }

    public String getTaskmanId() {
        return taskmanId;
    }

    public void setTaskmanId(String taskmanId) {
        this.taskmanId = taskmanId == null ? null : taskmanId.trim();
    }

    public String getDoneNum() {
        return doneNum;
    }

    public void setDoneNum(String doneNum) {
        this.doneNum = doneNum == null ? null : doneNum.trim();
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime == null ? null : reportTime.trim();
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker == null ? null : checker.trim();
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus == null ? null : checkStatus.trim();
    }

    public String getWinNo() {
        return winNo;
    }

    public void setWinNo(String winNo) {
        this.winNo = winNo == null ? null : winNo.trim();
    }

    public String getProcessGroup() {
        return processGroup;
    }

    public void setProcessGroup(String processGroup) {
        this.processGroup = processGroup == null ? null : processGroup.trim();
    }


    private String beginDate;
    private String numbers;
    private String num;
    private String mother;
    private String child;
    private MesDictionaries mesDictionaries;
    private String processGroupName;

    public String getProcessGroupName() {
        return processGroupName;
    }

    public void setProcessGroupName(String processGroupName) {
        this.processGroupName = processGroupName;
    }

    public MesDictionaries getMesDictionaries() {
        return mesDictionaries;
    }

    public void setMesDictionaries(MesDictionaries mesDictionaries) {
        this.mesDictionaries = mesDictionaries;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    private String projectName;

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    private String buildingNo;
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

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }



}