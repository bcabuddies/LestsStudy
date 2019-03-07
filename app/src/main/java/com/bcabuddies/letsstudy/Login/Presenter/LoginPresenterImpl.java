package com.bcabuddies.letsstudy.Login.Presenter;

import android.text.TextUtils;

import com.bcabuddies.letsstudy.Login.view.LoginView;
import com.google.firebase.auth.FirebaseAuth;

//this class will handle and do all the work related to the data and all
public class LoginPresenterImpl implements LoginPresenter {

    private FirebaseAuth auth;
    private LoginView loginView;

    public LoginPresenterImpl(FirebaseAuth auth) {
        this.auth = auth;
    }

    @Override
    public void login(String email, String password) {
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
            loginView.showValidationError("Email and Password can't be empty");
        } else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            try {
                                loginView.loginError(task.getException().getMessage());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            loginView.loginSuccess();
                        }
                    });
        }
    }

    @Override
    public void checkLogin() {
        if (auth.getCurrentUser() != null)
            loginView.isLogin(true);
        else
            loginView.isLogin(false);
    }

    @Override
    public void attachView(LoginView view) {
        loginView = view;
    }

    @Override
    public void detachView() {
        loginView = null;
    }
}
