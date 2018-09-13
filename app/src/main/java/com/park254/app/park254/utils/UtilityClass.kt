package com.park254.app.park254.utils

import android.app.Activity
import android.content.Context
import android.support.v4.content.ContextCompat.getSystemService
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

object UtilityClass {
    val  MAP_BUTTON_REQUEST_CODE  = 301
    val  OPEN_DOCUMENT_FIRST_PHOTO_CODE = 302
    val  OPEN_DOCUMENT_SECOND_PHOTO_CODE = 303
    val  OPEN_DOCUMENT_THIRD_PHOTO_CODE = 304
    val X_FIREBASE_ID_TOKEN = "YOUR-CUSTOM-HEADER"
    const val BASE_URL: String = "https://park254.azurewebsites.net/v1/"
    const val DATABASE_NAME = "park254DB"
    fun requestFocus(view: View, window: Window) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}