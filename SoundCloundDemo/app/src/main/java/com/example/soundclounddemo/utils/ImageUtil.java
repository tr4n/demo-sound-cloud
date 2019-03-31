package com.example.soundclounddemo.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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

public class ImageUtil {


    private IChatViewListener callback;
    public final static String convertBitmaptoBase64(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }



    public final static String convertBase64toBitmap(String base64) {
        return null;
    }

    public final static long getSizefromUri(Context context, Uri uri) {
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
                return (new File(path)).length()/(1024);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static Pair<Integer, Integer> getSizeImage(final ImageView imageView){
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

    public  static Bitmap getResizedBitmap(Bitmap bitmap, long size) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Log.d(TAG, "getResizedBitmap: " + "width: " + width + ", height: " + height);
        int scale = (int)Math.sqrt(size>>5);
        return Bitmap.createScaledBitmap(bitmap, width/scale, height/scale, true);

    }
    public static Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }



}
