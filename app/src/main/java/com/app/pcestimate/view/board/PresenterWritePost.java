package com.app.pcestimate.view.board;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.app.pcestimate.datamodel.PostDataModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PresenterWritePost {
    private static final String TAG = "##H";
    private static final String COLLECTION_PATH = "BulletinBoard";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    public Boolean setPost(PostDataModel postInfo) {
        if (postInfo == null) return false;

        if (postInfo.getTitle().isEmpty() || postInfo.getContent().isEmpty() || postInfo.getPassword().isEmpty())
            return false;

        Map<String, PostDataModel> posts = new HashMap<>();
        posts.put("Posts", postInfo);

        db.collection(COLLECTION_PATH).add(posts).addOnSuccessListener(documentReference -> {

        }).addOnFailureListener(e -> {
            Log.e(TAG, "setPost: error >>"+e);
        });

        return true;
    }


}
