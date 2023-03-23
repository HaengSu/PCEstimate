package com.app.pcestimate.view.board;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.pcestimate.databinding.ActivityDetailPostBinding;
import com.app.pcestimate.datamodel.PostDataModel;

import java.util.ArrayList;

// TODO: 2023/03/23  => 수정 , 삭제 클릭시 비밀번호 확인하는 다이얼로그 띄우고 성공시 글작성페이지로 데이터 그대로 이동하여 수정 , 삭제시 파이어베이스 데이터 삭제
// TODO: 2023/03/23  => 댓글 작성시 실시간 업데이트 기능 추가


public class ActivityDetailPost extends AppCompatActivity {
    private ActivityDetailPostBinding mBinding;
    private static final String TAG = "##H";
    private PostDataModel postInfo;
    private ArrayList<String> replyList;
    private AdapterReplay mAdapter;

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
        replyList = new ArrayList<>();
        mAdapter = new AdapterReplay(replyList);
    }

    private void getPostItem() {
        postInfo = (PostDataModel) getIntent().getSerializableExtra("PostInfo");

        // 넘어온 데이터가 있을 경우
        if (postInfo != null) {
            replyList = postInfo.getReplies();
            mBinding.tvTitleDetailPost.setText(postInfo.getTitle());
            mBinding.tvContentDetailPost.setText(postInfo.getContent());
            mBinding.tvRepliesCountDetailPost.setText(postInfo.getReplies().size()+"");
        }

        setReplyData();
    }

    private void setReplyData() {
        mBinding.reRepliesDetail.setAdapter(mAdapter);
        mBinding.reRepliesDetail.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.updateReplyList(replyList);
    }

    private void onViewClick() {
        mBinding.imBackDetailPost.setOnClickListener(v -> {
            finish();
        });

        mBinding.imSendDetail.setOnClickListener(v -> {
            String reply = mBinding.edReplyDetail.getText().toString();
            replyList.add(reply);
            postInfo.setReplies(replyList);
            PresenterPost.getInstance().setReply(postInfo);
        });
    }
}































