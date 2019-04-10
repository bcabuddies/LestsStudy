package com.bcabuddies.letsstudy.Registration.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bcabuddies.letsstudy.Home.view.MainActivity;
import com.bcabuddies.letsstudy.Login.view.LoginSplash;
import com.bcabuddies.letsstudy.R;
import com.bcabuddies.letsstudy.Registration.Presenter.PostRegistrationPresenterImpl;
import com.bcabuddies.letsstudy.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class PostRegistration extends AppCompatActivity implements PostRegistrationView {

    @BindView(R.id.button)
    Button button;
    @BindView(R.id.post_reg_nameLayout)
    TextInputLayout postRegNameLayout;
    @BindView(R.id.post_reg_profileView)
    CircleImageView postRegProfileView;
    @BindView(R.id.post_reg_ageLayout)
    TextInputLayout postRegAgeLayout;
    @BindView(R.id.post_reg_ageET)
    TextInputEditText postRegAgeET;
    @BindView(R.id.post_reg_inPursuing)
    TextInputEditText postRegInPursuing;
    @BindView(R.id.post_reg_chageprof)
    ImageView postRegChageprof;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.post_reg_inPursuing_layout)
    TextInputLayout postRegInPursuingLayout;

    private PostRegistrationPresenterImpl presenter;
    private PopupMenu popup;
    private final static String TAG = "PostRegistration.java";
    private String profile;
    private final Calendar myCalendar = Calendar.getInstance();
    private Bitmap thumb_Bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_registration);
        ButterKnife.bind(this);

        FirebaseApp.initializeApp(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        StorageReference thumbImgRef = FirebaseStorage.getInstance().getReference().child("Thumb_images");

        //if name found user is not coming again and not the first time
        try {
            String name = user.getDisplayName();

            if (!name.isEmpty()) {
                Log.e(TAG, "onCreate: found name "+name);
                Intent loginSplashIntent = new Intent(PostRegistration.this, LoginSplash.class);
                loginSplashIntent.putExtra("name", name);
                startActivity(loginSplashIntent);
                finish();
            } else {
                Log.e(TAG, "onCreate: Nothing found in name ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onCreate: name is null exception "+e.getMessage() );
        }

        try {
            progressBar.setVisibility(View.VISIBLE);
            String fName = this.getIntent().getBundleExtra("data").getString("name");
            profile = this.getIntent().getBundleExtra("data").getString("profile");
            preData(fName, profile);
            try {
                String age = this.getIntent().getBundleExtra("data").getString("age");
                String course = this.getIntent().getBundleExtra("data").getString("course");
                preSettingsData(age, course);
                progressBar.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            progressBar.setVisibility(View.GONE);
        }

        if (profile == null) {
            Glide.with(this)
                    .load("https://firebasestorage.googleapis.com/v0/b/letsstudy-c77c3.appspot.com/o/user_defalut_profile%2Fdefault.png?alt=media&token=027c4d7f-8452-4ff9-a4c7-263b77254bd0")
                    .into(postRegProfileView);
        }

        menuInitiated();

        presenter = new PostRegistrationPresenterImpl(firebaseFirestore, user, thumbImgRef);
        presenter.attachView(this);
        presenter.getMenu();
    }

    private void updateLabel() {
        progressBar.setVisibility(View.GONE);
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        postRegAgeLayout.getEditText().setText(sdf.format(myCalendar.getTime()));
    }

    private void preData(String name, String profile) {
        progressBar.setVisibility(View.GONE);
        postRegNameLayout.getEditText().setText(name);
        Glide.with(this).load(profile)
                .into(postRegProfileView);
    }

    private void preSettingsData(String age, String course) {
        progressBar.setVisibility(View.GONE);
        postRegAgeLayout.getEditText().setText(age);
        postRegInPursuing.setText(course);
    }


    private void menuInitiated() {
        Log.e(TAG, "menuInitiated: menu init ");
        //Creating the instance of PopupMenu
        popup = new PopupMenu(PostRegistration.this, postRegInPursuing);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.empty_menu, popup.getMenu());
        pursuingUpdate();
    }

    @Override
    public void showValidationError() {
        progressBar.setVisibility(View.GONE);
        Utils.showMessage(this, "Please write your name");
    }

    @Override
    public void detailUploadSuccess() {
        progressBar.setVisibility(View.GONE);
        Utils.showMessage(this, "Welcome " + postRegNameLayout.getEditText().getText().toString());
        Utils.setIntent(this, MainActivity.class);
        finish();
    }

    @Override
    public void detailsUploadError(String message) {
        progressBar.setVisibility(View.GONE);
        Utils.showMessage(this, "Error : " + message);
    }

    @Override
    public void pursuingMenu(String[] list) {
        //pass data to menu items
        Log.e(TAG, "pursuingMenu: menu data received " + list);
        for (String s : list) {
            popup.getMenu().add(s);
            Log.e(TAG, "pursuingMenu: s " + s);
        }
    }

    @Override
    public void firebasePreData(String name, String profUrl, String courseName, String age) {
        if (!(name == null || profUrl == null || courseName == null || age == null)) {
            Glide.with(this).load(profUrl).into(postRegProfileView);
            postRegNameLayout.getEditText().setText(name);
            postRegAgeLayout.getEditText().setText(age);
            postRegInPursuing.setText(courseName);
        } else {
            Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public Context getContext() {
        return null;
    }

    @OnClick({R.id.post_reg_profileView, R.id.post_reg_ageET, R.id.post_reg_inPursuing, R.id.button, R.id.post_reg_chageprof})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.post_reg_profileView:
                imagePicker();
                break;
            case R.id.post_reg_ageET:
                ageUpdate();
                break;
            case R.id.post_reg_inPursuing:
                Log.e(TAG, "onPostRegInPursuingClicked: menu clicked ");
                popup.show(); //showing popup menu
                break;
            case R.id.button:
                uploadData();
                break;
            case R.id.post_reg_chageprof:
                imagePicker();
                break;
        }
    }

    private void uploadData() {
        progressBar.setVisibility(View.VISIBLE);
        presenter.uploadData(
                postRegNameLayout.getEditText().getText().toString(),
                postRegAgeLayout.getEditText().getText().toString(),
                profile,
                postRegInPursuing.getText().toString()
        );
        progressBar.setVisibility(View.GONE);
    }

    private void imagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);
    }

    private void pursuingUpdate() {
        Log.e(TAG, "pursuingUpdate: " + "pursuing clicked");
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(item -> {
            Toast.makeText(
                    PostRegistration.this,
                    "You Selected : " + item.getTitle(),
                    Toast.LENGTH_SHORT
            ).show();
            postRegInPursuing.setText(item.getTitle());
            return true;
        });
    }

    private void ageUpdate() {
        //need to add Calender to select age
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri mainImageUri = result.getUri();
                Log.v("mkeyreg", "mianuri= " + mainImageUri);
                File thumb_filePathUri = new File(mainImageUri.getPath());
                try {
                    thumb_Bitmap = new Compressor(this).setMaxWidth(400).setMaxHeight(400).setQuality(50).compressToBitmap(thumb_filePathUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_Bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                byte[] thumb_byte = byteArrayOutputStream.toByteArray();
                postRegProfileView.setImageURI(mainImageUri);
                presenter.imagePost(thumb_byte);
            }
        }
    }
}
