package com.jh.entity;

public class MesMessage {
    private String id;

    private String content;

    private String title;

    private String createDate;

    private String readDate;

    private String sender;

    private String reciver;

    private String del;

    private String messageType;

    private String exta1;

    private String exta2;

    private String isRead;

    private String reciverId;

    private String senderId;

    private String exta3;

    private String exta4;

    private String exta5;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate == null ? null : createDate.trim();
    }

    public String getReadDate() {
        return readDate;
    }

    public void setReadDate(String readDate) {
        this.readDate = readDate == null ? null : readDate.trim();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender == null ? null : sender.trim();
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver == null ? null : reciver.trim();
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del == null ? null : del.trim();
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType == null ? null : messageType.trim();
    }

    public String getExta1() {
        return exta1;
    }

    public void setExta1(String exta1) {
        this.exta1 = exta1 == null ? null : exta1.trim();
    }

    public String getExta2() {
        return exta2;
    }

    public void setExta2(String exta2) {
        this.exta2 = exta2 == null ? null : exta2.trim();
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead == null ? null : isRead.trim();
    }

    public String getReciverId() {
        return reciverId;
    }

    public void setReciverId(String reciverId) {
        this.reciverId = reciverId == null ? null : reciverId.trim();
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId == null ? null : senderId.trim();
    }

    public String getExta3() {
        return exta3;
    }

    public void setExta3(String exta3) {
        this.exta3 = exta3 == null ? null : exta3.trim();
    }

    public String getExta4() {
        return exta4;
    }

    public void setExta4(String exta4) {
        this.exta4 = exta4 == null ? null : exta4.trim();
    }

    public String getExta5() {
        return exta5;
    }

    public void setExta5(String exta5) {
        this.exta5 = exta5 == null ? null : exta5.trim();
    }
}