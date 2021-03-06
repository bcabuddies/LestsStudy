package com.bcabuddies.letsstudy.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.bcabuddies.letsstudy.R;
import com.bcabuddies.letsstudy.Registration.view.PostRegistration;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.widget.PopupMenu;

public class Utils<Data> {
    //this class is for taking all the general use items into 1 place like Toast and Intents
    //you can add more as you like

    public static Intent setIntent(Context context, Class destination) {

        Intent intent = new Intent(context, destination);
        context.startActivity(intent);

        return intent ;
    }

    public static Intent setIntentNoBackLog(Context context, Class destination) {
        Intent intent = new Intent(context, destination);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        return intent ;
    }

    public static Intent setIntentExtra(Context context, Class destination, String key, Bundle data) {
        Intent intent = new Intent(context, destination);
        intent.putExtra(key, data);
        context.startActivity(intent);

        return intent ;
    }

    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
