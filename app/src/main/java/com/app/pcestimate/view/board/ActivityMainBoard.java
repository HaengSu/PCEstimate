package com.app.pcestimate.view.board;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.ContentView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.pcestimate.R;
import com.app.pcestimate.databinding.ActivityMainBoardBinding;
import com.app.pcestimate.datamodel.PostDataModel;

import java.util.ArrayList;

public class ActivityMainBoard extends AppCompatActivity {
    private ActivityMainBoardBinding mBinding;


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
    }

    private void getPosts() {
        PresenterPost.getInstance().getPost(new PresenterPost.IPostsResultCallback() {
            @Override
            public void onResult(ArrayList<PostDataModel> list) {

            }

            @Override
            public void onError(String erMsg) {

            }
        });
    }

    private void onViewClick() {
        mBinding.btAddPost.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityWritePost.class));
        });
    }
}
