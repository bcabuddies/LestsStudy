package com.bcabuddies.letsstudy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bcabuddies.letsstudy.Model.PostData;
import com.bcabuddies.letsstudy.Model.UserData;
import com.bcabuddies.letsstudy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder> {

    private ArrayList<PostData> postList;
    private ArrayList<UserData> userList;

    public static Context context;
    private static final String TAG = "PostRecAdapter";
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    public PostRecyclerAdapter(ArrayList<PostData> postList, ArrayList<UserData> userList) {
        this.postList = postList;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int resource = R.layout.home_row;
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
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
        final Date timestamp = postList.get(position).getTime();
        final String type = postList.get(position).getType();
        final String user = postList.get(position).getUser();
        final String url = postList.get(position).getUrl();
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
