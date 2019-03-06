package com.bcabuddies.letsstudy.Login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcabuddies.letsstudy.R;
import com.bcabuddies.letsstudy.Registration.Registration;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
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
        // TODO: 04-03-2019 Sign in
        Toast.makeText(this, "Login clicked", Toast.LENGTH_LONG).show();
    }

    private void Registration() {
       Intent sharedIntent =new Intent(Login.this,Registration.class);

        Pair[] pairs=new Pair[3];
        pairs[0]=new Pair<View,String>(loginEmailLayout,"email_transition");
        pairs[1]=new Pair<View,String>(loginPassLayout,"password_transition");
        pairs[2]=new Pair<View,String>(loginLoginBtn,"btnregister_transition");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,pairs);

       startActivity(sharedIntent,options.toBundle());
    }

    private void FacebookLogin() {
        // TODO: 04-03-2019 Facebook Login
    }

    private void GoogleLogin() {
        // TODO: 04-03-2019 Google Login
    }

    private void PasswordForgot() {
        // TODO: 04-03-2019 Password Forgot
    }
}
