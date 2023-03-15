package com.app.pcestimate.view.estimate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.pcestimate.R;
import com.app.pcestimate.databinding.ActivitySearchResultBinding;
import com.app.pcestimate.datamodel.PcDataModel;
import com.app.pcestimate.view.estimate.adapter.AdapterPcCard;
import com.app.pcestimate.view.estimate.presenter.EstimatePresenter;

import java.util.ArrayList;

public class ActivityEstimateResult extends AppCompatActivity {

    private ActivitySearchResultBinding mBinding;
    private EstimatePresenter mPresenter;
    private AdapterPcCard adapter;
    public static final String KEY_PRICE_INDEX = "price_index";
    private int priceIndex = 1;

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

        mBinding.recyclerViewItem.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterPcCard();
        mBinding.recyclerViewItem.setAdapter(adapter);

        getEstimateResult(priceIndex);
    }

    private void setEvent(){

        mBinding.btnBack.setOnClickListener(v -> {
            finish();
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
}