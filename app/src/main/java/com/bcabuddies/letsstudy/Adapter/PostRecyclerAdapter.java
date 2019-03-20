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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder> {

    private ArrayList<PostData> postList;
    private ArrayList<UserData> userList;

    public static Context context;
    private static final String TAG = "PostRecAdapter";
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    public PostRecyclerAdapter(ArrayList<PostData> postList, ArrayList<UserData> userList) {
        this.postList = postList;
        this.userList = userList;
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
        final String user = postList.get(position).getUser();
        final String url = postList.get(position).getUrl();

        try {
            String name = userList.get(position).getName();
            String userProf = userList.get(position).getProfileURL();

            holder.setNameTV(name);
            holder.setProf(userProf);

            Log.e(TAG, "onBindViewHolder: profile url "+userProf);
            Log.e(TAG, "onBindViewHolder: name: "+name );
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: name error "+e.getMessage() );
        }

        if (postList.get(position).getUrl() != null) {
            Log.e(TAG, "onBindViewHolder: url null" + position);
            holder.setPostImageView(url);
        }
        holder.descSet(text);

        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatMMDDYYYY = new SimpleDateFormat("dd MMM yy \t hh:mm a");
            final StringBuilder nowMMDDYYYY = new StringBuilder(dateformatMMDDYYYY.format(timestamp));
            holder.setDateTV(nowMMDDYYYY);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onBindViewHolder: date exception "+e.getMessage() );
        }

        setAnimation(holder.itemView, position);
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
        private CircleImageView prof;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            postImageView = mView.findViewById(R.id.homerow_imageview);
            descTV = mView.findViewById(R.id.homerow_descTV);
            nameTV = mView.findViewById(R.id.homerow_nameTV);
            dateTV = mView.findViewById(R.id.homerow_dateTV);
            prof = mView.findViewById(R.id.homerow_prof);
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
