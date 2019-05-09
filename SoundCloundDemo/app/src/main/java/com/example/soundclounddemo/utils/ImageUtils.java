package com.example.soundclounddemo.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.soundclounddemo.R;
import com.example.soundclounddemo.view.IChatViewListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import static com.android.volley.VolleyLog.TAG;

public class ImageUtils {


    private IChatViewListener callback;

    public static String convertBitmaptoBase64(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }


    public static String convertBase64toBitmap(String base64) {
        return null;
    }

    public static long getSizefromUri(Context context, Uri uri) {
        String scheme = uri.getScheme();
        //   System.out.println("Scheme type " + scheme);
        if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            try {
                InputStream fileInputStream = context.getContentResolver().openInputStream(uri);
                return fileInputStream.available() / (1024);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (scheme.equals(ContentResolver.SCHEME_FILE)) {
            String path = uri.getPath();
            try {
                return (new File(path)).length() / (1024);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static Pair<Integer, Integer> getSizeImage(final ImageView imageView) {
        final int[] height = new int[1];
        final int[] width = new int[1];
        ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                height[0] = imageView.getMeasuredHeight();
                width[0] = imageView.getMeasuredWidth();
                return true;
            }
        });
        return new Pair<>(width[0], height[0]);
    }

    public static Bitmap getResizedBitmap(Bitmap bitmap, long size) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Log.d(TAG, "getResizedBitmap: " + "width: " + width + ", height: " + height);
        int scale = (int) Math.sqrt(size >> 5);
        return Bitmap.createScaledBitmap(bitmap, width / scale, height / scale, true);

    }

    public static Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPathUri(Context context, Uri uri) {
        String[] data = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    public static boolean checkAvailableUri(Context context, Uri uri){
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String filePath = cursor.getString(0);

                if (new File(filePath).exists()) {
                    // do something if it exists
                    return true;
                } else {
                    // File was not found
                    return false;
                }
            } else {
                // Uri was ok but no entry found.
                return false;
            }
        } else {
            // content Uri was invalid or some other error occurred
            return false;
        }
    }
}
