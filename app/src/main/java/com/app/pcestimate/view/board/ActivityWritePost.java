package com.app.pcestimate.view.board;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.pcestimate.R;
import com.app.pcestimate.databinding.ActivityWritePostBinding;
import com.app.pcestimate.datamodel.PostDataModel;

import java.util.ArrayList;

// TODO: 2023/03/23 => 사진 넣기 기능 추가 및 파이어베이스 스토리지로 전송하여 url로 변경

public class ActivityWritePost extends AppCompatActivity {
    private ActivityWritePostBinding mBinding;
    private static final String TAG = "##H";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_write_post);

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
            mBinding.edTitleWrite.setText(getI.getTitle());
            mBinding.edContentWrite.setText(getI.getContent());
        }
    }

    private void onViewClick() {
        mBinding.btCreateWrite.setOnClickListener(v -> {
            String title = mBinding.edTitleWrite.getText().toString();
            String content = mBinding.edContentWrite.getText().toString();
            String password = mBinding.edPasswordWrite.getText().toString();

            Boolean res = PresenterPost.getInstance().setPost(new PostDataModel(title, content, password, new ArrayList<>()));
            if (res) {
                startActivity(new Intent(this, ActivityMainBoard.class));
                finish();
            } else {
                Toast.makeText(this, "게시글 작성에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}




























