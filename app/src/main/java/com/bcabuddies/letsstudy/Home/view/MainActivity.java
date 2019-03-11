package com.bcabuddies.letsstudy.Home.view;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.bcabuddies.letsstudy.Home.Presenter.HomePresenter;
import com.bcabuddies.letsstudy.Home.Presenter.HomePresenterImpl;
import com.bcabuddies.letsstudy.Login.view.Login;
import com.bcabuddies.letsstudy.R;
import com.bcabuddies.letsstudy.utils.Utils;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements HomeView {

    @BindView(R.id.signOutBtn)
    Button signOutBtn;
    String TAG = "MainActivity.class";
   /* @BindView(R.id.home_user_profileView)
    CircleImageView homeUserProfileView;
    @BindView(R.id.home_user_nameText)
    TextView homeUserNameText;*/

    private HomePresenter presenter;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        presenter = new HomePresenterImpl(auth, db);
        presenter.attachView(this);
        presenter.user(user);
    }

    @OnClick(R.id.signOutBtn)
    public void onViewClicked() {
        //sign out
        signout();
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
     /*   String name = user.getString("name");
        String profileURL = user.getString("profile");
        homeUserNameText.setText(name);
        Glide.with(this).load(profileURL).into(homeUserProfileView);
   */
    }

    @Override
    public Context getContext() {
        return null;
    }
}
