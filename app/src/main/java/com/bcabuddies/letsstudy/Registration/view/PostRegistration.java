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
import android.widget.Toast;

import com.bcabuddies.letsstudy.Home.view.MainActivity;
import com.bcabuddies.letsstudy.R;
import com.bcabuddies.letsstudy.Registration.Presenter.PostRegistrationPresenterImpl;
import com.bcabuddies.letsstudy.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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

    private FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private PostRegistrationPresenterImpl presenter;
    private PopupMenu popup;
    private final static String TAG = "PostRegistration.java";
    private String profile, fName;
    private final Calendar myCalendar = Calendar.getInstance();
    private Bitmap thumb_Bitmap = null;
    private Uri mainImageUri = null;
    private StorageReference thumbImgRef;
    public Uri thumb_downloadUrl = null;
    private Task<Uri> getDownloadUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_registration);
        ButterKnife.bind(this);

        try {
            fName = this.getIntent().getBundleExtra("data").getString("name");
            profile = this.getIntent().getBundleExtra("data").getString("profile");
            preData(fName, profile);
            try {
                String age= this.getIntent().getBundleExtra("data").getString("age");
                String course=this.getIntent().getBundleExtra("data").getString("course");
                preSettingsData(age,course);

            } catch (Exception e) {
                e.printStackTrace();
            }
            //  b = this.getIntent().getBundleExtra("data");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (profile == null) {
            Glide.with(this)
                    .load("https://firebasestorage.googleapis.com/v0/b/letsstudy-c77c3.appspot.com/o/user_defalut_profile%2Fdefault.png?alt=media&token=027c4d7f-8452-4ff9-a4c7-263b77254bd0")
                    .into(postRegProfileView);
        }

        menuInitiated();

     /*   try {
           // String name =this.getIntent().getBundleExtra("data").getString("name");
           // profile = b.getString("profile");


        } catch (Exception e) {
            e.printStackTrace();
        }*/

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        thumbImgRef = FirebaseStorage.getInstance().getReference().child("Thumb_images");
        presenter = new PostRegistrationPresenterImpl(firebaseFirestore, user);
        presenter.attachView(this);
        presenter.getMenu();
        //presenter.firebaseDataPre();

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        postRegAgeLayout.getEditText().setText(sdf.format(myCalendar.getTime()));
    }

    private void preData(String name, String profile) {
        postRegNameLayout.getEditText().setText(name);
        Glide.with(this).load(profile)
                .into(postRegProfileView);
    }

    private void preSettingsData(String age,String course) {
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
        Utils.showMessage(this, "Please write your name");
    }

    @Override
    public void detailUploadSuccess() {
        Utils.showMessage(this, "Welcome " + postRegNameLayout.getEditText().getText().toString());
        Utils.setIntent(this, MainActivity.class);
        finish();
    }

    @Override
    public void detailsUploadError(String message) {
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
                presenter.uploadData(
                        postRegNameLayout.getEditText().getText().toString(),
                        postRegAgeLayout.getEditText().getText().toString(),
                        profile,
                        postRegInPursuing.getText().toString(),
                        thumb_downloadUrl
                );
                break;
            case R.id.post_reg_chageprof:
                imagePicker();
                break;
        }
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
                mainImageUri = result.getUri();
                Log.v("mkeyreg", "mianuri= " + mainImageUri);
                File thumb_filePathUri = new File(mainImageUri.getPath());
                try {
                    thumb_Bitmap = new Compressor(this).setMaxWidth(400).setMaxHeight(400).setQuality(50).compressToBitmap(thumb_filePathUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_Bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                final byte[] thumb_byte = byteArrayOutputStream.toByteArray();
                final StorageReference thumb_filePath = thumbImgRef.child(auth.getCurrentUser().getUid() + ".jpg");
                thumb_filePath.putBytes(thumb_byte).addOnSuccessListener(taskSnapshot -> {
                    getDownloadUri = taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> {
                        thumb_downloadUrl = uri;
                        Log.v("mkey", "thumb download url: " + thumb_downloadUrl);
                    });
                    postRegProfileView.setImageURI(mainImageUri);
                });
            }
        }
    }
}
