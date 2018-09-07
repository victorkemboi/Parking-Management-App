package com.sergentcourier.sendit.network;

import android.content.Context;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sergentcourier.sendit.models.ExcludeSerialize;
import com.sergentcourier.sendit.models.Settings;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *  API proxy, sets up the API implementations.
 */
public class APIService {


    private Retrofit retrofit;
    private Settings settings;
    public APIService(Context context){
        settings = new Settings(context);
        Init(null);
    }

    public APIService(Context context,String url){
        settings = new Settings(context);
        Init(url);
    }

    /**
     * Initiate and setup
    */
    private void Init(String url){
        String base_url = settings.getServerURL();
        if(url!=null){
            base_url = url;
        }
        final String token = settings.getUser().token;
        /*
          Setup Logging interceptor.
         */
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //Intercept request and add auth-Key header..
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Authorization",String.format("Token %s",token))
                        .build();
                return chain.proceed(request);
            }
        }).addInterceptor(interceptor)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60,TimeUnit.SECONDS)
                .build();


        //Setup exclusion Strategy to skip serialization of the specific fields.
        Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new Exclude()).create();
        retrofit  = new Retrofit.Builder()
                .baseUrl(base_url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }


    public CommonAPI GetCommonAPI(){
        return retrofit.create(CommonAPI.class);
    }


   public static class Exclude implements ExclusionStrategy {

        public Exclude() {
            super();
        }

        @Override
        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }

        /**
         * Criteria for Skipping a field.
         * @param field The field to be evaluated for skipping.
         * @return True/False - Whether the field should be skipped or not.
         */
        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            //Check the field if it has annotation of type @ExcludeSerialize
            ExcludeSerialize sn = field.getAnnotation(ExcludeSerialize.class);
            return sn != null;
        }}

}
