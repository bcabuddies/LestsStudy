package com.bcabuddies.letsstudy.Home.Presenter;

import android.os.Bundle;
import android.util.Log;

import com.bcabuddies.letsstudy.Home.view.Feed_homeView;
import com.bcabuddies.letsstudy.Model.PostData;
import com.bcabuddies.letsstudy.Model.UserData;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Feed_homePresenterImpl implements Feed_homePresenter {

    private FirebaseUser user;
    private FirebaseFirestore db;
    private Feed_homeView homeView;
    private static final String TAG = "Feed_homePresImpl";
    private ArrayList<UserData> userList;
    private ArrayList<PostData> postList;

    public Feed_homePresenterImpl(FirebaseUser user, FirebaseFirestore db) {
        this.user = user;
        this.db = db;
    }

    //data received from View to upload
    @Override
    public void uploadText(Bundle text) {

        Map<String, Object> map = new HashMap<>();
        map.put("text", Objects.requireNonNull(text.getString("text")));
        map.put("timestamp", FieldValue.serverTimestamp());
        map.put("user", Objects.requireNonNull(text.getString("user")));
        map.put("type", Objects.requireNonNull(text.getString("type")));

        db.collection("Posts").document().set(map).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.e(TAG, "uploadText: text upload success");
                homeView.uploadSuccess();
            } else {
                String error = task.getException().getMessage();
                homeView.uploadTextError(error);
            }
        });
    }

    //get all post data from Firetore for RecyclerView
    @Override
    public void getData() {
        postList = new ArrayList<>();
        userList = new ArrayList<>();
        Query sortQuery = db.collection("Posts").orderBy("timestamp", Query.Direction.ASCENDING);
        sortQuery.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        final String postID = doc.getDocument().getId();
                        Log.e(TAG, "getData: postID " + postID);
                        Log.e(TAG, "getData: post time " + doc.getDocument().getDate("timestamp"));
                        //getting postData and setting it to the GetterSetter
                        final PostData postData = doc.getDocument().toObject(PostData.class).withID(postID);
                        Log.e(TAG, "getData: post data get");
                        Log.e(TAG, "getData: post data postData user " + postData.getUser());
                        postList.add(postData);
                        Log.e(TAG, "getData: post list data " + postList.size());

                        //send data to View;
                        homeView.getData(postList);
                    }
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
