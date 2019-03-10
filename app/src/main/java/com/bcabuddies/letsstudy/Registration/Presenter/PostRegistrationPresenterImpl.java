package com.bcabuddies.letsstudy.Registration.Presenter;

import android.text.TextUtils;
import android.util.Log;

import com.bcabuddies.letsstudy.Registration.view.PostRegistrationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PostRegistrationPresenterImpl implements PostRegistrationPresenter {

    private PostRegistrationView postRegView;
    private FirebaseFirestore db;
    private FirebaseUser user;

    public PostRegistrationPresenterImpl(FirebaseFirestore db, FirebaseUser user) {
        this.db = db;
        this.user = user;
    }

    @Override
    public void uploadData(String name, String age, String profileUri, String pursuing) {
        if (TextUtils.isEmpty(name)) {
            postRegView.showValidationError();
        } else {
            String uid = user.getUid();
            Map<String, Object> data = new HashMap<>();

            data.put("name", name);
            data.put("age",age);
            data.put("profileURL",profileUri);
            data.put("pursuing",pursuing);
            data.put("uid",uid);

            Log.e(TAG, "uploadData: data ready to upload "+data );

            db.collection("Users").document(uid).set(data).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    postRegView.detailUploadSuccess();
                } else {
                    try {
                        postRegView.detailsUploadError(task.getException().getMessage());
                        Log.e(TAG, "uploadData: data uploaded successfully" );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void getMenu() {
        Log.e(TAG, "getMenu: get menu started ");
        db.collection("PreData").document("Education").get().addOnCompleteListener(task -> {
            if (task.getResult().exists()) {
                Log.e(TAG, "getMenu: data found ");
                String menu_item = task.getResult().getString("menu");
                Log.e(TAG, "getMenu: data " + menu_item);
                String[] parts = menu_item.split(", ");
                Log.e(TAG, "getMenu: " + parts);
                postRegView.pursuingMenu(parts);
            } else {
                //no data
                Log.e(TAG, "getMenu: no data ");
            }
        });
    }

    @Override
    public void attachView(PostRegistrationView view) {
        postRegView = view;
    }

    @Override
    public void detachView() {
        postRegView = null;
    }
}
