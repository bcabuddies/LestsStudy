package com.bcabuddies.letsstudy.Post.Presenter;

import com.bcabuddies.letsstudy.Base.BasePresenter;
import com.bcabuddies.letsstudy.Post.view.PostView;

import java.util.HashMap;

public interface PostPresenter extends BasePresenter<PostView> {
    void getPost();
    void postComment(String comment, String user, String postID);
    void getComments();
}
