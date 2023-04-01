package com.app.pcestimate.view.board;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.pcestimate.R;
import com.app.pcestimate.databinding.ActivityDetailPostBinding;
import com.app.pcestimate.datamodel.PostDataModel;
import com.app.pcestimate.datamodel.Replies;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL;

public class ActivityDetailPost extends AppCompatActivity {
    private ActivityDetailPostBinding mBinding;
    private static final String TAG = "##H";
    private PostDataModel postInfo;
    private ArrayList<Replies> replyList;
    private AdapterReplay mAdapter;
    private Dialog dlg;

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
            mBinding.tvRepliesCountDetailPost.setText(postInfo.getReplies().size() + "");
            if (postInfo.getPictures().size() == 0) return;

            if (postInfo.getPictures().size() == 2) {
                Uri uri = Uri.parse(postInfo.getPictures().get(1));
                Glide.with(this).load(uri).into(mBinding.imTwoDetailPost);
                mBinding.imTwoDetailPost.setVisibility(View.VISIBLE);
            }
            Glide.with(this).load(postInfo.getPictures().get(0)).into(mBinding.imOneDetailPost);
            mBinding.imOneDetailPost.setVisibility(View.VISIBLE);
        }

        setReplyData();
    }

    private void setReplyData() {
        mBinding.reRepliesDetail.setAdapter(mAdapter);
        mBinding.reRepliesDetail.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.updateReplyList(replyList);
    }

    private void onViewClick() {
        mAdapter.onItemClickListener(new AdapterReplay.OnItemClick() {
            @Override
            public void clickDelete(String reply, int position) {
                // TODO: 2023/04/01 각 댓글 비밀번호 적용으로 인하여 댓글 삭제부분 코드 수정 필요
//                Dialog dlg = new Dialog(ActivityDetailPost.this, R.style.theme_dialog);
//                dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dlg.setCanceledOnTouchOutside(false);
//                dlg.setCancelable(false);
//                dlg.setContentView(R.layout.dialog_check_password);
//                dlg.show();
//
//                //상단에 취소키를 눌렀을때 다이얼로그창 종료
//                dlg.findViewById(R.id.im_cancel_dialog).setOnClickListener(v -> {
//                    dlg.dismiss();
//                });
//
//                //댓글 삭제버튼 클릭시
//                dlg.findViewById(R.id.bt_ok_dialog).setOnClickListener(v -> {
//                    String password = postInfo.getPassword();
//                    String inputPassword = ((EditText) dlg.findViewById(R.id.ed_password_dialog)).getText().toString();
//
//                    if (inputPassword.equals(password)) {
//                        replyList.remove(position);
//                        mAdapter.resetReplyList(replyList);
//
//                        postInfo.setReplies(replyList);
//                        PresenterPost.getInstance().deleteReply(postInfo);
//                        dlg.dismiss();
//                        mBinding.tvRepliesCountDetailPost.setText(replyList.size() + "");
//                        Toast.makeText(ActivityDetailPost.this, "댓글이 삭제되었습니다", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(ActivityDetailPost.this, "비밀번호가 틀립니다", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });
        mBinding.imBackDetailPost.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityMainBoard.class));
            finish();
        });

        mBinding.imSendDetail.setOnClickListener(v -> {
            // TODO: 2023/04/01 작성 버튼 클릭시 팝업생성 후 데이터 저장
            managePasswordDialog();
            String reply = mBinding.edReplyDetail.getText().toString();

            //상단에 취소키를 눌렀을때 다이얼로그창 종료

            dlg.findViewById(R.id.bt_ok_dialog).setOnClickListener(t -> {
                String password = postInfo.getPassword();
                String inputPassword = ((EditText) dlg.findViewById(R.id.ed_password_dialog)).getText().toString();
                Log.i("##INFO", "onViewClick(): replay = "+reply);
                Replies re = new Replies(reply, Integer.parseInt(inputPassword));
                replyList.add(re);
                postInfo.setReplies(replyList);
                PresenterPost.getInstance().setReply(postInfo);
                dlg.dismiss();
            });

            mAdapter.updateReplyList(replyList);
            mBinding.edReplyDetail.setText("");
            mBinding.tvRepliesCountDetailPost.setText(replyList.size() + "");

            //댓글 입력시 자동으로 키보드 내림
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        //삭제버튼 클릭시 게시글 삭제
        mBinding.tvDeleteContent.setOnClickListener(v -> {
            managePasswordDialog();

            //상단에 취소키를 눌렀을때 다이얼로그창 종료
            dlg.findViewById(R.id.im_cancel_dialog).setOnClickListener(t -> {
                dlg.dismiss();
            });

            dlg.findViewById(R.id.bt_ok_dialog).setOnClickListener(t -> {
                String password = postInfo.getPassword();
                String inputPassword = ((EditText) dlg.findViewById(R.id.ed_password_dialog)).getText().toString();

                if (inputPassword.equals(password)) {
                    PresenterPost.getInstance().deletePost(postInfo);
                    dlg.dismiss();
                    Toast.makeText(ActivityDetailPost.this, "게시글이 삭제되었습니다", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(this, ActivityMainBoard.class));
                    finish();
                } else {
                    Toast.makeText(ActivityDetailPost.this, "비밀번호가 틀립니다", Toast.LENGTH_SHORT).show();
                }
            });
        });

        //수정하기 버튼 클릭시 실행 로직
        mBinding.tvModifyContent.setOnClickListener(v -> {
            managePasswordDialog();

            //상단에 취소키를 눌렀을때 다이얼로그창 종료
            dlg.findViewById(R.id.im_cancel_dialog).setOnClickListener(t -> {
                dlg.dismiss();
            });

            dlg.findViewById(R.id.bt_ok_dialog).setOnClickListener(t -> {
                String password = postInfo.getPassword();
                String inputPassword = ((EditText) dlg.findViewById(R.id.ed_password_dialog)).getText().toString();

                if (inputPassword.equals(password)) {
                    dlg.dismiss();
                    Intent i = new Intent(ActivityDetailPost.this, ActivityWritePost.class);
                    i.putExtra("postInfo", postInfo);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(ActivityDetailPost.this, "비밀번호가 틀립니다", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void managePasswordDialog() {
        dlg = new Dialog(ActivityDetailPost.this, R.style.theme_dialog);

        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setCanceledOnTouchOutside(false);
        dlg.setCancelable(false);
        dlg.setContentView(R.layout.dialog_check_password);
        dlg.show();


        dlg.findViewById(R.id.im_cancel_dialog).setOnClickListener(t -> {
            dlg.dismiss();
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ActivityMainBoard.class));
        finish();
        super.onBackPressed();
    }
}































