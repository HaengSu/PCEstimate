package com.app.pcestimate.view.board;

import static android.os.ext.SdkExtensions.getExtensionVersion;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;

import com.app.pcestimate.R;
import com.app.pcestimate.databinding.ActivityBoardWriterBinding;
import com.app.pcestimate.util.FileUtils;

import java.io.File;
import java.util.List;

/**
 * 글 작성 화면
 */
public class ActivityBoardWriter extends AppCompatActivity {

    private ActivityBoardWriterBinding mBinding;
    private int maxSize = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_board_writer);

        setEvent();
    }

    private void setEvent() {

        // 사진 선택
        mBinding.imgGallery.setOnClickListener(v -> {
            launchPhotoPicker();
        });

    }

    // 33 이상 버전 콜백
    ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(maxSize), uris -> {
                // photo picker.
                if (!uris.isEmpty()) {
                    for (Uri uri : uris) {

                        Log.d("######","uri : " + uri.toString());
                        // 대용량 업그레이드 시 권한 길게 유지
                        int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                        getContentResolver().takePersistableUriPermission(uri, flag);

                        Bitmap bitmap = FileUtils.uriToBitmap(ActivityBoardWriter.this, uri);
                        mBinding.imageTest.setImageBitmap(bitmap);

                    }
                }else {
                    Log.d("#######","uri 없음 !!");
                }
            });

    // 33 미만 버전 콜백
    ActivityResultLauncher<Intent> mGetImage =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                if(result.getData() != null){

                    Intent data = result.getData();
                    if(data.getClipData() != null){

                        ClipData clipData = data.getClipData();
                        if(clipData.getItemCount() > 2){
                            Toast.makeText(ActivityBoardWriter.this, "이미지는 최대 2장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show();
                        }else {
                            for(int i = 0; i < clipData.getItemCount() ; i++){
                                Uri uri = clipData.getItemAt(i).getUri();

//                                File file = FileUtils.uriToFile(ActivityBoardWriter.this, uri);
                                Bitmap bitmap = FileUtils.uriToBitmap(ActivityBoardWriter.this, uri);

                                mBinding.imageTest.setImageBitmap(bitmap);
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

}