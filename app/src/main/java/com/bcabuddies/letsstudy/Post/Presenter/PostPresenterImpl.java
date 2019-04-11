package com.bcabuddies.letsstudy.Post.Presenter;

import android.util.Log;

import com.bcabuddies.letsstudy.Post.view.PostView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class PostPresenterImpl implements PostPresenter {

    private String postID;
    private FirebaseFirestore firebaseFirestore;
    private final static String TAG = "PostPresenterImpl.java";
    private PostView postView;

    public PostPresenterImpl(String postID, FirebaseFirestore firebaseFirestore) {
        this.postID = postID;
        this.firebaseFirestore = firebaseFirestore;
    }

    @Override
    public void getPost() {
        firebaseFirestore.collection("Posts").document(postID).get().addOnCompleteListener(task -> {
            if (Objects.requireNonNull(task.getResult()).exists()) {
                HashMap<String, Object> dataMap = new HashMap<>();

                final String postUser = task.getResult().getString("user");
                final String desc = task.getResult().getString("text");
                final Date date = task.getResult().getDate("timestamp");

                //if the post is only text
                try {
                    String postUrl = task.getResult().getString("url");
                    if (postUrl.isEmpty()) {
                        postUrl = "no photo";
                        dataMap.put("photo", postUrl);
                    } else {
                        dataMap.put("photo", postUrl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                firebaseFirestore.collection("Users").document(postUser).get().addOnCompleteListener(task1 -> {
                    if (Objects.requireNonNull(task1.getResult()).exists()) {
                        String name = task1.getResult().getString("name");
                        String userImage = task1.getResult().getString("profileURL");
                        Log.e(TAG, "getPost: name "+name);

                        dataMap.put("name", name);
                        dataMap.put("desc", desc);
                        dataMap.put("date", Objects.requireNonNull(date));
                        dataMap.put("profile", userImage);

                        postView.setPost(dataMap);
                    } else {
                        Log.e(TAG, "getPost: error no name "+task1.getException().getMessage() );
                    }
                });

            } else {
                Log.e(TAG, "getPost: no post found ");
            }
        });
    }

    @Override
    public void attachView(PostView view) {
        postView = view;
    }

    @Override
    public void detachView() {
        postView = null;
    }
}
