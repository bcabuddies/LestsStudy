package com.bcabuddies.letsstudy.Home.view;

import android.os.Bundle;

import com.bcabuddies.letsstudy.Base.BaseView;
import com.bcabuddies.letsstudy.Model.PostData;
import com.bcabuddies.letsstudy.Model.UserData;

import java.util.ArrayList;

public interface Feed_homeView extends BaseView {
    void getData(ArrayList<PostData> postData, ArrayList<UserData> userData);
    void uploadTextError(String error);
    void uploadSuccess();
}
