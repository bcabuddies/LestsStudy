package com.bcabuddies.letsstudy.Home;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.bcabuddies.letsstudy.Login.view.Login;
import com.bcabuddies.letsstudy.R;
import com.bcabuddies.letsstudy.utils.Utils;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.emailTV)
    TextView emailTV;
    @BindView(R.id.signOutBtn)
    Button signOutBtn;
    String TAG = "MainActivity.class";

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        try {
            emailTV.setText(auth.getCurrentUser().getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onCreate: Error " + e);
        }
    }

    @OnClick(R.id.signOutBtn)
    public void onViewClicked() {
        //sign out
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
}
