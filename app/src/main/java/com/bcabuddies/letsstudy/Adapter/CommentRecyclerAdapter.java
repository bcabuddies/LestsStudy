package com.bcabuddies.letsstudy.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcabuddies.letsstudy.Model.CommentData;
import com.bcabuddies.letsstudy.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder> {

    private ArrayList<CommentData> cmntList;

    private static final String TAG = "CommentAdapter.java";
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser user;
    private boolean postLike = false;

    public CommentRecyclerAdapter(ArrayList<CommentData> cmntList) {
        this.cmntList = cmntList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int resource = R.layout.comment_row;
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        context = parent.getContext();

        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get comment post id
        final String commentID = cmntList.get(position).CommentID;

        //get current user
        final String current_user = user.getUid();

        //getting comment data
        final String text = cmntList.get(position).getText();
        final String uid = cmntList.get(position).getUid();
        final Date date = cmntList.get(position).getTimestamp();
        final String postID = cmntList.get(position).getPostID();

        Log.e(TAG, "onBindViewHolder: list size " + cmntList.size());

        getUserDataAndShowComment(text, uid, date, holder);

        checkOwner(user, uid, holder);

        holder.deleteBtn.setOnClickListener(v -> {
            holder.deleteBtn.setEnabled(false);
            deleteComment(commentID, postID, holder);
            notifyDataSetChanged();
        });

        //check commentLike
        checkLike(postID, current_user, holder, commentID);

        //check commentLike count
        likeCount(postID, holder, commentID);

        holder.likeBtn.setOnClickListener(v -> {
            holder.likeBtn.setImageResource(R.drawable.like_selected);
            holder.likeBtn.setImageTintList(context.getResources().getColorStateList(R.color.like_pink));
            likeFeature(holder, postID, current_user, commentID);
        });
    }

    private void checkLike(String postID, String current_user, ViewHolder holder, String commentID) {
        firebaseFirestore.collection("Posts").document(postID).collection("Comments")
                .document(commentID).collection("Likes").document(current_user).get().addOnCompleteListener(task -> {
            if (Objects.requireNonNull(task.getResult()).exists()) {
                postLike = true;
                holder.likeBtn.setImageResource(R.drawable.like_selected);
                holder.likeBtn.setImageTintList(context.getResources().getColorStateList(R.color.like_pink));
                Log.e(TAG, "checkLike: comment is already liked by user " + commentID);
                Log.e(TAG, "checkLike: commentLike " + postLike + " comment id " + commentID);
            } else {
                postLike = false;
                Log.e(TAG, "checkLike: commentLike in else " + postLike + " comment id " + commentID);
            }
        });
    }

    private void likeFeature(ViewHolder holder, String postID, String current_user, String commentID) {
        //check if already liked
        firebaseFirestore.collection("Posts").document(postID).collection("Comments").document(commentID)
                .collection("Likes").document(current_user).get().addOnCompleteListener(task -> {
            if (Objects.requireNonNull(task.getResult()).exists()) {
                postLike = true;
                holder.likeBtn.setImageResource(R.drawable.like_selected);
                holder.likeBtn.setImageTintList(context.getResources().getColorStateList(R.color.like_pink));
                Log.e(TAG, "checkLike: comment is already liked by user " + commentID);
                Log.e(TAG, "checkLike: commentLike " + postLike + " comment id " + commentID);
                dislike(current_user, postID, holder, commentID);
            } else {
                postLike = false;
                Log.e(TAG, "checkLike: commentLike in else " + postLike + " comment id " + commentID);
                like(current_user, postID, holder, commentID);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void likeCount(String postID, ViewHolder holder, String commentID) {
        //count no of likes
        firebaseFirestore.collection("Posts").document(postID).collection("Comments")
                .document(commentID).collection("Likes").addSnapshotListener((queryDocumentSnapshots, e) -> {
            try {
                if (!Objects.requireNonNull(queryDocumentSnapshots).isEmpty()) {
                    int count = queryDocumentSnapshots.size();
                    count = count - 1;
                    if (count > 0) {
                        holder.commentCountTV.setText("+" + count);
                    } else {
                        holder.commentCountTV.setText("1");
                        Log.e(TAG, "likeCount: only 1 like by current user " + postID);
                    }
                } else {
                    holder.commentCountTV.setText("");
                    Log.e(TAG, "onEvent: no likes");
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                Log.e(TAG, "likeCount: exception getting like " + e1.getMessage());
            }
        });
    }

    private void like(String current_user, String postID, ViewHolder holder, String commentID) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("time_stamp", FieldValue.serverTimestamp());
        map.put("uid", current_user);
        try {
            firebaseFirestore.collection("Posts").document(postID).collection("Comments").document(commentID)
                    .collection("Likes").document(current_user).set(map).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    holder.likeBtn.setImageResource(R.drawable.like_selected);
                    holder.likeBtn.setImageTintList(context.getResources().getColorStateList(R.color.like_pink));
                    Log.e(TAG, "onComplete: like by" + current_user);
                    postLike = true;
                } else {
                    Log.e(TAG, "onComplete: like failed");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dislike(String current_user, String postID, ViewHolder holder, String commentID) {
        firebaseFirestore.collection("Posts").document(postID).collection("Comments")
                .document(commentID).collection("Likes").document(current_user).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.e(TAG, "dislike: like deleted ");
                holder.likeBtn.setImageResource(R.drawable.like_unselected);
                holder.likeBtn.setImageTintList(context.getResources().getColorStateList(R.color.black));

                postLike = false;
            }
        });
    }

    private void deleteComment(String commentID, String postID, ViewHolder holder) {
        firebaseFirestore.collection("Posts").document(postID).collection("Comments")
                .document(commentID).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.e(TAG, "deleteComment: comment deleted ");
                holder.deleteBtn.setEnabled(true);
                notifyDataSetChanged();
            } else {
                holder.deleteBtn.setEnabled(true);
                Log.e(TAG, "deleteComment: error " + Objects.requireNonNull(task.getException()).getMessage());
                notifyDataSetChanged();
            }
            notifyDataSetChanged();
        });
    }

    private void checkOwner(FirebaseUser user, String uid, ViewHolder holder) {
        Log.e(TAG, "checkOwner: user " + user.getUid() + " comment user " + uid);
        if (uid.equals(user.getUid())) {
            holder.deleteBtn.setVisibility(View.VISIBLE);
        }
    }

    private void getUserDataAndShowComment(String text, String uid, Date date, ViewHolder holder) {
        firebaseFirestore.collection("Users").document(uid).get().addOnCompleteListener(task -> {
            if (Objects.requireNonNull(task.getResult()).exists()) {
                Log.e(TAG, "getUserDataAndShowComment: user data exists ");
                final String name = task.getResult().getString("name");
                final String url = task.getResult().getString("profileURL");

                String comment = "<b>" + name + "</b> " + text;

                holder.nameTV.setText((Html.fromHtml(comment)));
                Glide.with(context).load(url).into(holder.profileView);
                try {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatMMDDYYYY = new SimpleDateFormat("dd MMM yy\nhh:mm a");
                    final StringBuilder nowMMDDYYYY = new StringBuilder(dateformatMMDDYYYY.format(date));
                    holder.timeTV.setText(nowMMDDYYYY);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "onBindViewHolder: date exception " + e.getMessage());
                }
            } else {
                Log.e(TAG, "getUserDataAndShowComment: error in getting user data " + Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cmntList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private CircleImageView profileView;
        private TextView nameTV, timeTV, commentCountTV;
        private ImageView deleteBtn, likeBtn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            profileView = view.findViewById(R.id.cmnt_profile);
            nameTV = view.findViewById(R.id.cmnt_nameTV);
            timeTV = view.findViewById(R.id.cmnt_timeTV);
            deleteBtn = view.findViewById(R.id.cmnt_deleteBtn);
            likeBtn = view.findViewById(R.id.cmnt_likeBtn);
            commentCountTV = view.findViewById(R.id.cmnt_likeCountTV);
        }
    }
}
