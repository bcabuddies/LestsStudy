package com.bcabuddies.letsstudy.Registration.Presenter;

import com.bcabuddies.letsstudy.Base.BasePresenter;
import com.bcabuddies.letsstudy.Registration.view.RegisterView;

public interface RegisterPresenter extends BasePresenter<RegisterView> {
    void signUp(String email, String password, String confPass);
}
