package com.app.pcestimate.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageUtils {

    // url로 이미지 가져오기
    public static void loadImageWithUrl(Context context, String url, ImageView imageView){

        Glide.with(context)
                .load(url)
                .into(imageView);
    }
}
