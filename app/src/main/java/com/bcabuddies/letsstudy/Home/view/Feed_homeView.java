package com.bcabuddies.letsstudy.Home.view;

import android.os.Bundle;

import com.bcabuddies.letsstudy.Base.BaseView;

public interface Feed_homeView extends BaseView {
    void getData(Bundle b);
    void uploadTextError(String error);
    void uploadSuccess();
}
