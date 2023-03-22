package com.app.pcestimate.view.board;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.pcestimate.databinding.ActivityDetailPostBinding;
import com.app.pcestimate.datamodel.PostDataModel;

import java.util.ArrayList;

public class ActivityDetailPost extends AppCompatActivity {
    private ActivityDetailPostBinding mBinding;
    private static final String TAG = "##H";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDetailPostBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        initVariable();
        getPostItem();
        onViewClick();
    }

    private void initVariable() {
    }

    private void getPostItem() {
        PostDataModel getI = (PostDataModel) getIntent().getSerializableExtra("PostInfo");

        // 넘어온 데이터가 있을 경우
        if (getI != null) {
//            Log.i(TAG, "getPostItem: posttitle = " + getI.getTitle());
            mBinding.tvTitleDetailPost.setText(getI.getTitle());
            mBinding.tvContentDetailPost.setText(getI.getContent());
            mBinding.tvRepliesCountDetailPost.setText(getI.getReplies().size()+"");
        }
    }

    private void onViewClick() {
        mBinding.imBackDetailPost.setOnClickListener(v -> {
            finish();
        });
    }
}
