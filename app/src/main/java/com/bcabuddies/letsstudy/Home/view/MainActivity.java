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
    TextView homeUserNameText;

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
    private Explore_home exploreHomeFrag;
    private Feed_home feedHomeFrag;
    private Prep_home prepHomeFrag;
    private Test_home testHomeFrag;
    private Notification_home notificationFrag;

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

        initializeFrags();
    }

    private void initializeFrags() {
        exploreHomeFrag = new Explore_home();
        feedHomeFrag = new Feed_home();
        prepHomeFrag = new Prep_home();
        testHomeFrag = new Test_home();
        notificationFrag = new Notification_home();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.home_frameLayout, exploreHomeFrag);
        fragmentTransaction.add(R.id.home_frameLayout, feedHomeFrag);
        fragmentTransaction.add(R.id.home_frameLayout, prepHomeFrag);
        fragmentTransaction.add(R.id.home_frameLayout, testHomeFrag);
        fragmentTransaction.add(R.id.home_frameLayout, notificationFrag);

        fragmentTransaction.hide(exploreHomeFrag);
        fragmentTransaction.hide(prepHomeFrag);
        fragmentTransaction.hide(testHomeFrag);
        fragmentTransaction.hide(notificationFrag);

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
                signOut();
                break;
        }
        return true;
    }

    public void fragmentReplace(Fragment fragment) {
        Log.e(TAG, "fragmentReplace: inside fragment replacer");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (fragment == feedHomeFrag) {
            Log.e(TAG, "fragmentReplace: feed");
            fragmentTransaction.show(feedHomeFrag);
            fragmentTransaction.hide(prepHomeFrag);
            fragmentTransaction.hide(testHomeFrag);
            fragmentTransaction.hide(exploreHomeFrag);
            fragmentTransaction.hide(notificationFrag);
        }
        if (fragment == prepHomeFrag) {
            Log.e(TAG, "fragmentReplace: prep");
            fragmentTransaction.show(prepHomeFrag);
            fragmentTransaction.hide(feedHomeFrag);
            fragmentTransaction.hide(testHomeFrag);
            fragmentTransaction.hide(exploreHomeFrag);
            fragmentTransaction.hide(notificationFrag);
        }
        if (fragment == testHomeFrag) {
            Log.e(TAG, "fragmentReplace: test");
            fragmentTransaction.show(testHomeFrag);
            fragmentTransaction.hide(prepHomeFrag);
            fragmentTransaction.hide(feedHomeFrag);
            fragmentTransaction.hide(exploreHomeFrag);
            fragmentTransaction.hide(notificationFrag);
        }
        if (fragment == exploreHomeFrag) {
            Log.e(TAG, "fragmentReplace: explore");
            fragmentTransaction.show(exploreHomeFrag);
            fragmentTransaction.hide(prepHomeFrag);
            fragmentTransaction.hide(testHomeFrag);
            fragmentTransaction.hide(feedHomeFrag);
            fragmentTransaction.hide(notificationFrag);
        }
        if (fragment == notificationFrag) {
            Log.e(TAG, "fragmentReplace: notification");
            fragmentTransaction.show(notificationFrag);
            fragmentTransaction.hide(prepHomeFrag);
            fragmentTransaction.hide(testHomeFrag);
            fragmentTransaction.hide(exploreHomeFrag);
            fragmentTransaction.hide(feedHomeFrag);
        }
        try {
            fragmentTransaction.show(fragment);
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "fragmentReplace: exception in fragReplace " + e.getMessage());
        }
    }

    @OnClick({R.id.feed_bNav, R.id.prep_bNav, R.id.test_bNav, R.id.explore_bNav, R.id.topMenu_topNav, R.id.noti_topNav})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.feed_bNav:
                fragmentReplace(feedHomeFrag);
                break;
            case R.id.prep_bNav:
                fragmentReplace(prepHomeFrag);
                break;
            case R.id.test_bNav:
                fragmentReplace(testHomeFrag);
                break;
            case R.id.explore_bNav:
                fragmentReplace(exploreHomeFrag);
                break;
            case R.id.noti_topNav:
                Log.e(TAG, "onViewClicked: noti clicked ");
                fragmentReplace(notificationFrag);
                break;
            case R.id.topMenu_topNav:
                homeDrawerLayout.openDrawer(Gravity.LEFT);
                break;
        }
    }
}
