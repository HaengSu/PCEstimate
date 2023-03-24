package com.app.pcestimate.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static File uriToFile(Context context, Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        String filePath = "";
        File file = null;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            file = new File(filePath);
        }

        return file;
    }

    public static Bitmap uriToBitmap(Context context, Uri uri) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.getApplicationContext().getContentResolver(), uri));
            } else {
                // fixme
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                // 이미지 회전 -> 접근권한 필요함
                bitmap = modifyOrientation(bitmap, getFileName(uriToFile(context, uri)));
                return bitmap ;
            }
        } catch (Exception e) {
            Log.e("#####", e.getLocalizedMessage());
        }
        return null;
    }

    public static void saveImage(Context context, Bitmap bitmap, String fileName) {

        File file = new File(getFilePath(context), fileName);

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            switch (file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1)) {
                case "jpeg":
                case "jpg":
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    break;
                case "png":
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    break;
            }
            bitmap.recycle();
            fileOutputStream.close();

        } catch (Exception e) {
            Log.d("#####", "파일 저장 실패.. ");
            Log.e("#######", e.getLocalizedMessage());
        }

    }

    private static String getFilePath(Context context) {
        String filePath = context.getFilesDir().getPath();
        return filePath;
    }

    //원본 이미지의 회전 상태를 새로운 bitmap에 반영
    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    //이미지 회전
    private static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    //이미지 뒤집기
    private static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    // 파일 경로에서 파일이름 반환=
    public static String getFileName(File file) {
        String result = "";

        String filePath = file.getAbsolutePath();
        result = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));

        return result;
    }
}