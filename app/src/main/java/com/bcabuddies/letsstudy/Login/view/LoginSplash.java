package com.bcabuddies.letsstudy.Login.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bcabuddies.letsstudy.Home.view.MainActivity;
import com.bcabuddies.letsstudy.R;

import androidx.appcompat.app.AppCompatActivity;

public class LoginSplash extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_splash);

        String name = getIntent().getStringExtra("name");

        TextView nameTV = findViewById(R.id.login_splash_tv);
        nameTV.setText("Welcome back\n" + name);

        //show splash screen
        int SPLASH_TIME_OUT = 2000;
        new Handler().postDelayed(() -> {
            Intent welcomeIntent = new Intent(LoginSplash.this, MainActivity.class);
            startActivity(welcomeIntent);
            finish();
        }, SPLASH_TIME_OUT);
    }
}
