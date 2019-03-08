package com.bcabuddies.letsstudy.Login.view;

import com.bcabuddies.letsstudy.Base.BaseView;

//this interface will pass data to Layout Class i.e., Login.java
public interface LoginView extends BaseView {
    void showValidationError(String message);

    void loginSuccess();

    void loginError(String error);

    void isLogin(boolean isLogin);
}
