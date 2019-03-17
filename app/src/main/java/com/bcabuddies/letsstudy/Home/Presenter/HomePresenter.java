package com.bcabuddies.letsstudy.Home.Presenter;

import com.bcabuddies.letsstudy.Base.BasePresenter;
import com.bcabuddies.letsstudy.Home.view.HomeView;
import com.google.firebase.auth.FirebaseUser;

public interface HomePresenter extends BasePresenter<HomeView> {
    void user(FirebaseUser user);
     void firebaseDataPre();
}
