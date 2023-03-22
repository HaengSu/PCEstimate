package com.app.pcestimate.view.board;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.pcestimate.R;
import com.app.pcestimate.databinding.ActivityMainBoardBinding;
import com.app.pcestimate.datamodel.PostDataModel;

import java.util.ArrayList;

public class ActivityMainBoard extends AppCompatActivity {
    private static final String TAG = "##H";
    private ActivityMainBoardBinding mBinding;
    private AdapterBoard mAdapter;
    private ArrayList<PostDataModel> pList;
    private ArrayList<PostDataModel> searchList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_board);

        initVariable();
        getPosts();
        onViewClick();
    }

    private void initVariable() {
        //presenter 싱글톤 사용을 위해서 getInstance() 최초 한번 호출
        PresenterPost.getInstance();
        searchList = new ArrayList<>();

        pList = new ArrayList<>();
        mAdapter = new AdapterBoard(pList);
        mBinding.rePosts.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rePosts.setAdapter(mAdapter);
    }

    private void getPosts() {
        PresenterPost.getInstance().getPost(new PresenterPost.IPostsResultCallback() {
            @Override
            public void onResult(ArrayList<PostDataModel> list) {
                pList = list;
                mAdapter.updatePostList(list);
            }

            @Override
            public void onError(String erMsg) {
                Log.e("##H", "onError: error = " + erMsg);
            }
        });
    }

    private void onViewClick() {
        mBinding.imWriteMainBoard.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityWritePost.class));
        });

        mBinding.imSearchMainBoard.setOnClickListener(v -> {
            Intent i = new Intent(this, ActivityPostSearch.class);
            i.putExtra("postList", pList);
            startActivity(i);
        });
    }
}
























