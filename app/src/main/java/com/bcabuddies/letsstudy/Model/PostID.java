package com.bcabuddies.letsstudy.Model;

import com.google.firebase.firestore.Exclude;

import androidx.annotation.NonNull;

public class PostID {
    @Exclude
    public String PostID;

    public <T extends PostID> T withID(@NonNull final String id){
        this.PostID = id;
        return (T) this;
    }
}
