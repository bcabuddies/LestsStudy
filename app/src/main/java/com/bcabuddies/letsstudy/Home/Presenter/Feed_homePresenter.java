package com.bcabuddies.letsstudy.Home.Presenter;

import android.os.Bundle;

import com.bcabuddies.letsstudy.Base.BasePresenter;
import com.bcabuddies.letsstudy.Home.view.Feed_homeView;

public interface Feed_homePresenter extends BasePresenter<Feed_homeView> {
    void uploadText(Bundle text);
    void getData();
}
