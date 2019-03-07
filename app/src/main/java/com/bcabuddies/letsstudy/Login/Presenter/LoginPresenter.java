package com.bcabuddies.letsstudy.Login.Presenter;

import com.bcabuddies.letsstudy.Login.Model.User;

public class LoginPresenter implements Login_presenter_interface {

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
    }

    LoginView loginView;

    @Override
    public void onLogin(String email, String password) {
        User user = new User(email, password);
        boolean isLoginSuccess = user.isValid();

        if (isLoginSuccess)
            loginView.onLoginResult("Login Success");
        else
            loginView.onLoginResult("Login error");
    }
}
