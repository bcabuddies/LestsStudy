package com.bcabuddies.letsstudy.Login.Model;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;

public class User implements User_interface {
    private String email, password;
    private FirebaseAuth firebaseAuth;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isValid() {
        //check if email and password field is not empty
        return !TextUtils.isEmpty(getEmail()) &&
                !TextUtils.isEmpty(getPassword());
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public void setFirebaseAuth(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }
}
