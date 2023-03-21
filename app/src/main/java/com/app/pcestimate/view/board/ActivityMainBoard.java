package com.app.pcestimate.view.board;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.ContentView;
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
    private AdapterMainBoard mAdapter;
    private ArrayList<PostDataModel> pList;


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

        pList = new ArrayList<>();
        mAdapter = new AdapterMainBoard(pList);
        mBinding.rePosts.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rePosts.setAdapter(mAdapter);
    }

    private void getPosts() {
        PresenterPost.getInstance().getPost(new PresenterPost.IPostsResultCallback() {
            @Override
            public void onResult(ArrayList<PostDataModel> list) {
                mAdapter.updatePostList(list);
            }

            @Override
            public void onError(String erMsg) {
                Log.e("##H", "onError: error = "+erMsg );
            }
        });
    }

    private void onViewClick() {
        mBinding.imWriteMainBoard.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityWritePost.class));
        });

        mBinding.imSearchMainBoard.setOnClickListener(v -> {
            mBinding.layoutSearchMainBoard.setVisibility(View.VISIBLE);
        });

        mBinding.imSearchEditBoard.setOnClickListener(v -> {
            searchPost();
        });

        mBinding.imBackMainBoard.setOnClickListener(view -> {
            mBinding.layoutSearchMainBoard.setVisibility(View.GONE);
        });
    }

    private void searchPost() {
        String keyword = mBinding.edSearchPost.getText().toString();
        Log.i(TAG, "searchPost : keyword = "+keyword);

    }
}
























