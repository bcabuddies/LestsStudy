package com.bcabuddies.letsstudy.Home.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bcabuddies.letsstudy.Adapter.PostRecyclerAdapter;
import com.bcabuddies.letsstudy.Home.Presenter.Feed_homePresenter;
import com.bcabuddies.letsstudy.Home.Presenter.Feed_homePresenterImpl;
import com.bcabuddies.letsstudy.Home.view.Feed_homeView;
import com.bcabuddies.letsstudy.Model.PostData;
import com.bcabuddies.letsstudy.NewPost.view.NewPost;
import com.bcabuddies.letsstudy.R;
import com.bcabuddies.letsstudy.utils.Utils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class Feed_home extends Fragment implements Feed_homeView {


    @BindView(R.id.feed_textLayout)
    TextInputLayout feedTextLayout;
    @BindView(R.id.feed_textSubmitBtn)
    Button feedTextSubmitBtn;
    @BindView(R.id.feed_photoSubmitBtn)
    Button feedPhotoSubmitBtn;
    @BindView(R.id.feed_recyclerView)
    RecyclerView feedRecyclerView;
    @BindView(R.id.home_new_post_view)
    CardView homeNewPostView;

    private final static String TAG = "Feed.java";
    private Feed_homePresenter presenter;
    private FirebaseUser user;
    private ArrayList<PostData> postList;
    private static int firstVisibleInListview;


    public Feed_home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_feed_home, container, false);
        ButterKnife.bind(this, view);

        user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        presenter = new Feed_homePresenterImpl(db);
        presenter.attachView(this);

        recyclerViewInit();
        presenter.getData();

        return view;
    }

    private void recyclerViewInit() {
        Log.e(TAG, "recyclerViewInit: ");
        postList = new ArrayList<>();
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

    private boolean textValidity(String text) {
        return !TextUtils.isEmpty(text);
    }

    private void uploadPhoto() {
        Log.e(TAG, "uploadPhoto: upload photo clicked");
        Utils.setIntent(getContext(), NewPost.class);
    }

    private void uploadText() {
        String text;
        text = Objects.requireNonNull(feedTextLayout.getEditText()).getText().toString();
        if (textValidity(text)) {
            //sending data to presenter
            Bundle b = new Bundle();
            b.putString("text", text);
            b.putString("user", user.getUid());
            b.putString("type", "text");
            presenter.uploadText(b);
        } else {
            feedTextLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            feedTextLayout.setHelperText("Please write before submitting");
        }
        Log.e(TAG, "uploadText: upload text clicked");
    }

    @Override
    public void getData(ArrayList<PostData> pData) {
        //getting user and post data from presenter
        postList = pData;
        Log.e(TAG, "getData: feed_home postlist size " + postList.size());
        PostRecyclerAdapter postRecyclerAdapter = new PostRecyclerAdapter(postList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        feedRecyclerView.setLayoutManager(mLayoutManager);
        feedRecyclerView.setAdapter(postRecyclerAdapter);

        firstVisibleInListview = mLayoutManager.findFirstVisibleItemPosition();

        feedRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy >= 100) {
                    Log.e(TAG, "onScrolled: moving down");
                    //fade
                    homeNewPostView.animate()
                            .translationY(-100)
                            .alpha(0.0f)
                            .setDuration(1000)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    feedTextLayout.setVisibility(View.GONE);
                                    feedPhotoSubmitBtn.setVisibility(View.GONE);
                                    feedTextSubmitBtn.setVisibility(View.GONE);
                                }
                            });
                } else if (dy <= -100) {
                    Log.e(TAG, "onScrolled: moving up");
                    //show
                    homeNewPostView.animate()
                            .translationY(0)
                            .alpha(1)
                            .setDuration(1000)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    feedTextLayout.setVisibility(View.VISIBLE);
                                    feedPhotoSubmitBtn.setVisibility(View.VISIBLE);
                                    feedTextSubmitBtn.setVisibility(View.VISIBLE);
                                }
                            });
                }
            }
        });

        //postRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void uploadTextError(String error) {
        //error uploading data
        Utils.showMessage(getContext(), error);
    }

    @Override
    public void uploadSuccess() {
        //no error uploading data
        Objects.requireNonNull(feedTextLayout.getEditText()).setText(null);
        feedTextLayout.getEditText().clearFocus();
    }

    public static Fragment newInstance() {
        return new Feed_home();
    }
}
