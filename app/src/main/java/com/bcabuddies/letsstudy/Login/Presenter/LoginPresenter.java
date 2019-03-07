package com.bcabuddies.letsstudy.Login.Presenter;

import com.bcabuddies.letsstudy.Login.Base.BasePresenter;
import com.bcabuddies.letsstudy.Login.LoginView;

public interface LoginPresenter extends BasePresenter<LoginView> {
    void login(String email, String password);

    void checkLogin();
}
