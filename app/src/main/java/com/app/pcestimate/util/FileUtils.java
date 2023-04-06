package com.app.pcestimate.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pcestimate.BuildConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    public static Bitmap viewToBitmap(View view) {
        if(view.getMeasuredWidth() <= 0 || view.getMeasuredHeight() <= 0){
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static void shareImage(Context context, Bitmap bitmap){

        String fileName = "estimate_" + System.currentTimeMillis() + ".jpg";
        File file = saveImage(context, bitmap, fileName);

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/jpg");
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file));
        context.startActivity(i);

    }

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

    public static Uri bitmapToUri(Context context, Bitmap bitmap) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "estimate", null);
            return Uri.parse(path);
        } catch (Exception e) {
            Log.e("#####", e.getLocalizedMessage());
        }
        return null;
    }

    public static File saveImage(Context context, Bitmap bitmap, String fileName) {

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

        return file;
    }

    public static void saveImage(Context context, Uri uri, String fileName){

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            values.put(MediaStore.Images.Media.IS_PENDING, 1);
        }

        ContentResolver contentResolver = context.getContentResolver();
        Uri item = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try{

            ParcelFileDescriptor pdf = contentResolver.openFileDescriptor(item, "w", null);
            if(pdf == null){
                Log.e("####", "pdf is null..");
            }else {
                byte[] inputData = getBytes(context, uri);
                FileOutputStream fos = new FileOutputStream(pdf.getFileDescriptor());
                fos.write(inputData);
                fos.close();

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    values.clear();
                    values.put(MediaStore.Images.Media.IS_PENDING, 0);
                    contentResolver.update(item, values, null, null);
                }

                // 갱신
//                galleryAddPic(context, fileName);

            }

        }catch (Exception e){
            e.printStackTrace();
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

    // Uri to ByteArr
    public static byte[] getBytes(Context context, Uri image_uri) throws IOException {
        InputStream iStream = context.getContentResolver().openInputStream(image_uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024; // 버퍼 크기
        byte[] buffer = new byte[bufferSize]; // 버퍼 배열

        int len = 0;
        // InputStream에서 읽어올 게 없을 때까지 바이트 배열에 쓴다.
        while ((len = iStream.read(buffer)) != -1)
            byteBuffer.write(buffer, 0, len);
        return byteBuffer.toByteArray();
    }

    private static void galleryAddPic(Context context, String Image_Path) {

        Log.d("Woongs","갱신 : "+Image_Path);

        // 이전 사용 방식
        /*Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(Image_Path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.context.sendBroadcast(mediaScanIntent);*/

        File file = new File(Image_Path);
        MediaScannerConnection.scanFile(context,
                new String[]{file.toString()},
                null, null);
    }
}