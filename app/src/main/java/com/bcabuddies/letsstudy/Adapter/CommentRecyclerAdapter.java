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
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder> {

    private ArrayList<CommentData> cmntList;

    private static final String TAG = "CommentAdapter.java";
    private static Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser user;
    private int resource = R.layout.comment_row;

    public CommentRecyclerAdapter(ArrayList<CommentData> cmntList) {
        this.cmntList = cmntList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

        likeFeature(holder);

        holder.likeBtn.setOnClickListener(v -> {
            holder.likeBtn.setImageResource(R.drawable.like_selected);
            holder.likeBtn.setImageTintList(context.getResources().getColorStateList(R.color.like_pink));
        });
    }

    private void likeFeature(ViewHolder holder) {
        //check if already liked
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
                Log.e(TAG, "deleteComment: error " + task.getException().getMessage());
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
            if (task.getResult().exists()) {
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
                Log.e(TAG, "getUserDataAndShowComment: error in getting user data " + task.getException().getMessage());
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
        private TextView nameTV, timeTV;
        private ImageView deleteBtn, likeBtn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            profileView = view.findViewById(R.id.cmnt_profile);
            nameTV = view.findViewById(R.id.cmnt_nameTV);
            timeTV = view.findViewById(R.id.cmnt_timeTV);
            deleteBtn = view.findViewById(R.id.cmnt_deleteBtn);
            likeBtn = view.findViewById(R.id.cmnt_likeBtn);
        }
    }
}
