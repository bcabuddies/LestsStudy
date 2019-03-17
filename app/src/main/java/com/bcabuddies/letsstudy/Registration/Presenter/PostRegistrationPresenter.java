package com.bcabuddies.letsstudy.Registration.Presenter;

import android.net.Uri;

import com.bcabuddies.letsstudy.Base.BasePresenter;
import com.bcabuddies.letsstudy.Registration.view.PostRegistrationView;
import com.google.android.gms.tasks.Task;

public interface PostRegistrationPresenter extends BasePresenter<PostRegistrationView> {
    void uploadData(String name, String age, String profileUri, String pursuing, Uri thumb_downloadUrl);

    void getMenu();
     void firebaseDataPre();
}
