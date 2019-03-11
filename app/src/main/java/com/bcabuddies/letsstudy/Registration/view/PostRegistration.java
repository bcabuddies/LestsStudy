package com.bcabuddies.letsstudy.Registration.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bcabuddies.letsstudy.Home.view.MainActivity;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
    @BindView(R.id.post_reg_inPursuing)
    TextView postRegInPursuing;
    @BindView(R.id.post_reg_ageET)
    TextInputEditText postRegAgeET;

    private FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private PostRegistrationPresenterImpl presenter;
    private PopupMenu popup;
    private Bundle b;
    private final static String TAG = "PostRegistration.java";
    private String profile;
    private final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_registration);
        ButterKnife.bind(this);

        try {
            b = this.getIntent().getBundleExtra("data");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onCreate: " + b.getString("name") + b.getString("profile"));
        }

        menuInitiated();

        try {
            String name = b.getString("name");
            profile = b.getString("profile");
            preData(name, profile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        presenter = new PostRegistrationPresenterImpl(firebaseFirestore, user);
        presenter.attachView(this);
        presenter.getMenu();

        presenter.firebaseDataPre();

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
    public void firebasePreData(String name, String profUrl, String courseName) {

        if (!(name.equals(null) || profUrl.equals(null) || courseName.equals(null))) {
            preData(name, profUrl);
        } else {
            Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public Context getContext() {
        return null;
    }

    @OnClick({R.id.post_reg_profileView, R.id.post_reg_ageET, R.id.post_reg_inPursuing, R.id.button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.post_reg_profileView:
                profileUpdate();
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
                        postRegInPursuing.getText().toString()
                );
                break;
        }
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

    private void profileUpdate() {
        //need to create option to select or click image
    }
}
