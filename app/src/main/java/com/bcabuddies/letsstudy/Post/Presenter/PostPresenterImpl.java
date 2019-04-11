package com.bcabuddies.letsstudy.Post.Presenter;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.bcabuddies.letsstudy.Model.CommentData;
import com.bcabuddies.letsstudy.Post.view.PostView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class PostPresenterImpl implements PostPresenter {

    private String postID;
    private FirebaseFirestore firebaseFirestore;
    private final static String TAG = "PostPresenterImpl.java";
    private PostView postView;
    private ArrayList<CommentData> commentDataList;

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
                        Log.e(TAG, "getPost: name " + name);

                        dataMap.put("name", name);
                        dataMap.put("desc", desc);
                        dataMap.put("date", Objects.requireNonNull(date));
                        dataMap.put("profile", userImage);

                        postView.setPost(dataMap);
                    } else {
                        Log.e(TAG, "getPost: error no name " + task1.getException().getMessage());
                    }
                });

            } else {
                Log.e(TAG, "getPost: no post found ");
            }
        });
    }

    @Override
    public void postComment(String comment, String user, String postID) {
        HashMap<String, Object> commentMap = new HashMap<>();

        commentMap.put("text", comment);
        commentMap.put("timestamp", FieldValue.serverTimestamp());
        commentMap.put("uid", user);
        commentMap.put("postID", postID);

        firebaseFirestore.collection("Posts").document(postID).collection("Comments").add(commentMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.e(TAG, "postComment: comment passed");
            } else {
                Log.e(TAG, "postComment: error " + task.getException().getMessage());
                Toast.makeText(postView.getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void getComments() {
        Query sortComments = firebaseFirestore.collection("Posts").document(postID)
                .collection("Comments").orderBy("timestamp", Query.Direction.DESCENDING);
        commentDataList = new ArrayList<>();

        sortComments.addSnapshotListener((Activity) postView.getContext(), (queryDocumentSnapshots, e) -> {
            try {
                if (!queryDocumentSnapshots.isEmpty()) {
                    Log.e(TAG, "getComments: comments available ");
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            final String commentID = doc.getDocument().getId();
                            final CommentData commentList = doc.getDocument().toObject(CommentData.class)
                                    .withID(commentID);
                            commentDataList.add(commentList);
                            Log.e(TAG, "getComments: list size in impl " + commentDataList.size());
                            postView.setComments(commentDataList);
                        }
                    }
                } else {
                    Log.e(TAG, "getComments: comments not available ");
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                Log.e(TAG, "getComments: exception " + e1.getMessage());
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
