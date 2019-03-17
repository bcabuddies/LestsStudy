package com.bcabuddies.letsstudy.NewPost.view;

import com.bcabuddies.letsstudy.Base.BaseView;

public interface NewPostView extends BaseView {
    void errorUpload(String error);
    void uploadSuccess();
}
