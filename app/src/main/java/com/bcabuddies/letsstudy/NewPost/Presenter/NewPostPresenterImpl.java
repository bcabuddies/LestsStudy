package com.bcabuddies.letsstudy.NewPost.Presenter;

import android.os.Bundle;

import com.bcabuddies.letsstudy.NewPost.view.NewPostView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewPostPresenterImpl implements NewPostPresenter{

    private FirebaseUser user;
    private FirebaseFirestore db;
    private NewPostView postView;

    public NewPostPresenterImpl(FirebaseUser user, FirebaseFirestore db) {
        this.user = user;
        this.db = db;
    }

    @Override
    public void textPost(Bundle b) {

    }

    @Override
    public void imagePost(Bundle b) {

    }

    @Override
    public void attachView(NewPostView view) {
        postView = view;
    }

    @Override
    public void detachView() {
        postView = null;
    }
}
