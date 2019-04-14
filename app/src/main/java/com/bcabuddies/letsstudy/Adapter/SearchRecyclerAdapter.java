package com.bcabuddies.letsstudy.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcabuddies.letsstudy.Model.PostData;
import com.bcabuddies.letsstudy.Post.view.Post;
import com.bcabuddies.letsstudy.R;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> {

    private ArrayList<PostData> postList;
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    private FirebaseFirestore firebaseFirestore;
    private static final String TAG = "SearchRecycler.java";
    private String userName;

    public SearchRecyclerAdapter(ArrayList<PostData> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row, parent, false);

        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get postID
        final String postID = postList.get(position).PostID;

        //get data
        final String text = postList.get(position).getText();
        final Date timestamp = postList.get(position).getTimestamp();
        final String postUserId = postList.get(position).getUser();
        final String url = postList.get(position).getUrl();

        if (postList.get(position).getUrl() != null) {
            Log.e(TAG, "onBindViewHolder: url " + position);
            holder.setPostImageView(url);
        }

        Log.e(TAG, "onBindViewHolder: list size "+postList.size() );

        //user data
        firebaseFirestore.collection("Users").document(postUserId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userName = Objects.requireNonNull(task.getResult()).getString("name");
                String profUrl = task.getResult().getString("profileURL");
                holder.setProf(profUrl);
                setNameAndText(text, userName, holder);
            } else {
                Log.e(TAG, "onComplete: error");
            }
        });

        //to open post
        holder.cardView.setOnClickListener(v -> openPost(postID));
    }

    private void setNameAndText(String text, String userName, ViewHolder holder) {
        String itemText = "<b>" + userName + "</b> " + text;
        holder.textView.setText((Html.fromHtml(itemText)));

    }

    private void openPost(String postID) {
        Intent openPost = new Intent(context, Post.class);
        openPost.putExtra("postID", postID);
        context.startActivity(openPost);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private CircleImageView profileView;
        private TextView textView;
        private ImageView imageView;
        private CardView cardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            profileView = view.findViewById(R.id.search_row_profileView);
            textView = view.findViewById(R.id.search_row_textView);
            imageView = view.findViewById(R.id.search_row_imageView);
            cardView = view.findViewById(R.id.search_row_cardView);
        }

        void setPostImageView(String url) {
            imageView.setVisibility(View.VISIBLE);
            Glide.with(context).load(url).into(imageView);
        }

        void setProf(String profUrl) {
            Glide.with(context).load(profUrl).into(profileView);
        }
    }
}
