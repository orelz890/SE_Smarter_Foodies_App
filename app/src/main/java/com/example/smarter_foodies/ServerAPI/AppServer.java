package com.example.smarter_foodies.ServerAPI;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppServer {
    /**
     * this is our function that uses our app server.
     * check the auth of the user in order to secure that user wont get to admin screens and back
     *
     * @param uid        id of the user
     * @param recipe    the recipe that should be added
     */
    public static void SendToTheServer(String uid, String recipe) {
        Call<String> call = ReAPI.getInstance().getAPI().SubmitRecipeToUser(uid,recipe); // Contact the server with API
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) { // When the server gives response
                if (response.isSuccessful()) {
                    String s = response.body();
                    System.out.println("***********"+ s);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public static void SendToTheServer2(String uid, String recipe) {
        Call<ResponseBody> call = ReAPI.getInstance().getAPI().SubmitRecipeToUser2(uid,recipe); // Contact the server with API
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) { // When the server gives response
                if (response.isSuccessful()) {
                    String s = String.valueOf(response.body());
                    System.out.println("***********"+ s);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}

