package com.park254.app.park254.network

import android.arch.lifecycle.LiveData
import com.park254.app.park254.models.Lot
import com.park254.app.park254.models.User
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import retrofit2.http.PATCH
import retrofit2.http.POST

interface RetrofitApiService{
    /**
     * Get the list of the pots from the API
     */
    @POST("Profiles")
    fun registerUser():  LiveData<ApiResponse<User>>

    @PATCH("Profiles")
    fun updateUser(user:User): LiveData<ApiResponse<User>>

}