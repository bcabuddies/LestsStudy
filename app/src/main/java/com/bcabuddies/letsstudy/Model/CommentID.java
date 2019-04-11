package com.bcabuddies.letsstudy.Model;

import com.google.firebase.firestore.Exclude;

import androidx.annotation.NonNull;

public class CommentID {
    @Exclude
    public String CommentID;

    public <T extends CommentID> T withID(@NonNull final String id) {
        this.CommentID = id;
        return (T) this;
    }
}