package com.bcabuddies.letsstudy.Login.Presenter;

import android.text.TextUtils;
import android.util.Log;

import com.bcabuddies.letsstudy.Login.view.LoginView;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import androidx.annotation.NonNull;

//this class will handle and do all the work related to the data and all
public class LoginPresenterImpl implements LoginPresenter {

    private FirebaseAuth auth;
    private LoginView loginView;


    public LoginPresenterImpl(FirebaseAuth auth) {
        this.auth = auth;
    }

    @Override
    public void login(String email, String password) {
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
            loginView.showValidationError("Email and Password can't be empty");
        } else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            try {
                                loginView.loginError(task.getException().getMessage());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            loginView.loginSuccess();
                        }
                    });
        }
    }


    //google signIn
    public void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        Log.v("mGoogleSignIn", "firebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    try {
                        loginView.loginError(task.getException().getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    loginView.thirdPartyLoginSuccess();
                }
            }
        });


    }


    //facebook signIn
    public void handleFacebookAccessToken(AccessToken token) {
        Log.v("facebooktest", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    try {
                        Log.e("facebooktest", "error: " + task.getException().getMessage());
                        LoginManager.getInstance().logOut();
                        loginView.loginError(task.getException().getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("facebooktest", "onComplete: " + e.getMessage());
                    }
                } else {
                    loginView.thirdPartyLoginSuccess();
                    Log.e("facebooktest", "onComplete: success ");
                }
            }
        });
    }


    @Override
    public void checkLogin() {
        if (auth.getCurrentUser() != null)
            loginView.isLogin(true);
        else
            loginView.isLogin(false);
    }

    @Override
    public void attachView(LoginView view) {
        loginView = view;
    }

    @Override
    public void detachView() {
        loginView = null;
    }
}
