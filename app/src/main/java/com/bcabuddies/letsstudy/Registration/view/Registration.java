package com.bcabuddies.letsstudy.Registration.view;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import com.bcabuddies.letsstudy.Home.MainActivity;
import com.bcabuddies.letsstudy.R;
import com.bcabuddies.letsstudy.Registration.Presenter.RegisterPresenterImpl;
import com.bcabuddies.letsstudy.utils.Utils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Registration extends AppCompatActivity implements RegisterView {

    @BindView(R.id.register_name_layout)
    TextInputLayout registerNameLayout;
    @BindView(R.id.register_email_layout)
    TextInputLayout registerEmailLayout;
    @BindView(R.id.register_pass_layout)
    TextInputLayout registerPassLayout;
    @BindView(R.id.register_cpass_layout)
    TextInputLayout registerCpassLayout;
    @BindView(R.id.register_registerBtn)

    Button registerRegisterBtn;
    private FirebaseAuth auth;
    private RegisterPresenterImpl presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        presenter = new RegisterPresenterImpl(auth);
        presenter.attachView(this);
    }

    @OnClick(R.id.register_registerBtn)
    public void onViewClicked() {
        presenter.signUp(
                registerEmailLayout.getEditText().getText().toString().trim().toLowerCase(),
                registerPassLayout.getEditText().getText().toString(),
                registerCpassLayout.getEditText().getText().toString());
    }

    @Override
    public void showValidationError() {
        Utils.showMessage(this,"Please fill the details carefully");
    }

    @Override
    public void signUpSuccess() {
        Utils.showMessage(this, "Welcome");
        Utils.setIntentExtra(this, PostRegistration.class, "name", registerNameLayout.getEditText().getText().length());
    }

    @Override
    public void signUpError(String message) {
        Utils.showMessage(this, "sign up error : "+message);
    }

    @Override
    public Context getContext() {
        return null;
    }
}
