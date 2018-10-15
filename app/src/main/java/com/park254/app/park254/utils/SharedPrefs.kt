package com.park254.app.park254.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.park254.app.park254.models.APIVersion


class SharedPrefs(context: Context) {

    internal var settings: SharedPreferences = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    var serverURL: String?
        get() = settings.getString("url", UtilityClass.BASE_URL)
        set(url) {
            val editor = settings.edit()
            editor.putString("url", url)
            editor.apply()
        }

    var token: String?
        get() = settings.getString("token", "")
        set(value) {

            val editor = settings.edit()
            editor.putString("token", value)
            editor.apply()

        }

    var userId: String?
        get() = settings.getString("user", "")
        set(value) {

            val editor = settings.edit()
            editor.putString("user", value)
            editor.apply()

        }

    var apiVersion: APIVersion
        get() {
            val json_data = settings.getString("api_version", "{}")
            return Gson().fromJson(json_data, APIVersion::class.java)
        }
        set(APIVersion) {
            val apiversion_json = Gson().toJson(APIVersion)
            val editor = settings.edit()
            editor.putString("api_version", apiversion_json)
            editor.apply()
        }


    /**
     * Method to clear data in shared preference using a preference key.
     *
     * @param prefsKey [String] Preference Key
     */
    fun clearSharedPreferenceData(prefsKey: String) {
        settings.edit().remove(prefsKey).apply()
    }
}