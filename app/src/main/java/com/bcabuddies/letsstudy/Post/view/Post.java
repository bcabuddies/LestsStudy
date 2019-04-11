package com.bcabuddies.letsstudy.Post.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcabuddies.letsstudy.Adapter.CommentRecyclerAdapter;
import com.bcabuddies.letsstudy.Model.CommentData;
import com.bcabuddies.letsstudy.Post.Presenter.PostPresenterImpl;
import com.bcabuddies.letsstudy.R;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Post extends AppCompatActivity implements PostView {

    @BindView(R.id.homerow_prof)
    CircleImageView homerowProf;
    @BindView(R.id.homerow_nameTV)
    TextView homerowNameTV;
    @BindView(R.id.homerow_menu)
    ImageView homerowMenu;
    @BindView(R.id.homerow_dateTV)
    TextView homerowDateTV;
    @BindView(R.id.homerow_descTV)
    TextView homerowDescTV;
    @BindView(R.id.homerow_imageview)
    ImageView homerowImageView;
    @BindView(R.id.homerow_likecard)
    CardView homerowLikeCard;
    @BindView(R.id.homerow_cmntcard)
    CardView homerowCmntcard;
    @BindView(R.id.comment_editText)
    TextInputEditText commentEditText;
    @BindView(R.id.comment_Layout)
    TextInputLayout commentLayout;
    @BindView(R.id.comment_post_btn)
    ImageView commentPostBtn;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private final static String TAG = "Post.java";
    private PostPresenterImpl presenter;
    private ArrayList<CommentData> commentList;
    private CommentRecyclerAdapter adapter;
    private String postID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);

        homerowCmntcard.setVisibility(View.GONE);
        homerowLikeCard.setVisibility(View.GONE);
        homerowMenu.setVisibility(View.GONE);

        try {
            postID = getIntent().getStringExtra("postID");
            Log.e(TAG, "onCreate: postID " + postID);

            FirebaseApp.initializeApp(this);
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            presenter = new PostPresenterImpl(postID, firestore);
            presenter.attachView(this);
            presenter.getPost();
            presenter.getComments();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onCreate: Exception " + e.getMessage());
            finish();
        }

        commentList = new ArrayList<>();

        commentPostBtn.setOnClickListener(v -> {
            String comment = commentLayout.getEditText().getText().toString();
            String user;
            FirebaseAuth auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser().getUid();
            if (!comment.isEmpty()) {
                presenter.postComment(comment, user, postID);
                commentEditText.setText(null);
                commentEditText.clearFocus();
            } else {
                Toast.makeText(this, "Please write some comment", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setPost(HashMap<String, Object> dataMap) {
        Log.e(TAG, "setPost: data received " + dataMap);
        if (!dataMap.isEmpty()) {
            String photoUrl = String.valueOf(dataMap.get("photo"));

            if (photoUrl.contains("no photo"))
                homerowImageView.setVisibility(View.GONE);
            else {
                Glide.with(Post.this).load(photoUrl).into(homerowImageView);
            }

            String name = String.valueOf(dataMap.get("name"));
            Date timestamp = (Date) dataMap.get("date");
            String desc = String.valueOf(dataMap.get("desc"));
            String profile = String.valueOf(dataMap.get("profile"));

            Glide.with(Post.this).load(profile).into(homerowProf);
            homerowNameTV.setText(name);
            homerowDescTV.setText(desc);

            try {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatMMDDYYYY = new SimpleDateFormat("dd MMM yy \t hh:mm a");
                final StringBuilder nowMMDDYYYY = new StringBuilder(dateformatMMDDYYYY.format(timestamp));
                homerowDateTV.setText(nowMMDDYYYY);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "onBindViewHolder: date exception " + e.getMessage());
            }
        } else {
            finish();
            Log.e(TAG, "setPost: no data in hashMap");
        }
    }

    @Override
    public void setComments(ArrayList<CommentData> commentDataList) {
        //send data to Adapter
        commentList = commentDataList;
        adapter = new CommentRecyclerAdapter(commentList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
