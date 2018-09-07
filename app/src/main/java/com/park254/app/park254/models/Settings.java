package com.park254.app.park254.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Settings entity.
 */
public class Settings {

    SharedPreferences settings;

    public Settings(Context context) {
        settings = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE);
    }

    public String getServerURL() {
        return settings.getString("url", "http://http://park254.azurewebsites.net/swagger/");
    }

    public void setServerURL(String url) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("url", url);
        editor.apply();
    }



    public String getIMEI() {
        return settings.getString("device_imei", "");
    }

    public void setIMEI(String pin) {

        SharedPreferences.Editor editor = settings.edit();
        editor.putString("device_imei", pin);
        editor.apply();

    }






    /**
     * Method to clear data in shared preference using a preference key.
     *
     * @param prefsKey {@link String} Preference Key
     */
    public void clearSharedPreferenceData(String prefsKey) {
        settings.edit().remove(prefsKey).apply();
    }


    public void setAPIVersion(APIVersion APIVersion) {
        String apiversion_json = new Gson().toJson(APIVersion);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("api_version", apiversion_json);
        editor.apply();
    }

    public APIVersion getAPIVersion(){
        String json_data  = settings.getString("api_version", "{}");
        return new Gson().fromJson(json_data,APIVersion.class);
    }
}