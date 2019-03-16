package com.bcabuddies.letsstudy.Home.view;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcabuddies.letsstudy.Home.Presenter.HomePresenter;
import com.bcabuddies.letsstudy.Home.Presenter.HomePresenterImpl;
import com.bcabuddies.letsstudy.Login.view.Login;
import com.bcabuddies.letsstudy.R;
import com.bcabuddies.letsstudy.Registration.view.PostRegistration;
import com.bcabuddies.letsstudy.utils.Utils;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements HomeView, NavigationView.OnNavigationItemSelectedListener {


    String TAG = "MainActivity.class";

    CircleImageView homeUserProfileView;
    TextView homeUserNameText;
    ImageView topMenu;

    @BindView(R.id.home_nav)
    NavigationView homeNav;
    @BindView(R.id.home_drawer_layout)
    DrawerLayout homeDrawerLayout;

    private HomePresenter presenter;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        homeNav.setNavigationItemSelectedListener(this);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        presenter = new HomePresenterImpl(auth, db);
        presenter.attachView(this);
        presenter.user(user);

        topBar();
    }

    private void topBar() {
        topMenu = findViewById(R.id.topMenu_topNav);
        topMenu.setOnClickListener(v -> {
            homeDrawerLayout.openDrawer(Gravity.LEFT);
        });
    }

    private void signout() {
        auth.signOut();
        try {
            LoginManager.getInstance().logOut();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Utils.showMessage(this, "Sign out");
        Utils.setIntent(this, Login.class);

        finish();
    }

    @Override
    public void showValidationError(String message) {
        Utils.showMessage(this, "Error");
    }

    @Override
    public void getUserDetails(Bundle user) {
        //User data receive here
        homeUserProfileView = findViewById(R.id.home_user_profileView);
        homeUserNameText = findViewById(R.id.home_user_nameText);
        String name = user.getString("name");
        String profileURL = user.getString("profile");
        homeUserNameText.setText(name);
        Glide.with(this).load(profileURL).into(homeUserProfileView);
    }


    @Override
    public Context getContext() {
        return null;
    }

    //side navigation menu clicked
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.homenav_settings:
                Utils.setIntent(this, PostRegistration.class);
                break;
            case R.id.homenav_logout:
                signout();
                break;
        }
        return true;
    }


}
