package com.example.soundclounddemo.utils;

import android.text.format.DateUtils;

import com.example.soundclounddemo.network.singleton.RetrofitSingleton;
import com.google.android.gms.common.util.DataUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TimeHandle {
    private static TimeHandle instance;

    private TimeHandle() {
    }

    public static TimeHandle getInstance() {
        if (instance == null) {
            synchronized (TimeHandle.class) {
                if (null == instance) {
                    instance = new TimeHandle();
                }
            }
        }
        return instance;
    }

    public String getCurrentTime() {
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime.toString();
    }

    public String getTime() {
        return (new SimpleDateFormat("EEE, d MMM yyyy \nHH:mm:ss Z"))
                .format(Calendar.getInstance().getTime())
                .toUpperCase();
    }


    public String getCurrentMiliseconds() {
        return String.valueOf(System.currentTimeMillis());
    }

    public String getDisplayTime(Date createdAt) {
        return DateUtils.isToday(createdAt.getTime())
                ? (new SimpleDateFormat("HH:mm")).format(createdAt).toUpperCase()
                : (new SimpleDateFormat("EEE, d MMM yyyy HH:mm")).format(createdAt).toUpperCase();
    }

}
