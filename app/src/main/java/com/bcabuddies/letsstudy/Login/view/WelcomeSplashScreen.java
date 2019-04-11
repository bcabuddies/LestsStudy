package com.bcabuddies.letsstudy.Login.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.bcabuddies.letsstudy.Home.view.MainActivity;
import com.bcabuddies.letsstudy.R;

public class WelcomeSplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome_splash_screen);

        //show splash screen
        int SPLASH_TIME_OUT = 1000;
        new Handler().postDelayed(() -> {
            Intent welcomeIntent = new Intent(WelcomeSplashScreen.this, Login.class);
            startActivity(welcomeIntent);
            finish();
        }, SPLASH_TIME_OUT);
    }
}