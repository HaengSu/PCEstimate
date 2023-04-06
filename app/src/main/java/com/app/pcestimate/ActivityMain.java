package com.app.pcestimate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.app.pcestimate.databinding.ActivityMainBinding;
import com.app.pcestimate.view.board.ActivityBoardWriter;
import com.app.pcestimate.view.board.ActivityMainBoard;
import com.app.pcestimate.view.estimate.ActivityPriceSelector;

public class ActivityMain extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setEvent();

    }

    private void setEvent(){

        mBinding.btnEstimate.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityPriceSelector.class));
            finish();
        });

        mBinding.btnBoard.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityMainBoard.class));
            finish();
        });

    }
}