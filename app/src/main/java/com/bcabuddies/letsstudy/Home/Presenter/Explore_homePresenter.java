package com.bcabuddies.letsstudy.Home.Presenter;

import com.bcabuddies.letsstudy.Base.BasePresenter;
import com.bcabuddies.letsstudy.Home.view.Explore_homeView;

public interface Explore_homePresenter extends BasePresenter<Explore_homeView> {
    void search(String text);
}
