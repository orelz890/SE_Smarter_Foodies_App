package com.example.smarter_foodies.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {

    @POST("SubmitRecipeToUser")
    Call<String> SubmitRecipeToUser(
            @Query("uid") String uid,String recipe
    );

    @FormUrlEncoded
    @POST("SubmitRecipeToUser2")
    Call<String> SubmitRecipeToUser2(
            @Field("uid") String uid,
            @Field("recipe") String recipe
    );

}

