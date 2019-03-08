package com.bcabuddies.letsstudy.Registration.view;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bcabuddies.letsstudy.Home.MainActivity;
import com.bcabuddies.letsstudy.R;
import com.bcabuddies.letsstudy.Registration.Presenter.PostRegistrationPresenterImpl;
import com.bcabuddies.letsstudy.utils.Utils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostRegistration extends AppCompatActivity implements PostRegistrationView {

    @BindView(R.id.button)
    Button button;
    @BindView(R.id.post_reg_nameLayout)
    TextInputLayout postRegNameLayout;
    @BindView(R.id.post_reg_profileView)
    CircleImageView postRegProfileView;
    @BindView(R.id.post_reg_ageLayout)
    TextInputLayout postRegAgeLayout;
    @BindView(R.id.post_reg_qualification)
    EditText postRegQualification;
    @BindView(R.id.post_reg_inQualification)
    EditText postRegInQualification;
    @BindView(R.id.post_reg_pursuing)
    EditText postRegPursuing;
    @BindView(R.id.post_reg_inPursuing)
    EditText postRegInPursuing;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private PostRegistrationPresenterImpl presenter;
    private PopupMenu popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_registration);
        ButterKnife.bind(this);

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
    public void qualiMenu(ArrayList<String> list) {
        //pass data to menu items
        popup.getMenu().add(list.get(0));
    }

    @Override
    public void pursuingMenu(ArrayList<String> list) {
        //pass data to menu items
        popup.getMenu().add(list.get(0));
    }

    @Override
    public Context getContext() {
        return null;
    }

    @OnClick({R.id.post_reg_profileView, R.id.post_reg_ageLayout, R.id.post_reg_qualification, R.id.post_reg_inQualification, R.id.post_reg_pursuing, R.id.post_reg_inPursuing})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.post_reg_profileView:
                profileUpdate();
                break;
            case R.id.post_reg_ageLayout:
                ageUpdate();
                break;
            case R.id.post_reg_qualification:
                qualificationUpdate();
                break;
            case R.id.post_reg_inQualification:
                detailQualification();
                break;
            case R.id.post_reg_pursuing:
                pursuingUpdate();
                break;
            case R.id.post_reg_inPursuing:
                detailPursuingUpdate();
                break;
        }
    }

    private void detailPursuingUpdate() {

    }

    private void pursuingUpdate() {
        presenter.getMenu("pursuing");
        //for qualification
        //Creating the instance of PopupMenu
        popup = new PopupMenu(PostRegistration.this, postRegInQualification);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.empty_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(item -> {
            Toast.makeText(
                    PostRegistration.this,
                    "You Selected : " + item.getTitle(),
                    Toast.LENGTH_SHORT
            ).show();
            postRegQualification.setText(item.getTitle());
            return true;
        });
        popup.show(); //showing popup menu
    }

    private void detailQualification() {

    }

    private void qualificationUpdate() {
        presenter.getMenu("qualification");
        //for qualification
        //Creating the instance of PopupMenu
        popup = new PopupMenu(PostRegistration.this, postRegInQualification);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.empty_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(item -> {
            Toast.makeText(
                    PostRegistration.this,
                    "You Selected : " + item.getTitle(),
                    Toast.LENGTH_SHORT
            ).show();
            postRegQualification.setText(item.getTitle());
            return true;
        });
        popup.show(); //showing popup menu
    }

    private void ageUpdate() {

    }

    private void profileUpdate() {

    }
}
