package com.bcabuddies.letsstudy.Registration.view;

import com.bcabuddies.letsstudy.Base.BaseView;

public interface PostRegistrationView extends BaseView {
    void showValidationError();
    void detailUploadSuccess();
    void detailsUploadError(String message);
}
