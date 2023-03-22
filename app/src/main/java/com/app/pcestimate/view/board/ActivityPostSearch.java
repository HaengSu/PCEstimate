package com.app.pcestimate.view.board;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.pcestimate.databinding.ActivityPostSearchBinding;
import com.app.pcestimate.datamodel.PostDataModel;

import java.util.ArrayList;

public class ActivityPostSearch extends AppCompatActivity {
    private ActivityPostSearchBinding mBinding;
    private static final String TAG = "##H";
    private ArrayList<PostDataModel> pList;
    private ArrayList<PostDataModel> searchList;
    private AdapterBoard mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityPostSearchBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        intiVariable();
        getPostList();
        onViewClick();
    }

    private void getPostList() {
        ArrayList<PostDataModel> getI = (ArrayList<PostDataModel>) getIntent().getSerializableExtra("postList");
        pList = getI;
    }

    private void intiVariable() {
        pList = new ArrayList<>();
        searchList = new ArrayList<>();
        mAdapter = new AdapterBoard(searchList);

        mBinding.reSearchSearch.setAdapter(mAdapter);
        mBinding.reSearchSearch.setLayoutManager(new LinearLayoutManager(this));
    }

    private void onViewClick() {
        mBinding.imSearchEditBoard.setOnClickListener(v -> {
            searchPost();
        });

        mBinding.imBackMainBoard.setOnClickListener(v -> {
            finish();
        });
    }

    private void searchPost() {
        String keyword = mBinding.edSearchPost.getText().toString();
        Log.i(TAG, "searchPost : keyword = " + keyword);
        searchList = new ArrayList<>();

        pList.forEach(post -> {
            boolean res = post.getTitle().contains(keyword);
            Log.i(TAG, "searchPost: post.getTitle = " + post.getTitle());

            if (res) {
                searchList.add(post);
            }
        });

        if (searchList.isEmpty()) {
            // 검색 결과가 존재하지 않을때
            Log.i(TAG, "searchPost: result is not exits");
            mBinding.tvSearchContent.setText("\'" + keyword + "\'");
            mBinding.layoutSearchNotExits.setVisibility(View.VISIBLE);
            mBinding.tvSearchResult.setVisibility(View.GONE);
        } else {
            // 검색 결과가 존재할떄
            Log.i(TAG, "searchPost: result is exits size = "+searchList.size());
            mBinding.tvSearchResult.setText("\'"+keyword+"\'"+" 검색 결과" );
            mBinding.tvSearchResult.setVisibility(View.VISIBLE);
            mAdapter.resetPostList(searchList);
            mBinding.layoutSearchNotExits.setVisibility(View.GONE);
        }
    }
}


















































