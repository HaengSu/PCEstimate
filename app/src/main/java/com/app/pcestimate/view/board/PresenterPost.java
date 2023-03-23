package com.app.pcestimate.view.board;

import android.util.Log;

import androidx.annotation.NonNull;

import com.app.pcestimate.datamodel.PostDataModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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
                        //getId() = documentId를 가져온다
                        Log.i("##INFO", "onSuccess(): data = "+ snapshot.getId());
                        HashMap<String, PostDataModel> res = new HashMap<>();
                        res = (HashMap<String, PostDataModel>) snapshot.get("Posts");

                        if (res != null) {
                            PostDataModel data = new PostDataModel();
                            data.setId(snapshot.getId());
                            data.setTitle(String.valueOf(res.get("title")));
                            data.setContent(String.valueOf(res.get("content")));
                            data.setPassword(String.valueOf(res.get("password")));
                            data.setReplies(new ArrayList<String>((Collection<? extends String>) res.get("replies")));

                            //region ---- Test Section  ---
                            Log.i("##INFO", "onSuccess(): data.getId = "+data.getId());
                            //endregion
                            postsList.add(data);
                            callback.onResult(postsList);
                        }
                    }
                }
            }
        });
        return true;
    }

    public Boolean setReply(PostDataModel postInfo) {
        db.collection(COLLECTION_PATH).document(postInfo.getId()).update("Posts", postInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("##INFO", "onSuccess(): success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("##INFO", "onFailure(): e = "+e.getMessage());
            }
        });
        return true;
    }

    public interface IPostsResultCallback {
        void onResult(ArrayList<PostDataModel> list);

        void onError(String erMsg);
    }

}










































































































