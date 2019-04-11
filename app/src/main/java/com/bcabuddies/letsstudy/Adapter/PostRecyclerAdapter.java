package com.bcabuddies.letsstudy.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bcabuddies.letsstudy.Model.PostData;
import com.bcabuddies.letsstudy.Post.view.Post;
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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder> {

    private ArrayList<PostData> postList;

    public static Context context;
    private static final String TAG = "PostRecAdapter";
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private Boolean postLike;
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    public PostRecyclerAdapter(ArrayList<PostData> postList) {
        this.postList = postList;
        Log.e(TAG, "PostRecyclerAdapter: postlist size " + this.postList.size());
    }

    @NonNull
    @Override
    public PostRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_row, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get postID
        final String postID = postList.get(position).PostID;

        //get current user
        final String current_user = firebaseUser.getUid();

        //getting data
        final String text = postList.get(position).getText();
        final Date timestamp = postList.get(position).getTimestamp();
        final String postUserId = postList.get(position).getUser();
        final String url = postList.get(position).getUrl();

        //check postLike
        checkLike(postID, current_user, holder);

        if (postList.get(position).getUrl() != null) {
            Log.e(TAG, "onBindViewHolder: url " + position);
            holder.setPostImageView(url);
            //change the scaleType of postImageView
            holder.postImageView.setScaleType(ImageView.ScaleType.CENTER);
        }

        //this will retrieve user data at for each position
        firebaseFirestore.collection("Users").document(postUserId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String fName = task.getResult().getString("name");
                String profUrl = task.getResult().getString("profileURL");
                holder.setNameTV(fName);
                holder.setProf(profUrl);
            } else {
                Log.e(TAG, "onComplete: error");
            }
        });
        holder.descSet(text);
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatMMDDYYYY = new SimpleDateFormat("dd MMM yy \t hh:mm a");
            final StringBuilder nowMMDDYYYY = new StringBuilder(dateformatMMDDYYYY.format(timestamp));
            holder.setDateTV(nowMMDDYYYY);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onBindViewHolder: date exception " + e.getMessage());
        }
        setAnimation(holder.itemView, position);

        //liking and disliking
        holder.likeCard.setOnClickListener(v -> likeFeature(postID, current_user, holder, postUserId));

        //menu on post click
        holder.postMenu.setOnClickListener(v -> postMenu(postID, current_user, postUserId, holder));

        //opening a post
        holder.postCard.setOnClickListener(v -> openPost(postID));
    }

    private void postMenu(String postID, String current_user, String postUserId, ViewHolder holder) {
        PopupMenu popupMenu = new PopupMenu(context, holder.postMenu);
        popupMenu.getMenuInflater().inflate(R.menu.empty_menu, popupMenu.getMenu());
        String menuListOwn[] = {"Delete"};
        String menuListUser[] = {"Report"};

        if (current_user.equals(postUserId)) {
            for (String s : menuListOwn) {
                popupMenu.getMenu().add(s);
                Log.e(TAG, "pursuingMenu: s " + s);
            }
        } else {
            for (String s : menuListUser) {
                popupMenu.getMenu().add(s);
                Log.e(TAG, "pursuingMenu: s " + s);
            }
        }
        popupMenu.show();
    }

    private void checkLike(String postID, String current_user, ViewHolder holder) {
        firebaseFirestore.collection("Posts").document(postID).collection("Likes").document(current_user).get().addOnCompleteListener(task -> {
            if (task.getResult().exists()) {
                postLike = true;
                holder.likeTV.setVisibility(GONE);
                holder.likeIcon.setVisibility(GONE);
                holder.likedIcon.setVisibility(View.VISIBLE);
                holder.likedTV.setVisibility(View.VISIBLE);
                Log.e(TAG, "checkLike: post is already liked by user " + postID);
                Log.e(TAG, "checkLike: postLike " + postLike + " post id " + postID);
            } else {
                postLike = false;
                Log.e(TAG, "checkLike: postLike in else " + postLike + " post id " + postID);
            }
        });
    }

    private void likeFeature(String postID, String current_user, ViewHolder holder, String postUserId) {
        firebaseFirestore.collection("Posts").document(postID).collection("Likes").document(current_user).get().addOnCompleteListener(task -> {
            if (task.getResult().exists()) {
                postLike = true;
                holder.likeTV.setVisibility(GONE);
                holder.likeIcon.setVisibility(GONE);
                holder.likedIcon.setVisibility(View.VISIBLE);
                holder.likedTV.setVisibility(View.VISIBLE);
                Log.e(TAG, "checkLike: post is already liked by user " + postID);
                Log.e(TAG, "checkLike: postLike " + postLike + " post id " + postID);
                dislike(current_user, postID, holder, postUserId);
            } else {
                postLike = false;
                Log.e(TAG, "checkLike: postLike in else " + postLike + " post id " + postID);
                like(current_user, postID, holder, postUserId);
            }
        });
    }

    private void openPost(String postID) {
        Intent openPost = new Intent(context, Post.class);
        openPost.putExtra("postID", postID);
        context.startActivity(openPost);
    }

    private void dislike(String current_user, String postID, ViewHolder holder, String postUserId) {
        firebaseFirestore.collection("Posts").document(postID).collection("Likes").document(current_user).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.e(TAG, "dislike: like deleted ");
                holder.likeTV.setVisibility(View.VISIBLE);
                holder.likeIcon.setVisibility(View.VISIBLE);
                holder.likedIcon.setVisibility(GONE);
                holder.likedTV.setVisibility(GONE);

                postLike = false;

                //removing points
                firebaseFirestore.collection("Users").document(postUserId).get().addOnCompleteListener(task1 -> {
                    if (task1.getResult().exists()) {
                        long points = (long) task1.getResult().get("points");
                        Log.e(TAG, "onBindViewHolder: points before removing 10 " + points);
                        points = points - 10;
                        Log.e(TAG, "onBindViewHolder: points after removing 10 " + points);

                        HashMap<String, Object> data = new HashMap<>();
                        data.put("points", points);

                        firebaseFirestore.collection("Users").document(postUserId).update(data).addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {
                                Log.e(TAG, "onBindViewHolder: points removed ");
                            } else {
                                Log.e(TAG, "onBindViewHolder: points not added error " + task2.getException().getMessage());
                            }
                        });
                    }
                });
            }
        });
    }

    private void like(String current_user, String postID, ViewHolder holder, String postUserId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("time_stamp", FieldValue.serverTimestamp());
        map.put("uid", current_user);
        try {
            firebaseFirestore.collection("Posts").document(postID).collection("Likes").document(current_user).set(map).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    holder.likeTV.setVisibility(GONE);
                    holder.likeIcon.setVisibility(GONE);
                    holder.likedIcon.setVisibility(View.VISIBLE);
                    holder.likedTV.setVisibility(View.VISIBLE);
                    Log.e(TAG, "onComplete: like by" + current_user);

                    postLike = true;

                    //adding points
                    firebaseFirestore.collection("Users").document(postUserId).get().addOnCompleteListener(task1 -> {
                        if (task1.getResult().exists()) {
                            long points = (long) task1.getResult().get("points");
                            Log.e(TAG, "onBindViewHolder: points before adding 10 " + points);
                            points = points + 10;
                            Log.e(TAG, "onBindViewHolder: points after adding 10 " + points);

                            HashMap<String, Object> data = new HashMap<>();
                            data.put("points", points);

                            firebaseFirestore.collection("Users").document(postUserId).update(data).addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    Log.e(TAG, "onBindViewHolder: points added ");
                                } else {
                                    Log.e(TAG, "onBindViewHolder: points not added error " + task2.getException().getMessage());
                                }
                            });
                        }
                    });
                } else {
                    Log.e(TAG, "onComplete: like failed");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setAnimation(View itemView, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private ImageView postImageView;
        private TextView descTV;
        private TextView nameTV;
        private TextView dateTV;
        private TextView likeTV;
        private TextView likedTV;
        private ImageView likeIcon, likedIcon;
        private CircleImageView prof;
        private CardView likeCard;
        private ImageView postMenu;
        private CardView postCard;
        private TextView longPost;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            postImageView = mView.findViewById(R.id.homerow_imageview);
            descTV = mView.findViewById(R.id.homerow_descTV);
            nameTV = mView.findViewById(R.id.homerow_nameTV);
            dateTV = mView.findViewById(R.id.homerow_dateTV);
            likeTV = mView.findViewById(R.id.homerow_likeTV);
            likedTV = mView.findViewById(R.id.homerow_likedTV);
            likeIcon = mView.findViewById(R.id.homerow_likeIcon);
            likedIcon = mView.findViewById(R.id.homerow_likedIcon);
            prof = mView.findViewById(R.id.homerow_prof);
            likeCard = mView.findViewById(R.id.homerow_likecard);
            postMenu = mView.findViewById(R.id.homerow_menu);
            postCard = mView.findViewById(R.id.homerow_postCard);
            longPost = mView.findViewById(R.id.homerow_longTV);
        }

        void setPostImageView(String mUrl) {
            Glide.with(context).load(mUrl).into(postImageView);
            postImageView.requestLayout();
            postImageView.getLayoutParams().height = 800;
            longPost.setVisibility(View.VISIBLE);
        }

        void descSet(String text) {
            descTV.setText(text);
        }

        void setNameTV(String name) {
            nameTV.setText(name);
        }

        void setDateTV(StringBuilder timeStamp) {
            dateTV.setText(timeStamp);
        }

        void setProf(String profUrl) {
            Glide.with(context).load(profUrl).into(prof);
        }
    }


}
