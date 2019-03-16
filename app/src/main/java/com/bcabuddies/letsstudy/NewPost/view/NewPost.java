package com.bcabuddies.letsstudy.NewPost.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcabuddies.letsstudy.NewPost.Presenter.NewPostPresenter;
import com.bcabuddies.letsstudy.NewPost.Presenter.NewPostPresenterImpl;
import com.bcabuddies.letsstudy.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewPost extends AppCompatActivity implements NewPostView {

    @BindView(R.id.text_topNav)
    TextView textTopNav;
    @BindView(R.id.newPost_descLayout)
    TextInputLayout newPostDescLayout;
    @BindView(R.id.newPost_uploadImageView)
    ImageView newPostUploadImageView;
    @BindView(R.id.newPost_addImageView)
    ImageView newPostAddImageView;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private static final String TAG = "NewPost.java";
    private NewPostPresenter presenter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.bind(this);

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        presenter = new NewPostPresenterImpl(user, db);
        presenter.attachView(this);

        textTopNav.setText("Add Post");
    }

    @Override
    public Context getContext() {
        return null;
    }

    @OnClick({R.id.newPost_uploadImageView, R.id.newPost_addImageView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.newPost_uploadImageView:
                uploadPost();
                break;
            case R.id.newPost_addImageView:
                addImage();
                break;
        }
    }

    private void addImage() {

    }

    private void uploadPost() {

    }
}
