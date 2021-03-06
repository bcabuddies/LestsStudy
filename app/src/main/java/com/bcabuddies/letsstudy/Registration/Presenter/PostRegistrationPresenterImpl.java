package com.bcabuddies.letsstudy.Registration.Presenter;

import android.net.Uri;
import android.util.Log;

import com.bcabuddies.letsstudy.Registration.view.PostRegistrationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PostRegistrationPresenterImpl implements PostRegistrationPresenter {

    private PostRegistrationView postRegView;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private StorageReference thumbImgRef;
    private Uri thumb_downloadUrl = null;

    public PostRegistrationPresenterImpl(FirebaseFirestore db, FirebaseUser user, StorageReference thumbImgRef) {
        this.db = db;
        this.user = user;
        this.thumbImgRef = thumbImgRef;
    }

    @Override
    public void uploadData(String name, String age, String profileUri, String pursuing) {
        if (name.isEmpty()) {                                                       //if full name is empty
            postRegView.showValidationError();
            Log.e(TAG, "uploadData: if");
        } else if (thumb_downloadUrl == null) {                                     //if user did not sign in using google or facebook
            Log.e(TAG, "uploadData: else if");
            if (!(profileUri == null)) {                                            //if user did not upload custom image
                thumb_downloadUrl = Uri.parse(profileUri);
                Log.e(TAG, "uploadData: else if- if" + profileUri);
                detailsUpload(name, age, pursuing, thumb_downloadUrl);
            } else {                                                                  //if there is no profile pic at the end
                Log.e(TAG, "uploadData: else if- else" + thumb_downloadUrl);
                thumb_downloadUrl = Uri.parse("https://firebasestorage.googleapis.com/v0/b/fitsteps-311ed.appspot.com/o/default_user_thumb%2Fdefault.png?alt=media&token=c2de219c-9430-48bf-84c1-b2ba0b37be66");
                detailsUpload(name, age, pursuing, thumb_downloadUrl);
            }
        } else {                                                                    //if user sing in using google or fb
            detailsUpload(name, age, pursuing, thumb_downloadUrl);
        }
    }

    private void detailsUpload(String name, String age, String pursuing, Uri thumb_downloadUrl) {
        String uid = user.getUid();
        HashMap<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("age", age);
        data.put("profileURL", thumb_downloadUrl.toString());
        data.put("pursuing", pursuing);
        data.put("uid", uid);

        db.collection("Users").document(uid).get().addOnCompleteListener(task -> {
            try {
                if (Objects.requireNonNull(task.getResult()).exists()) {
                    Log.e(TAG, "detailsUpload: data exists" );
                    try {
                        Log.e(TAG, "detailsUpload: data exists and in try of update " );
                        db.collection("Users").document(uid).update(data).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                postRegView.detailUploadSuccess();
                            } else {
                                postRegView.detailsUploadError(Objects.requireNonNull(task.getException()).getMessage());
                                Log.e(TAG, "onComplete:try:  " + task1.getException());
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "onClick: error " + e.getMessage());
                    }
                } else {
                    Log.e(TAG, "detailsUpload: no data exists " );
                    try {
                        Log.e(TAG, "detailsUpload: no data exists and in try of set " );
                        db.collection("Users").document(uid).set(data).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                postRegView.detailUploadSuccess();
                            } else {
                                postRegView.detailsUploadError(Objects.requireNonNull(task.getException()).getMessage());
                                Log.e(TAG, "onComplete:try:  " + task1.getException());
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "onClick: error " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "detailsUpload: exception in points " + e.getMessage());
            }
        });

        Log.e(TAG, "uploadData: data ready to upload " + data);
    }

    @Override
    public void getMenu() {
        Log.e(TAG, "getMenu: get menu started ");
        db.collection("PreData").document("Education").get().addOnCompleteListener(task -> {
            if (Objects.requireNonNull(task.getResult()).exists()) {
                Log.e(TAG, "getMenu: data found ");
                String menu_item = task.getResult().getString("menu");
                Log.e(TAG, "getMenu: data " + menu_item);
                String[] parts = Objects.requireNonNull(menu_item).split(", ");
                Log.e(TAG, "getMenu: " + Arrays.toString(parts));
                postRegView.pursuingMenu(parts);
            } else {
                //no data
                Log.e(TAG, "getMenu: no data ");
            }
        });
    }

    public void firebaseDataPre() {
        db.collection("Users").document(user.getUid()).get().addOnCompleteListener(task -> {
            if (Objects.requireNonNull(task.getResult()).exists()) {
                String profUrl;
                profUrl = task.getResult().getString("profileURL");
                if (profUrl == null) {
                    //  profUrl = "https://firebasestorage.googleapis.com/v0/b/letsstudy-c77c3.appspot.com/o/user_defalut_profile%2Fdefault.png?alt=media&token=027c4d7f-8452-4ff9-a4c7-263b77254bd0";
                    profUrl = "";
                }
                String name = task.getResult().getString("name");
                String courseName = task.getResult().getString("pursuing");
                String age = task.getResult().getString("age");
                Log.e(TAG, "onComplete: firebasepredata: prof " + profUrl);
                postRegView.firebasePreData(name, profUrl, courseName, age);
                Log.e(TAG, "onComplete: firebasedatapre : " + name + "    " + profUrl + "    " + courseName);
            } else {
                Log.e(TAG, "onComplete: firebasepredata: no data");
            }
        });
    }

    public void imagePost(byte[] thumb_byte) {
        final StorageReference thumb_filePath = thumbImgRef.child(user.getUid() + ".jpg");
        thumb_filePath.putBytes(thumb_byte).addOnSuccessListener(taskSnapshot -> Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl().addOnSuccessListener(uri -> {
            thumb_downloadUrl = uri;
            Log.e("mKey", "thumb download url: " + thumb_downloadUrl);
        }));
    }

    @Override
    public void attachView(PostRegistrationView view) {
        postRegView = view;
    }

    @Override
    public void detachView() {
        postRegView = null;
    }
}
