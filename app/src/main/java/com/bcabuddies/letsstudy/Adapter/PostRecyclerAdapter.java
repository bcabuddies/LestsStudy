package com.bcabuddies.letsstudy.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcabuddies.letsstudy.Model.PostData;
import com.bcabuddies.letsstudy.Model.UserData;
import com.bcabuddies.letsstudy.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
        final String type = postList.get(position).getType();
        final String postUserId = postList.get(position).getUser();
        final String url = postList.get(position).getUrl();

        if (postList.get(position).getUrl() != null) {
            Log.e(TAG, "onBindViewHolder: url " + position);
            holder.setPostImageView(url);
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

        holder.likeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> map=new HashMap<>();
                map.put("time_stamp",FieldValue.serverTimestamp());
                map.put("uid",current_user.toString());
                try {
                    firebaseFirestore.collection("Posts").document(postID).collection("Likes").document(current_user).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                holder.likeTV.setVisibility(GONE);
                                holder.likeIcon.setVisibility(GONE);
                                holder.likedIcon.setVisibility(View.VISIBLE);
                                holder.likedTV.setVisibility(View.VISIBLE);
                                Log.e(TAG, "onComplete: like by"+current_user.toString() );
                            }
                            else{
                                Log.e(TAG, "onComplete: like failed");
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

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

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            postImageView = mView.findViewById(R.id.homerow_imageview);
            descTV = mView.findViewById(R.id.homerow_descTV);
            nameTV = mView.findViewById(R.id.homerow_nameTV);
            dateTV = mView.findViewById(R.id.homerow_dateTV);
            likeTV=mView.findViewById(R.id.homerow_likeTV);
            likedTV=mView.findViewById(R.id.homerow_likedTV);
            likeIcon=mView.findViewById(R.id.homerow_likeIcon);
            likedIcon=mView.findViewById(R.id.homerow_likedIcon);
            prof = mView.findViewById(R.id.homerow_prof);
            likeCard=mView.findViewById(R.id.homerow_likecard);
        }

        void setPostImageView(String mUrl) {
            Glide.with(context).load(mUrl).into(postImageView);
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
