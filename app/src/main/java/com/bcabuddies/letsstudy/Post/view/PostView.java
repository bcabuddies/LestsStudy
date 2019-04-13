package com.bcabuddies.letsstudy.Post.view;

import com.bcabuddies.letsstudy.Base.BaseView;
import com.bcabuddies.letsstudy.Model.CommentData;

import java.util.ArrayList;
import java.util.HashMap;

public interface PostView extends BaseView {
    void setPost(HashMap<String, Object> dataMap);
    void setLike();
    void setLikeCount(int count);
    void setComments(ArrayList<CommentData> commentDataList);
}
