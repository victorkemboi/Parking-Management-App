package com.park254.app.park254.network

import android.arch.lifecycle.LiveData
import com.park254.app.park254.models.*
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import retrofit2.http.*

interface RetrofitApiService{
    /**
     *
     */
    @POST("Profiles")
    fun registerUser():  LiveData<ApiResponse<User>>

    @PATCH("Profiles")
    fun updateUser(user:User): LiveData<ApiResponse<User>>

    @POST("Registration/requestLot")
    fun registerParkingLot(@Body lot: Lot): LiveData<ApiResponse<Lot>>

    @GET("Registration/owned")
    fun getOwnedParkingLots() :  LiveData<ApiResponse<List<LotResponse>>>

    @GET("Registration")
    fun getParkingLots():  LiveData<ApiResponse<List<LotResponse>>>

    @GET("Profiles/email")
    fun getUserByEmail(@Query("q")  email:String ) :  LiveData<ApiResponse<User>>

    @POST("Management")
    fun registerEmployee(@Body employee: Employee): LiveData<ApiResponse<Employee>>

    @POST("Parking/book")
    fun bookParkingLot(@Body bookRequest: BookRequest): LiveData<ApiResponse<Booking>>

    @POST("Parking/estimate")
    fun getBookingEstimation(@Body estimateRequest: EstimateRequest): LiveData<ApiResponse<Double>>

}