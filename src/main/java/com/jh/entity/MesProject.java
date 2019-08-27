package com.jh.entity;

import java.util.Date;
import java.util.List;

public class MesProject {
    private String projectId;

    private String projectName;

    private String projectAddress;

    private String projectRemark;

    private String state;

    private String contract;

    private String bdate;

    private String edate;

    private String man;

    private String checker;

    private String checkDate;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    private String createDate;

    public List<MesProjectDetail> getList() {
        return list;
    }

    public void setList(List<MesProjectDetail> list) {
        this.list = list;
    }

    private List<MesProjectDetail> list;
    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }

    private String del="0";

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private int count;
    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    private List<MesProject> MesProjectList;

    public List<MesProject> getMesProjectList() {
        return MesProjectList;
    }

    public void setMesProjectList(List<MesProject> mesProjectList) {
        MesProjectList = mesProjectList;
    }


    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress == null ? null : projectAddress.trim();
    }

    public String getProjectRemark() {
        return projectRemark;
    }

    public void setProjectRemark(String projectRemark) {
        this.projectRemark = projectRemark == null ? null : projectRemark.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract == null ? null : contract.trim();
    }


    public String getMan() {
        return man;
    }

    public void setMan(String man) {
        this.man = man == null ? null : man.trim();
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker == null ? null : checker.trim();
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }
}