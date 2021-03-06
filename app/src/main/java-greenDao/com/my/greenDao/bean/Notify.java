package com.my.greenDao.bean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "NOTIFY".
 */
public class Notify {

    private Long id;
    private String title;
    private String description;
    private String notifyContent;
    private Boolean isRead;
    private java.util.Date createDate;

    public Notify() {
    }

    public Notify(Long id) {
        this.id = id;
    }

    public Notify(Long id, String title, String description, String notifyContent, Boolean isRead, java.util.Date createDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.notifyContent = notifyContent;
        this.isRead = isRead;
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotifyContent() {
        return notifyContent;
    }

    public void setNotifyContent(String notifyContent) {
        this.notifyContent = notifyContent;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public java.util.Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }

}
