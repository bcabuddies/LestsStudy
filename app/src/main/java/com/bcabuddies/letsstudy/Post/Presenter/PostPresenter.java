package com.bcabuddies.letsstudy.Post.Presenter;

import com.bcabuddies.letsstudy.Base.BasePresenter;
import com.bcabuddies.letsstudy.Post.view.PostView;

public interface PostPresenter extends BasePresenter<PostView> {
    void getPost();
}
