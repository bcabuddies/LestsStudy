package com.bcabuddies.letsstudy.NewPost.Presenter;

import android.net.Uri;
import android.util.Log;

import com.bcabuddies.letsstudy.NewPost.view.NewPostView;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NewPostPresenterImpl implements NewPostPresenter {

    private FirebaseUser user;
    private FirebaseFirestore db;
    private NewPostView postView;
    private StorageReference thumbImgRef;
    private static final String TAG = "NewPostPresImpl.java";
    private Uri thumb_downloadUri;

    public NewPostPresenterImpl(FirebaseUser user, FirebaseFirestore db, StorageReference thumbImgRef) {
        this.user = user;
        this.db = db;
        this.thumbImgRef = thumbImgRef;
        Log.e(TAG, "NewPostPresenterImpl: thumb: " + thumbImgRef.toString());
    }

    @Override
    public void attachView(NewPostView view) {
        postView = view;
    }

    @Override
    public void detachView() {
        postView = null;
    }

    @Override
    public void imagePost(byte[] thumb_byte, String text) {
        String randomName = UUID.randomUUID().toString();
        Log.e(TAG, "imagePost: text " + text + " thumb byte " + thumb_byte.length);
        final StorageReference thumb_filePath = thumbImgRef.child(randomName + ".jpg");
        thumb_filePath.putBytes(thumb_byte).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> getDownloadUri = taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> {
                thumb_downloadUri = uri;
                Log.e(TAG, "imagePost: thumb_uri " + uri.toString());
                uploadData(thumb_downloadUri, text);
            }).addOnFailureListener(e -> postView.errorUpload(e.getMessage()));
        });
    }

    private void uploadData(Uri thumb_downloadUri, String text) {
        Log.e(TAG, "uploadData: entered data upload ");
        Map<String, Object> map = new HashMap<>();
        map.put("url", thumb_downloadUri.toString());
        map.put("type", "photo");
        map.put("timestamp", FieldValue.serverTimestamp());
        map.put("text", text);
        map.put("user", user.getUid());
        db.collection("Posts").add(map).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                postView.uploadSuccess();
                Log.e(TAG, "uploadData: upload success");
            } else {
                postView.errorUpload(task.getException().getMessage());
                Log.e(TAG, "uploadData: upload error " + task.getException().getMessage());
            }
        });
    }
}
