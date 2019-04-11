package com.bcabuddies.letsstudy.Home.view;

import com.bcabuddies.letsstudy.Base.BaseView;
import com.bcabuddies.letsstudy.Model.PostData;

import java.util.ArrayList;

public interface Feed_homeView extends BaseView {
    void getData(ArrayList<PostData> postData);

    void uploadTextError(String error);

    void uploadSuccess();
}
