package com.bcabuddies.letsstudy.Home.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcabuddies.letsstudy.Home.Presenter.HomePresenter;
import com.bcabuddies.letsstudy.Home.Presenter.HomePresenterImpl;
import com.bcabuddies.letsstudy.Home.fragments.Explore_home;
import com.bcabuddies.letsstudy.Home.fragments.Feed_home;
import com.bcabuddies.letsstudy.Home.fragments.Notification_home;
import com.bcabuddies.letsstudy.Home.fragments.Prep_home;
import com.bcabuddies.letsstudy.Home.fragments.Test_home;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements HomeView, NavigationView.OnNavigationItemSelectedListener {


    String TAG = "MainActivity.class";

    CircleImageView homeUserProfileView;
    TextView homeUserNameText, homeUserStudyPointText;

    @BindView(R.id.home_nav)
    NavigationView homeNav;
    @BindView(R.id.home_drawer_layout)
    DrawerLayout homeDrawerLayout;
    @BindView(R.id.topMenu_topNav)
    ImageView topMenuTopNav;
    @BindView(R.id.text_topNav)
    TextView textTopNav;
    @BindView(R.id.noti_topNav)
    ImageView notiTopNav;
    @BindView(R.id.feed_bNav)
    ImageView feedBNav;
    @BindView(R.id.prep_bNav)
    ImageView prepBNav;
    @BindView(R.id.test_bNav)
    ImageView testBNav;
    @BindView(R.id.explore_bNav)
    ImageView exploreBNav;
    @BindView(R.id.home_frameLayout)
    FrameLayout homeFrameLayout;

    private HomePresenter presenter;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private Unbinder bind;
    //fragments
    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        homeNav.setNavigationItemSelectedListener(this);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        //check user login
        checkLogin(user);

        presenter = new HomePresenterImpl(auth, db);
        presenter.attachView(this);
        presenter.user(user);

        fragment = Feed_home.newInstance();
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void signOut() {
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
        homeUserStudyPointText = findViewById(R.id.home_user_studyPointText);

        String name = user.getString("name");
        String profileURL = user.getString("profile");
        long points = user.getLong("points");

        homeUserNameText.setText(name);
        homeUserStudyPointText.setText(""+points);
        Glide.with(this).load(profileURL).into(homeUserProfileView);
    }

    @Override
    public void firebaseData(String profUrl, String fName, String age, String course, long points) {
        Bundle data = new Bundle();
        Log.e(TAG, "thirdPartyLoginSuccess: name and profile " + fName + " " + profUrl);
        data.putString("name", fName);
        data.putString("profile", profUrl);
        data.putString("age", age);
        data.putString("course", course);
        data.putLong("points", points);
        Utils.setIntentExtra(this, PostRegistration.class, "data", data);
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
                presenter.firebaseDataPre();
                break;
            case R.id.homenav_logout:
                signOut();
                break;
        }
        return true;
    }

    @OnClick({R.id.feed_bNav, R.id.prep_bNav, R.id.test_bNav, R.id.explore_bNav, R.id.topMenu_topNav, R.id.noti_topNav})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.feed_bNav:
                fragment = Feed_home.newInstance();
                break;
            case R.id.prep_bNav:
                fragment = Prep_home.newInstance();
                break;
            case R.id.test_bNav:
                fragment = Test_home.newInstance();
                break;
            case R.id.explore_bNav:
                fragment = Explore_home.newInstance();
                break;
            case R.id.noti_topNav:
                Log.e(TAG, "onViewClicked: noti clicked ");
                fragment = Notification_home.newInstance();
                break;
            case R.id.topMenu_topNav:
                homeDrawerLayout.openDrawer(Gravity.LEFT);
                break;
        }
        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction2.replace(R.id.home_frameLayout, fragment);
        fragmentTransaction2.commit();
    }

    void checkLogin(FirebaseUser user){
        if (user == null){
            Utils.setIntentNoBackLog(this, Login.class);
            finish();
        }
    }
}
