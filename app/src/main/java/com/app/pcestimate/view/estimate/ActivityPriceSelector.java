package com.app.pcestimate.view.estimate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.pcestimate.ActivityMain;
import com.app.pcestimate.R;
import com.app.pcestimate.databinding.ActivityPriceSelectorBinding;
import com.app.pcestimate.datamodel.PriceDataModel;

/**
 * 가격대 선택 화면
 */
public class ActivityPriceSelector extends AppCompatActivity {

    private ActivityPriceSelectorBinding mBinding;

    private int progressIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_price_selector);

        init();
        setEvent();

        mBinding.seekBarPrice.setProgress(3); // 초기값으로 90~100만원대 셋팅

    }

    private void init(){

        // 가격 범위
        mBinding.seekBarPrice.setMin(0);
        mBinding.seekBarPrice.setMax(PriceDataModel.priceHints.length - 1);

    }

    private void setEvent(){

        // back
        mBinding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        // seekbar
        mBinding.seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressIndex = seekBar.getProgress();
                mBinding.tvPrice.setText(PriceDataModel.priceHints[progressIndex]);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // next button
        mBinding.btnConfirm.setOnClickListener(v -> {
            Intent i = new Intent(this, ActivityEstimateResult.class);
            i.putExtra(ActivityEstimateResult.KEY_PRICE_INDEX, progressIndex);
            startActivity(i);
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ActivityMain.class));
        finish();
    }
}