package com.bcabuddies.letsstudy.Model;

import java.util.Date;

public class CommentData extends CommentID{
    private String text, uid, postID;
    private Date timestamp;

    public CommentData(){
    }

    public CommentData(String text, String uid, String postID, Date timestamp) {
        this.text = text;
        this.uid = uid;
        this.postID = postID;
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }
}
