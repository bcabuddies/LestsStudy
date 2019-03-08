package com.bcabuddies.letsstudy.Login.view;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcabuddies.letsstudy.Home.MainActivity;
import com.bcabuddies.letsstudy.Login.Presenter.LoginPresenter;
import com.bcabuddies.letsstudy.Login.Presenter.LoginPresenterImpl;
import com.bcabuddies.letsstudy.R;
import com.bcabuddies.letsstudy.Registration.view.Registration;
import com.bcabuddies.letsstudy.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends AppCompatActivity implements LoginView {

    @BindView(R.id.login_email_layout)
    TextInputLayout loginEmailLayout;
    @BindView(R.id.login_pass_layout)
    TextInputLayout loginPassLayout;
    @BindView(R.id.login_forgot_passTV)
    TextView loginForgotPassTV;
    @BindView(R.id.login_google_imageView)
    ImageView loginGoogleImageView;
    @BindView(R.id.login_facebook_imageView)
    ImageView loginFacebookImageView;
    @BindView(R.id.login_registrationTV)
    TextView loginRegistrationTV;
    @BindView(R.id.login_loginBtn)
    Button loginLoginBtn;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;

    //this class will handle the layout

    FirebaseAuth auth;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.detachView();
    }

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        FirebaseApp.initializeApp(this);

        auth = FirebaseAuth.getInstance();

        loginPresenter = new LoginPresenterImpl(auth);
        loginPresenter.attachView(this);
        loginPresenter.checkLogin();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("379376416536-7o8a6ikcmbldqrq3kuan4b1hdv4ueeo4.apps.googleusercontent.com")
                .requestEmail()
                .build();




        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @OnClick({R.id.login_forgot_passTV, R.id.login_google_imageView, R.id.login_facebook_imageView, R.id.login_registrationTV, R.id.login_loginBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_forgot_passTV:
                PasswordForgot();
                break;
            case R.id.login_google_imageView:
                GoogleLogin();
                break;
            case R.id.login_facebook_imageView:
                FacebookLogin();
                break;
            case R.id.login_registrationTV:
                Registration();
                break;
            case R.id.login_loginBtn:
                SignIn();
                break;
        }
    }

    private void SignIn() {
        String email_text = loginEmailLayout.getEditText().getText().toString();
        String password_text = loginPassLayout.getEditText().getText().toString();
        loginPresenter.login(email_text, password_text);
        loginLoginBtn.setEnabled(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                loginPresenter.firebaseAuthWithGoogle(account);
                Log.v("mGoogleSignIn", "Google sign in try");
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.v("mGoogleSignIn", "Google sign in failed", e);
                // ...
            }
        }
    }




    private void Registration() {
        Intent sharedIntent = new Intent(Login.this, Registration.class);

        Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(loginEmailLayout, "email_transition");
        pairs[1] = new Pair<View, String>(loginPassLayout, "password_transition");
        pairs[2] = new Pair<View, String>(loginLoginBtn, "btnregister_transition");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);

        startActivity(sharedIntent, options.toBundle());
    }

    private void FacebookLogin() {
        // TODO: 04-03-2019 Facebook Login
    }

    private void GoogleLogin() {
        googleSignIn();
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void PasswordForgot() {
        // TODO: 04-03-2019 Password Forgot
        loginPassLayout.setVisibility(View.INVISIBLE);
        loginPassLayout.setEnabled(false);
    }

    @Override
    public void showValidationError(String message) {
        Utils.showMessage(this, message);
    }

    @Override
    public void loginSuccess() {
        Utils.showMessage(this, "Login Success!");
        loginLoginBtn.setEnabled(true);
        Utils.setIntent(this, MainActivity.class);
    }

    @Override
    public void loginError(String error) {
        Utils.showMessage(this, "Login error " + error);
    }

    @Override
    public void isLogin(boolean isLogin) {
        if (isLogin) {
            Utils.setIntentNoBackLog(this, MainActivity.class);
            finish();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }
}
