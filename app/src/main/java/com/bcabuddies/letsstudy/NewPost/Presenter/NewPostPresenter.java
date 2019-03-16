package com.bcabuddies.letsstudy.NewPost.Presenter;

import android.os.Bundle;

import com.bcabuddies.letsstudy.Base.BasePresenter;
import com.bcabuddies.letsstudy.NewPost.view.NewPostView;

public interface NewPostPresenter extends BasePresenter<NewPostView> {
    void textPost(Bundle b);
    void imagePost(Bundle b);
}
