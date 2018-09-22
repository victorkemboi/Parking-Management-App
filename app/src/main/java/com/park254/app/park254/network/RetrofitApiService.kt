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

    @POST("Registration/lot")
    fun registerParkingLot(@Body lot: Lot): LiveData<ApiResponse<LotResponse>>

    @GET("Registration/owned")
    fun getOwnedParkingLots() :  LiveData<ApiResponse<List<LotResponse>>>

    @GET("Registration")
    fun getParkingLots():  LiveData<ApiResponse<List<LotResponse>>>

    @GET("Parking/bookings")
    fun getUserBookings():  LiveData<ApiResponse<List<Booking>>>

    @GET("Payments")
    fun getUserPayments():  LiveData<ApiResponse<List<Payment>>>

    @GET("Registration/lot")
    fun getParkingLotById(@Query("id")  id:String ) :  LiveData<ApiResponse<LotResponse>>

    @GET("Profiles/email")
    fun getUserByEmail(@Query("q")  email:String ) :  LiveData<ApiResponse<User>>

    @POST("Management")
    fun registerEmployee(@Body baseEmployeeRegistration: BaseEmployeeRegistration): LiveData<ApiResponse<Employee>>

    @POST("Parking/vacancy")
    fun updateAvailableSpaces(@Body availableSpaceUpdate: AvailableSpaceUpdate): LiveData<ApiResponse<AvailableSpaceResponse>>

    @POST("Parking/book")
    fun bookParkingLot(@Body bookRequest: BookRequest): LiveData<ApiResponse<Booking>>

    @POST("Parking/estimate")
    fun getBookingEstimation(@Body estimateRequest: EstimateRequest): LiveData<ApiResponse<Double>>

    @GET("Parking/{id}/free")
    fun getAvailableSpacesInaParkingLot(@Path("id") id:String) :LiveData<ApiResponse<AvailableSpaceResponse>>

    @GET("Management/{employeeId}")
    fun getEmployeeByUserId(@Path("employeeId") id:String) :LiveData<ApiResponse<Employee>>

    @GET("Management")
    fun getParkingLotEmployees(@Query("lotId")  id:String ) :  LiveData<ApiResponse<ArrayList<User>>>


    @POST("Profiles/update")
    fun updateUserInfo(@Body userUpdate: UserUpdate): LiveData<ApiResponse<User>>

    @POST("Mpesa")
    fun payForBooking(@Body bookingId: String): LiveData<ApiResponse<Void>>

    @GET("Parking/bookings/{id}")
    fun geBookingById(@Path("id") id:String) :LiveData<ApiResponse<Booking>>







}