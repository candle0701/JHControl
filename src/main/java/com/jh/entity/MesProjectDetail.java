package com.jh.entity;

public class MesProjectDetail {
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

    private String ver;

    private String item;

    private String projectId;

    public String getPartyWinNo() {
        return partyWinNo;
    }

    public void setPartyWinNo(String partyWinNo) {
        this.partyWinNo = partyWinNo;
    }

    private String partyWinNo;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    private String ids;
    public String getNumCount() {
        return numCount;
    }

    public void setNumCount(String numCount) {
        this.numCount = numCount;
    }

    private String numCount;
    public String getCompletion() {
        return completion;
    }

    public void setCompletion(String completion) {
        this.completion = completion;
    }

    public String getUnfinished() {
        return unfinished;
    }

    public void setUnfinished(String unfinished) {
        this.unfinished = unfinished;
    }

    public String getCompletionSquare() {
        return completionSquare;
    }

    public void setCompletionSquare(String completionSquare) {
        this.completionSquare = completionSquare;
    }

    public String getUnfinishedSquare() {
        return unfinishedSquare;
    }

    public void setUnfinishedSquare(String unfinishedSquare) {
        this.unfinishedSquare = unfinishedSquare;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String code;

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getDicId() {
        return dicId;
    }

    public void setDicId(String dicId) {
        this.dicId = dicId;
    }

    private String dicId;
    private String numbers;
    private String completion;
    private String unfinished;
    private String completionSquare;
    private String unfinishedSquare;


    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    public String getProcessLevel() {
        return processLevel;
    }

    public void setProcessLevel(String processLevel) {
        this.processLevel = processLevel;
    }

    private String procedureId;
    private String processLevel;

    public String getWinModelId() {
        return winModelId;
    }

    public void setWinModelId(String winModelId) {
        this.winModelId = winModelId;
    }

    private String winModelId;

    public String getWinName() {
        return winName;
    }

    public void setWinName(String winName) {
        this.winName = winName;
    }

    private String winName;

    public String getTaskWinType() {
        return taskWinType;
    }

    public void setTaskWinType(String taskWinType) {
        this.taskWinType = taskWinType;
    }

    private String taskWinType;

    public String getOrderFromSquare() {
        return orderFromSquare;
    }

    public void setOrderFromSquare(String orderFromSquare) {
        this.orderFromSquare = orderFromSquare;
    }

    private String orderFromSquare;
    public String getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(String taskNum) {
        this.taskNum = taskNum;
    }

    private String taskNum;

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    private String tempId;

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }

    private String del ="0";

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

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver == null ? null : ver.trim();
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item == null ? null : item.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }
}