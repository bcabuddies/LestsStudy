package com.bcabuddies.letsstudy.Home.Presenter;

import android.os.Bundle;
import android.util.Log;

import com.bcabuddies.letsstudy.Home.view.HomeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

import androidx.annotation.NonNull;

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
            if (task.getResult().exists()) {
                Log.e(TAG, "user: data received "+task.getResult());
                Bundle data = new Bundle();
                data.putString("name",task.getResult().getString("name"));
                data.putString("profile",task.getResult().getString("profileURL"));
                data.putLong("points", (Long) task.getResult().get("points"));
                homeView.getUserDetails(data);
            } else {
                try {
                    Log.e(TAG, "user: error " + task.getException().getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void firebaseDataPre() {
        firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.getResult().exists()) {
                String profUrl;
                profUrl = task.getResult().getString("profileURL");
                if (profUrl == null) {
                    //  profUrl = "https://firebasestorage.googleapis.com/v0/b/letsstudy-c77c3.appspot.com/o/user_defalut_profile%2Fdefault.png?alt=media&token=027c4d7f-8452-4ff9-a4c7-263b77254bd0";
                    profUrl = "";
                }
                String name = task.getResult().getString("name");
                String courseName = task.getResult().getString("pursuing");
                String age = task.getResult().getString("age");
                Long points = (Long)task.getResult().get("points");
                Log.e(TAG, "onComplete: firebasepredata: proff " + profUrl);
                Log.e(TAG, "onComplete: firebasedatapre : " + name + "    " + profUrl + "    " + courseName);
                homeView.firebaseData(profUrl,name,age,courseName,points);
            } else {
                Log.e(TAG, "onComplete: firebasepredata: no data");
            }
        });
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
