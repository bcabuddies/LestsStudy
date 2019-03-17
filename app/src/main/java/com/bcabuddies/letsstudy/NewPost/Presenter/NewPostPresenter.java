package com.bcabuddies.letsstudy.NewPost.Presenter;

import com.bcabuddies.letsstudy.Base.BasePresenter;
import com.bcabuddies.letsstudy.NewPost.view.NewPostView;

public interface NewPostPresenter extends BasePresenter<NewPostView> {
    void imagePost(byte[] thumb_byte, String text);
}
