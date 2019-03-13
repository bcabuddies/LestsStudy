package com.bcabuddies.letsstudy.Home.Presenter;

import android.os.Bundle;
import android.util.Log;

import com.bcabuddies.letsstudy.Home.view.HomeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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

    @Override
    public void attachView(HomeView view) {
        homeView = view;
    }

    @Override
    public void detachView() {
        homeView = null;
    }
}
