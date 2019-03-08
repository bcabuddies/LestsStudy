package com.bcabuddies.letsstudy.Registration.view;

import com.bcabuddies.letsstudy.Base.BaseView;

public interface RegisterView extends BaseView {
    void showValidationError();
    void signUpSuccess();
    void signUpError(String message);
}
