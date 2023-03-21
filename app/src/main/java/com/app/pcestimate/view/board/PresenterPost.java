package com.app.pcestimate.view.board;

import android.content.Context;
import android.util.Log;

import com.app.pcestimate.datamodel.PcDataModel;
import com.app.pcestimate.datamodel.PostDataModel;
import com.app.pcestimate.datamodel.ReplayInfo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PresenterPost {
    private static final String TAG = "##H";
    private static final String COLLECTION_PATH = "BulletinBoard";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    private PresenterPost() {
    }

    private static PresenterPost INSTANCE = null;

    public static synchronized PresenterPost getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PresenterPost();
        }
        return INSTANCE;
    }


    public Boolean setPost(PostDataModel postInfo) {
        if (postInfo == null) return false;

        if (postInfo.getTitle().isEmpty() || postInfo.getContent().isEmpty() || postInfo.getPassword().isEmpty())
            return false;

        Map<String, PostDataModel> posts = new HashMap<>();
        posts.put("Posts", postInfo);

        db.collection(COLLECTION_PATH).add(posts).addOnSuccessListener(documentReference -> {

        }).addOnFailureListener(e -> {
            Log.e(TAG, "setPost: error >>" + e);
        });

        return true;
    }

    public Boolean getPost(IPostsResultCallback callback) {
        db.collection(COLLECTION_PATH).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<PostDataModel> postsList = new ArrayList<>();

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        HashMap<String, PostDataModel> res = new HashMap<>();
                        res = (HashMap<String, PostDataModel>) snapshot.get("Posts");

                        PostDataModel data = new PostDataModel();
                        data.setTitle(String.valueOf(res.get("title")));
                        data.setContent(String.valueOf(res.get("content")));
                        data.setPassword(String.valueOf(res.get("password")));
                        data.setReplies(new ArrayList<ReplayInfo>((Collection<? extends ReplayInfo>) res.get("replies")));
//                        Log.i(TAG, "onSuccess: data ="+ data.getReplies());
                        postsList.add(data);
                        callback.onResult(postsList);
                    }
                }
            }
        });
        return true;
    }

    public interface IPostsResultCallback {
        void onResult(ArrayList<PostDataModel> list);

        void onError(String erMsg);
    }

}










































































































