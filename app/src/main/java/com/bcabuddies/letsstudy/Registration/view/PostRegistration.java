package com.bcabuddies.letsstudy.Registration.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.bcabuddies.letsstudy.Home.MainActivity;
import com.bcabuddies.letsstudy.R;
import com.bcabuddies.letsstudy.Registration.Presenter.PostRegistrationPresenterImpl;
import com.bcabuddies.letsstudy.utils.Utils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostRegistration extends AppCompatActivity implements PostRegistrationView {

    @BindView(R.id.button)
    Button button;
    @BindView(R.id.post_reg_nameLayout)
    TextInputLayout postRegNameLayout;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private PostRegistrationPresenterImpl presenter;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_registration);
        ButterKnife.bind(this);

        name = getIntent().getStringExtra("name");
        postRegNameLayout.getEditText().setText(name);

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        presenter = new PostRegistrationPresenterImpl(firebaseFirestore, user);
        presenter.attachView(this);
    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        presenter.uploadData(postRegNameLayout.getEditText().getText().toString());
    }

    @Override
    public void showValidationError() {
        Utils.showMessage(this,"Please write your name");
    }

    @Override
    public void detailUploadSuccess() {
        Utils.showMessage(this, "Welcome "+postRegNameLayout.getEditText().getText().toString());
        Utils.setIntent(this, MainActivity.class);
        finish();
    }

    @Override
    public void detailsUploadError(String message) {
        Utils.showMessage(this, "Error : "+message);
    }

    @Override
    public Context getContext() {
        return null;
    }
}
