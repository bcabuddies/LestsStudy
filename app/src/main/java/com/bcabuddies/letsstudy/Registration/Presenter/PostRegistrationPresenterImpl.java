package com.bcabuddies.letsstudy.Registration.Presenter;

import android.text.TextUtils;
import android.util.Log;

import com.bcabuddies.letsstudy.Registration.view.PostRegistrationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

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
            data.put("age", age);
            if (!(profileUri == null)) {
                data.put("profileURL", profileUri);
            }
            data.put("pursuing", pursuing);
            data.put("uid", uid);
            Log.e(TAG, "uploadData: data ready to upload " + data);
            db.collection("Users").document(uid).update(data).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    postRegView.detailUploadSuccess();
                } else {
                    try {
                        postRegView.detailsUploadError(task.getException().getMessage());
                        Log.e(TAG, "uploadData: data uploaded successfully");
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

    public void firebaseDataPre() {
        db.collection("Users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
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
                    Log.e(TAG, "onComplete: firebasepredata: proff " + profUrl);
                    postRegView.firebasePreData(name, profUrl, courseName, age);
                    Log.e(TAG, "onComplete: firebasedatapre : " + name + "    " + profUrl + "    " + courseName);
                } else {
                    Log.e(TAG, "onComplete: firebasepredata: no data");
                }
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
