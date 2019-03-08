package com.bcabuddies.letsstudy.Login.Presenter;

import com.bcabuddies.letsstudy.Base.BasePresenter;
import com.bcabuddies.letsstudy.Login.view.LoginView;

//this is a interface to pass the data to the class which implements it
public interface LoginPresenter extends BasePresenter<LoginView> {
    void login(String email, String password);

    void checkLogin();
}
