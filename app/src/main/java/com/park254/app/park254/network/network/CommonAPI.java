package com.sergentcourier.sendit.network;


import com.google.gson.JsonElement;
import com.sergentcourier.sendit.models.APIVersion;
import com.sergentcourier.sendit.models.Agent;
import com.sergentcourier.sendit.models.AgentAccount;
import com.sergentcourier.sendit.models.DeliveryType;
import com.sergentcourier.sendit.models.Package;
import com.sergentcourier.sendit.models.User;
import com.sergentcourier.sendit.models.VehicleLocation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CommonAPI {

    @POST("/")
    Call<String> DirectCall();

    @POST("login")
    Call<User> Login(@Body User user);

    @FormUrlEncoded
    @POST("track")
    Call<Package> TrackPackage(@Field("package_id") String package_id);


    @POST("register")
    Call<Agent> Register(@Body Agent data);


    @POST("request")
    Call<Package> PostRequest(@Body Package data);


    @POST("update_location")
    Call<VehicleLocation> UpdateLocation(@Body VehicleLocation data);

    @GET("check_update")
    Call<APIVersion> CheckVersion();

    @FormUrlEncoded
    @POST("agent_cancel")
    Call<Package> CancelRequest(@Field("agent_id") String agent_id,@Field("package_id") String package_id);


    @GET("type_list")
    Call<List<DeliveryType>> GetDeliveryTypes();


    @GET("packages")
    Call<List<Package>> GetPackages(@Query("agent_id") String agent_id);

    @GET("vehicle_deliveries")
    Call<List<Package>> GetDeliveries(@Query("vehicle_id") String vehicle_id);

    @GET("offers")
    Call<List<Package>> GetOffers();

    @GET("transactions")
    Call<List<AgentAccount>> GetTransactions(@Query("agent_id") String agent_id);

    @FormUrlEncoded
    @POST("accept")
    Call<Package> AcceptRequest(@Field("vehicle_id") String vehicle_id ,
                                @Field("package_id") String package_id);

    @FormUrlEncoded
    @POST("cancel")
    Call<Package> CancelPickup(@Field("vehicle_id") String vehicle_id ,
                               @Field("package_id") String package_id);

    @FormUrlEncoded
    @POST("pickup")
    Call<Package> PackagePickup(@Field("vehicle_id") String vehicle_id ,
                                @Field("package_id") String package_id);

    @FormUrlEncoded
    @POST("deliver")
    Call<Package> DeliverPackage(@Field("vehicle_id") String vehicle_id ,
                                 @Field("package_id") String package_id);

    @FormUrlEncoded
    @POST("return")
    Call<Package> ReturnPackage(@Field("vehicle_id") String vehicle_id ,
                                 @Field("package_id") String package_id);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<User> ForgotPassword(@Field("username") String phone_number );

    @FormUrlEncoded
    @POST("change_password")
    Call<User> ResetPassword(@Field("user_name") String user_name,
                             @Field("password") String password
                            );

}
