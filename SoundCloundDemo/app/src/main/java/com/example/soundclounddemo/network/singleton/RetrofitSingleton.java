package com.example.soundclounddemo.network.singleton;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {
    private static Retrofit instance;
    private static final String URL = "http://api.soundcloud.com/";
    public static Retrofit getInstance(){
        if(instance == null){
            synchronized (RetrofitSingleton.class){
                if(null == instance){
                    instance = new Retrofit.Builder()
                            .baseUrl(URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return instance;
    }
}
