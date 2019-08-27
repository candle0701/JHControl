package com.jh.entity;

import java.util.Date;

public class MesUsers {
    private String id;

    private String code;

    private String username;

    private String tel;

    private String address;

    private String sex;

    private Date birth;

    private String duty;

    private String note;

    private String valid;

    private String password;

    private String capability;

    private String capability1;

    private String status;

    private String salt;

    private String nickname;

    private MesRoleUsers mesRoleUsers;

    private MesDepart mesDepart;

    public String getNickUserName() {
        return nickUserName;
    }

    public void setNickUserName(String nickUserName) {
        this.nickUserName = nickUserName;
    }

    private String nickUserName;

    public MesDepart getMesDepart() {
        return mesDepart;
    }

    public void setMesDepart(MesDepart mesDepart) {
        this.mesDepart = mesDepart;
    }

    public MesRoleUsers getMesRoleUsers() {
        return mesRoleUsers;
    }

    public void setMesRoleUsers(MesRoleUsers mesRoleUsers) {
        this.mesRoleUsers = mesRoleUsers;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    //外字段
    private String roleName;
    private String departName;

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    //部门分配人员时，校验是否当前部门的字段
    private String departStatus;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String price;
    public String getDepartStatus() {
        return departStatus;
    }

    public void setDepartStatus(String departStatus) {
        this.departStatus = departStatus;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty == null ? null : duty.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid == null ? null : valid.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getCapability() {
        return capability;
    }

    public void setCapability(String capability) {
        this.capability = capability == null ? null : capability.trim();
    }

    public String getCapability1() {
        return capability1;
    }

    public void setCapability1(String capability1) {
        this.capability1 = capability1 == null ? null : capability1.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public String getCredentialsSalt() {

        return this.username + this.salt;
    }
}