package com.bcabuddies.letsstudy.Registration.Presenter;

import android.text.TextUtils;

import com.bcabuddies.letsstudy.Registration.view.RegisterView;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterPresenterImpl implements RegisterPresenter {

    private RegisterView regView;
    private FirebaseAuth auth;

    public RegisterPresenterImpl(FirebaseAuth auth) {
        this.auth = auth;
    }

    @Override
    public void signUp(String email, String password, String confPass) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            regView.showValidationError();
        } else if (password.equals(confPass)) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            regView.signUpSuccess();
                        } else {
                            try {
                                regView.signUpError(task.getException().getMessage());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            regView.signUpError("Password not match");
        }
    }

    @Override
    public void attachView(RegisterView view) {
        regView = view;
    }

    @Override
    public void detachView() {
        regView = null;
    }
}
