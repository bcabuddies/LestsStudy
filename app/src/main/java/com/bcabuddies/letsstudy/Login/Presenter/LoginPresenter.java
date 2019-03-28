package com.bcabuddies.letsstudy.Login.Presenter;

import com.bcabuddies.letsstudy.Base.BasePresenter;
import com.bcabuddies.letsstudy.Login.view.LoginView;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

//this is a interface to pass the data to the class which implements it
public interface LoginPresenter extends BasePresenter<LoginView> {
    void login(String email, String password);

    void firebaseAuthWithGoogle(GoogleSignInAccount account);

    void handleFacebookAccessToken(AccessToken token);

    void checkLogin();
    void pointsCheck();
}
