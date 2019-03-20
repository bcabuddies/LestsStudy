package com.bcabuddies.letsstudy.Model;

import java.util.Date;

public class PostData extends com.bcabuddies.letsstudy.Model.PostID {
    private String text, type, url, user;
    private Date timestamp;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public PostData() {
    }

    public PostData(String text, String type, String url, String user, Date timestamp) {
        this.text = text;
        this.type = type;
        this.url = url;
        this.user = user;
        this.timestamp = timestamp;
    }

}
