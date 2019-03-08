package com.bcabuddies.letsstudy.Registration.Presenter;

import android.text.TextUtils;

import com.bcabuddies.letsstudy.Registration.view.PostRegistrationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PostRegistrationPresenterImpl implements PostRegistrationPresenter{

    private PostRegistrationView postRegView;
    private FirebaseFirestore db;
    private FirebaseUser user;

    public PostRegistrationPresenterImpl(FirebaseFirestore db, FirebaseUser user) {
        this.db = db;
        this.user = user;
    }

    @Override
    public void uploadData(String name) {
        if (TextUtils.isEmpty(name)){
            postRegView.showValidationError();
        } else {
            String uid = user.getUid();
            Map<String, Object> data = new HashMap<>();

            data.put("name",name);

            db.collection("Users").document(uid).set(data).addOnCompleteListener(task -> {
               if (task.isSuccessful()){
                   postRegView.detailUploadSuccess();
               } else {
                   try {
                       postRegView.detailsUploadError(task.getException().getMessage());
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
            });
        }
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
