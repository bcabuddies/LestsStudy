package com.bcabuddies.letsstudy.Home.Presenter;

import android.os.Bundle;
import android.util.Log;

import com.bcabuddies.letsstudy.Home.view.HomeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomePresenterImpl implements HomePresenter {

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private HomeView homeView;

    public HomePresenterImpl(FirebaseAuth auth, FirebaseFirestore firebaseFirestore) {
        this.auth = auth;
        this.firebaseFirestore = firebaseFirestore;
    }

    @Override
    public void user(FirebaseUser user) {
        firebaseFirestore.collection("Users").document(user.getUid()).get().addOnCompleteListener(task -> {
            if (Objects.requireNonNull(task.getResult()).exists()) {
                Bundle data = new Bundle();
                Log.e(TAG, "user: data received " + task.getResult());
                data.putString("name", task.getResult().getString("name"));
                data.putString("profile", task.getResult().getString("profileURL"));
                homeView.getUserDetails(data);
            } else {
                try {
                    Log.e(TAG, "user: error " + Objects.requireNonNull(task.getException()).getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void firebaseDataPre() {
        firebaseFirestore.collection("Users").document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).get().addOnCompleteListener(task -> {
            if (Objects.requireNonNull(task.getResult()).exists()) {
                String profUrl;
                profUrl = task.getResult().getString("profileURL");
                if (profUrl == null) {
                    //  profUrl = "https://firebasestorage.googleapis.com/v0/b/letsstudy-c77c3.appspot.com/o/user_defalut_profile%2Fdefault.png?alt=media&token=027c4d7f-8452-4ff9-a4c7-263b77254bd0";
                    profUrl = "";
                }
                String name = task.getResult().getString("name");
                String courseName = task.getResult().getString("pursuing");
                String age = task.getResult().getString("age");
                Long points = (Long) task.getResult().get("points");
                Log.e(TAG, "onComplete: firebasepredata: prof " + profUrl);
                Log.e(TAG, "onComplete: firebasedatapre : " + name + "    " + profUrl + "    " + courseName);
                homeView.firebaseData(profUrl, name, age, courseName, Objects.requireNonNull(points));
            } else {
                Log.e(TAG, "onComplete: firebasepredata: no data");
            }
        });
    }

    @Override
    public void userPoints(FirebaseUser user) {
        try {
            firebaseFirestore.collection("Users").document(user.getUid()).addSnapshotListener((documentSnapshot, e) -> {
                try {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Bundle data = new Bundle();
                        data.putLong("points", (Long) Objects.requireNonNull(documentSnapshot.getData()).get("points"));
                        Log.e(TAG, "onEvent: points" + documentSnapshot.getData().get("points"));
                        homeView.getUserPoints(data);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    Log.e(TAG, "userPoints: exception in getting points "+e1.getMessage() );
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "userPoints: exception " + e.getMessage());
        }
    }

    @Override
    public void attachView(HomeView view) {
        homeView = view;
    }

    @Override
    public void detachView() {
        homeView = null;
    }
}
