package com.bcabuddies.letsstudy.Registration.Presenter;

import com.bcabuddies.letsstudy.Base.BasePresenter;
import com.bcabuddies.letsstudy.Registration.view.PostRegistrationView;

public interface PostRegistrationPresenter extends BasePresenter<PostRegistrationView> {
    void uploadData(String name);

    void getMenu();
}
