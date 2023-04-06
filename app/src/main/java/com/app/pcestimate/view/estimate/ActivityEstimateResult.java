package com.app.pcestimate.view.estimate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.pcestimate.ActivityMain;
import com.app.pcestimate.R;
import com.app.pcestimate.databinding.ActivitySearchResultBinding;
import com.app.pcestimate.datamodel.PcDataModel;
import com.app.pcestimate.util.FileUtils;
import com.app.pcestimate.view.estimate.adapter.AdapterPcCard;
import com.app.pcestimate.view.estimate.presenter.EstimatePresenter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityEstimateResult extends AppCompatActivity {

    private ActivitySearchResultBinding mBinding;
    private EstimatePresenter mPresenter;
    private AdapterPcCard adapter;
    public static final String KEY_PRICE_INDEX = "price_index";
    private int priceIndex = 1;

    private Animation loadingAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_result);

        init();
        setEvent();

    }

    private void init(){

        mPresenter = new EstimatePresenter();
        priceIndex = getIntent().getIntExtra(KEY_PRICE_INDEX, 1);

        loadingAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        loadingAnimation.setDuration(2000);
        loadingAnimation.setRepeatCount(0);
        mBinding.imgLoading.startAnimation(loadingAnimation);

        mBinding.recyclerViewItem.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterPcCard();
        mBinding.recyclerViewItem.setAdapter(adapter);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                getEstimateResult(priceIndex);

            }
        }, 1500);
    }

    private void setEvent(){

        mBinding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        mBinding.btnInit.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityPriceSelector.class));
            finish();
        });

        mBinding.btnShare.setOnClickListener(v -> {
            FileUtils.shareImage(this, FileUtils.viewToBitmap(mBinding.recyclerViewItem));
        });

        mBinding.btnSave.setOnClickListener(v -> {
            FileUtils.bitmapToUri(this, FileUtils.viewToBitmap(mBinding.recyclerViewItem));
            Toast.makeText(this, "견적이 저장되었습니다.", Toast.LENGTH_SHORT).show();
//            FileUtils.saveImage(this, FileUtils.bitmapToUri(this, FileUtils.viewToBitmap(mBinding.recyclerViewItem)), "estimate_" + System.currentTimeMillis() + ".jpg");
        });
    }

    /**
     * 가격대별 견적 리스트 가져오기
     */
    private void getEstimateResult(int index){

        mPresenter.getEstimateResult(index, new EstimatePresenter.IEstimateResultCallback() {
            @Override
            public void onResult(ArrayList<PcDataModel> list, String totalPrice) {

                runOnUiThread(() -> {

                    adapter.setList(list);
                    mBinding.tvTotalPrice.setText(totalPrice);

                    mBinding.loadingView.setVisibility(View.GONE);
                });
            }

            @Override
            public void onError(String msg) {
                runOnUiThread(() -> {
                    Toast.makeText(ActivityEstimateResult.this, msg, Toast.LENGTH_SHORT).show();
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ActivityMain.class));
        finish();
    }
}