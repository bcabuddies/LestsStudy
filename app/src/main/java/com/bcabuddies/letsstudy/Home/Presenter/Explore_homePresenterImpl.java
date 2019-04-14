package com.bcabuddies.letsstudy.Home.Presenter;

import android.util.Log;

import com.bcabuddies.letsstudy.Home.view.Explore_homeView;
import com.bcabuddies.letsstudy.Model.PostData;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Objects;

public class Explore_homePresenterImpl implements Explore_homePresenter {

    private FirebaseFirestore firebaseFirestore;
    private Explore_homeView homeView;
    private final static String TAG = "explore presenter";
    private ArrayList<PostData> postList;

    public Explore_homePresenterImpl(FirebaseFirestore firebaseFirestore) {
        this.firebaseFirestore = firebaseFirestore;
    }

    @Override
    public void search(String text) {
        Log.e(TAG, "search: text " + text);

        postList = new ArrayList<>();

        //work around for LIKE query of SQL
        Query query = firebaseFirestore.collection("Posts").orderBy("text", Query.Direction.ASCENDING)
                .startAt(text).endAt(text + "\uf8ff");
        query.get().addOnCompleteListener(task -> {
            try {
                Log.e(TAG, "search: entered try ");
                if (task.isSuccessful()) {
                    Log.e(TAG, "search: inside if");
                    postList.removeAll(postList);
                    for (DocumentSnapshot doc : Objects.requireNonNull(task.getResult())) {
                        String postID = doc.getId();
                        PostData list = Objects.requireNonNull(doc.toObject(PostData.class)).withID(postID);
                        postList.add(list);
                        Log.e(TAG, "search: postID " + postID);
                        Log.e(TAG, "search: postList size " + postList.get(0).getText());
                        Log.e(TAG, "search: postList " + postList.get(0));
                        homeView.getData(postList);
                    }
                } else {
                    Log.e(TAG, "search: no data ");
                    postList.removeAll(postList);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "search: exception " + e.getMessage());
            }
        });
    }

    @Override
    public void attachView(Explore_homeView view) {
        homeView = view;
    }

    @Override
    public void detachView() {
        homeView = null;
    }
}
