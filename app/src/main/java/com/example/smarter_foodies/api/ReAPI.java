package com.example.smarter_foodies.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReAPI {
    private static final String BASE_URL = "http://127.0.0.1:3000";
    private static ReAPI mInstance;
    private Retrofit retrofit;

    private ReAPI(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized ReAPI getInstance(){
        if(mInstance == null){
            mInstance = new ReAPI();
        }
        return mInstance;
    }

    public API getAPI() {
        return retrofit.create(API.class);
    }
}
