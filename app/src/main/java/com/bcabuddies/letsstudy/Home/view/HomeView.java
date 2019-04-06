package com.bcabuddies.letsstudy.Home.view;

import android.os.Bundle;

import com.bcabuddies.letsstudy.Base.BaseView;

public interface HomeView extends BaseView {
    void showValidationError(String message);
    void getUserDetails(Bundle user);
    void firebaseData(String profUrl, String name, String age, String course, long points);
}
