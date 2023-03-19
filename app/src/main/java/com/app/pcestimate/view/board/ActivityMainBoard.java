package com.app.pcestimate.view.board;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.ContentView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.pcestimate.R;
import com.app.pcestimate.databinding.ActivityMainBoardBinding;

public class ActivityMainBoard extends AppCompatActivity {
    private ActivityMainBoardBinding mBinding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_board);

        initVariable();
        onViewClick();
    }

    private void initVariable() {

    }

    private void onViewClick() {
        mBinding.btAddPost.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityWritePost.class));
        });
    }
}
