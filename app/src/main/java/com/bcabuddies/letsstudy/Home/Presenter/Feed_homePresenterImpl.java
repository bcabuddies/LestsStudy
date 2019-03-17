package com.bcabuddies.letsstudy.Home.Presenter;

import android.os.Bundle;
import android.util.Log;

import com.bcabuddies.letsstudy.Home.view.Feed_homeView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Feed_homePresenterImpl implements Feed_homePresenter {

    private FirebaseUser user;
    private FirebaseFirestore db;
    private Feed_homeView homeView;
    private static final String TAG = "Feed_homePresImpl";

    public Feed_homePresenterImpl(FirebaseUser user, FirebaseFirestore db) {
        this.user = user;
        this.db = db;
    }

    //data received from View to upload
    @Override
    public void uploadText(Bundle text) {

        Map<String,Object> map = new HashMap<>();
        map.put("text", Objects.requireNonNull(text.getString("text")));
        map.put("time", FieldValue.serverTimestamp());
        map.put("user", Objects.requireNonNull(text.getString("user")));
        map.put("type", Objects.requireNonNull(text.getString("type")));

        db.collection("Posts").document().set(map).addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               Log.e(TAG, "uploadText: text upload success" );
               homeView.uploadSuccess();
           } else {
               String error = task.getException().getMessage();
               homeView.uploadTextError(error);
           }
        });
    }

    //get all post data from Firestore for RecyclerView
    @Override
    public void getData() {
        db.collection("Posts").document().get().addOnCompleteListener(task -> {
            if (task.getResult().exists()) {
                Bundle b = new Bundle();
                String postType = task.getResult().getString("type");
                String text = task.getResult().getString("text");
                String user = task.getResult().getString("user");
                try {
                    if (postType.contains("photo")){
                        String photoLink = task.getResult().getString("url");
                        b.putString("photoLink", photoLink);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "getData: exception "+e.getMessage() );
                }
                b.putString("text", text);
                b.putString("user", user);
                //sending data to Feed_homeFrag
                homeView.getData(b);
            } else {
                try {
                    homeView.uploadTextError(task.getException().getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "getData: exception "+e.getMessage() );
                }
            }
        });
    }

    @Override
    public void attachView(Feed_homeView view) {
        homeView = view;
    }

    @Override
    public void detachView() {
        homeView = null;
    }
}
