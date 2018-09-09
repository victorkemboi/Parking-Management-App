package com.park254.app.park254.network.network;



import com.park254.app.park254.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CommonAPI {

    @POST("/")
    Call<String> DirectCall();

    @POST("profiles")
    Call<User> Login();

    /*@PATCH("profiles")
    Call<> */



}
