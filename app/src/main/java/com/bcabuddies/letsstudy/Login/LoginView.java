package com.bcabuddies.letsstudy.Login;

import com.bcabuddies.letsstudy.Login.Base.BaseView;

public interface LoginView extends BaseView {
    void showValidationError(String message);

    void loginSuccess();

    void loginError(String error);

    void isLogin(boolean isLogin);
}
