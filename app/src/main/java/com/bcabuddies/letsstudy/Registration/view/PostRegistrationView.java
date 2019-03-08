package com.bcabuddies.letsstudy.Registration.view;

import com.bcabuddies.letsstudy.Base.BaseView;

import java.util.ArrayList;

public interface PostRegistrationView extends BaseView {
    void showValidationError();
    void detailUploadSuccess();
    void detailsUploadError(String message);
    void qualiMenu(ArrayList<String> list);
    void pursuingMenu(ArrayList<String> list);
}
