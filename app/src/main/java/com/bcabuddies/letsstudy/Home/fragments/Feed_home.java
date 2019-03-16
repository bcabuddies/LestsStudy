package com.bcabuddies.letsstudy.Home.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bcabuddies.letsstudy.NewPost.view.NewPost;
import com.bcabuddies.letsstudy.R;
import com.bcabuddies.letsstudy.utils.Utils;
import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class Feed_home extends Fragment {


    @BindView(R.id.feed_textLayout)
    TextInputLayout feedTextLayout;
    @BindView(R.id.feed_textSubmitBtn)
    Button feedTextSubmitBtn;
    @BindView(R.id.feed_photoSubmitBtn)
    Button feedPhotoSubmitBtn;
    @BindView(R.id.feed_recyclerView)
    RecyclerView feedRecyclerView;

    public Feed_home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_home, container, false);
    }

    @OnClick({R.id.feed_textSubmitBtn, R.id.feed_photoSubmitBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.feed_textSubmitBtn:
                uploadText();
                break;
            case R.id.feed_photoSubmitBtn:
                uploadPhoto();
                break;
        }
    }

    private void uploadPhoto() {
        Utils.setIntent(getContext(), NewPost.class);
    }

    private void uploadText() {

    }
}
