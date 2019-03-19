package com.bcabuddies.letsstudy.NewPost.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcabuddies.letsstudy.Home.view.MainActivity;
import com.bcabuddies.letsstudy.NewPost.Presenter.NewPostPresenter;
import com.bcabuddies.letsstudy.NewPost.Presenter.NewPostPresenterImpl;
import com.bcabuddies.letsstudy.R;
import com.bcabuddies.letsstudy.utils.Utils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;

public class NewPost extends AppCompatActivity implements NewPostView {

    @BindView(R.id.text_topNav)
    TextView textTopNav;
    @BindView(R.id.newPost_descLayout)
    TextInputLayout newPostDescLayout;
    @BindView(R.id.newPost_uploadImageView)
    ImageView newPostUploadImageView;
    @BindView(R.id.newPost_addImageView)
    ImageView newPostAddImageView;

    private static final String TAG = "NewPost.java";
    @BindView(R.id.topMenu_topNav)
    ImageView topMenuTopNav;
    @BindView(R.id.noti_topNav)
    ImageView notiTopNav;
    private NewPostPresenter presenter;
    private Bundle b = new Bundle();
    private Bitmap thumb_bitmap;
    private byte[] thumb_byte;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.bind(this);

        FirebaseApp.initializeApp(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        StorageReference thumbImgRef = FirebaseStorage.getInstance().getReference().child("Post_images");

        presenter = new NewPostPresenterImpl(user, db, thumbImgRef);
        presenter.attachView(this);

        hideTopBarButtons();

        textTopNav.setText("Add Post");
    }

    @SuppressLint("SetTextI18n")
    private void hideTopBarButtons() {
        textTopNav.setText("New Post");
        topMenuTopNav.setVisibility(View.GONE);
        notiTopNav.setVisibility(View.GONE);
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
        Log.e(TAG, "addImage: upload photo clicked ");
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    private void uploadPost() {
        String text = newPostDescLayout.getEditText().getText().toString();
        if (thumb_byte.length == 0 && !TextUtils.isEmpty(text)) {
            Log.e(TAG, "uploadPost: thumb_byte length " + thumb_byte.length);
            Utils.showMessage(this, "Please select a photo and fill the description");
        } else {
            Log.e(TAG, "uploadPost: thumb_byte length " + thumb_byte.length);
            presenter.imagePost(thumb_byte, text);
        }
    }

    @Override
    public void errorUpload(String error) {
        Utils.showMessage(this, error);
    }

    @Override
    public void uploadSuccess() {
        Utils.showMessage(this, "Upload Success");
        Utils.setIntent(this, MainActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: started");
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();
                Log.e(TAG, "onActivityResult: image uri " + imageUri);
                File thumb_filePathUri = new File(imageUri.getPath());
                try {
                    thumb_bitmap = new Compressor(this).setQuality(70)
                            .compressToBitmap(thumb_filePathUri);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onActivityResult: error in compress " + e.getMessage());
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                thumb_byte = byteArrayOutputStream.toByteArray();
                newPostAddImageView.setImageURI(imageUri);
            } else {
                Log.e(TAG, "onActivityResult: resultCode not ok " + requestCode);
            }
        }
    }
}
