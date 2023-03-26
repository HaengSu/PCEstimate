package com.app.pcestimate.view.board;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.pcestimate.R;
import com.app.pcestimate.databinding.ActivityWritePostBinding;
import com.app.pcestimate.datamodel.PostDataModel;
import com.app.pcestimate.util.FileUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

// TODO: 2023/03/23 => 사진 넣기 기능 추가 및 파이어베이스 스토리지로 전송하여 url로 변경

public class ActivityWritePost<ActivityBoardWriter> extends AppCompatActivity {
    private ActivityWritePostBinding mBinding;
    private static final String TAG = "##H";
    private String postId = "";
    private ArrayList<String> replies = new ArrayList<>();
    private ArrayList<String> imageList = new ArrayList<>();
    private ArrayList<Bitmap> bitmapList = new ArrayList<>();
    private int maxSize = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_write_post);

        initVariable();
        getPostItem();
        onViewClick();

        setEvent();
    }

    //region ---- Test Section  ---

    private void setEvent() {

        // 사진 선택

    }

    // 33 이상 버전 콜백
    ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(maxSize), uris -> {
                // photo picker.
                if (!uris.isEmpty()) {
                    for (Uri uri : uris) {

                        Log.d("######", "uri : " + uri.toString());
                        // 대용량 업그레이드 시 권한 길게 유지
                        int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                        getContentResolver().takePersistableUriPermission(uri, flag);
                        Bitmap bitmap = FileUtils.uriToBitmap(ActivityWritePost.this, uri);
                        mBinding.imOneWrite.setImageBitmap(bitmap);
                        bitmapList.add(bitmap);
                    }
                } else {
                    Log.d("#######", "uri 없음 !!");
                }
            });

    // 33 미만 버전 콜백
    ActivityResultLauncher<Intent> mGetImage =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                if (result.getData() != null) {

                    Intent data = result.getData();
                    if (data.getClipData() != null) {

                        ClipData clipData = data.getClipData();
                        if (clipData.getItemCount() > 2) {
                            Toast.makeText(ActivityWritePost.this, "이미지는 최대 2장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                Uri uri = clipData.getItemAt(i).getUri();

                                Bitmap bitmap = FileUtils.uriToBitmap(ActivityWritePost.this, uri);
                                bitmapList.add(bitmap);
                                if (mBinding.imOneWrite.getDrawable() != null) {
                                    mBinding.imTwoWrite.setImageBitmap(bitmap);
                                } else {
                                    mBinding.imOneWrite.setImageBitmap(bitmap);
                                }
                            }
                        }

                    }
//                    else {
//                        // 이미지 1개만 선택
//                        Uri uri = data.getData();
//                        Log.d("#####","uri : " + uri);
//
//                        File file = FileUtils.uriToFile(ActivityBoardWriter.this, uri);
//
//                    }
                }
            });

    private boolean isPhotoPickerAvailable() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU;
    }

    public void launchPhotoPicker() {
        if (isPhotoPickerAvailable()) {

            // fixme - 실행 자체는 문제가 없으나 컴파일 전 에러가 뜨는듯..
            pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        } else {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_PICK);
            mGetImage.launch(intent);
        }
    }
    //endregion


    private void initVariable() {
    }

    private void getPostItem() {
        PostDataModel getPostData = (PostDataModel) getIntent().getSerializableExtra("postInfo");

        // 넘어온 데이터가 있을 경우
        if (getPostData != null) {
            mBinding.edTitleWrite.setText(getPostData.getTitle());
            mBinding.edContentWrite.setText(getPostData.getContent());
            mBinding.edPasswordWrite.setText(getPostData.getPassword());
            postId = getPostData.getId();
            replies = getPostData.getReplies();
        }
    }

    private void onViewClick() {
        mBinding.btCreateWrite.setOnClickListener(v -> {
            //user 입력란에 공백이 있는지에 대한 확인
            String title = mBinding.edTitleWrite.getText().toString();
            String content = mBinding.edContentWrite.getText().toString();
            String password = mBinding.edPasswordWrite.getText().toString();
            if (title.isEmpty() && content.isEmpty() && password.isEmpty()) {
                Toast.makeText(this, "빈 부분이 있습니다", Toast.LENGTH_SHORT).show();
            } else {
                bitmapList.forEach(image -> {
                    getImageUri(image);
                });
            }
        });


        mBinding.imBackWrite.setOnClickListener(v -> {
            finish();
        });

        mBinding.ibtGetPhotoWrite.setOnClickListener(v -> {
            if (mBinding.imOneWrite.getDrawable() != null && mBinding.imTwoWrite.getDrawable() != null) {
                Toast.makeText(ActivityWritePost.this, "이미지는 최대 2장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show();
            } else {
                launchPhotoPicker();
            }

        });

        mBinding.imOneCancelWrite.setOnClickListener(v -> {
            mBinding.imOneWrite.setImageResource(0);
        });

        mBinding.imTwoCancelWrite.setOnClickListener(v -> {
            mBinding.imTwoWrite.setImageResource(0);
        });
    }

    private void getImageUri(Bitmap imageBitmap) {
        Log.i("##INFO", "getImageUri(): bitmap = " + imageBitmap);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        int randomNum = (int) (Math.random() * 100000);
        StorageReference mountainsRef = storageRef.child(mBinding.edTitleWrite.getText() + String.valueOf(randomNum) + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();


        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("##INFO", "onFailure(): exception = " + exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.i("##INFO", "onSuccess(): getImageUri");
                        imageList.add(uri.toString());
                        if (imageList.size() == bitmapList.size()) {
                            Log.i("##INFO", "onSuccess(): size is same");
                            addPost();
                        }
                    }
                });
            }
        });
    }

    private void addPost() {
        String title = mBinding.edTitleWrite.getText().toString();
        String content = mBinding.edContentWrite.getText().toString();
        String password = mBinding.edPasswordWrite.getText().toString();

        Boolean res = PresenterPost.getInstance().setPost(new PostDataModel(title, content, password, replies, imageList), postId);
        if (res) {
            startActivity(new Intent(this, ActivityMainBoard.class));
            finish();
        } else {
            Toast.makeText(this, "게시글 작성에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}





























