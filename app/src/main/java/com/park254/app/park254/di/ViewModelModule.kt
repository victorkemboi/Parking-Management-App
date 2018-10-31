package com.park254.app.park254.di

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.park254.app.park254.App
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.repo.*
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.Job
import javax.inject.Singleton

@Module
class ViewModelModule {
    @Singleton
    @Provides
    internal fun providesLoginViewModel(firebaseAuth:FirebaseAuth): LoginViewModel =
            LoginViewModel(firebaseAuth)

    @Singleton
    @Provides
    internal fun providesParkingLotRegistrationViewModel(): ParkingLotRegistrationViewModel =
            ParkingLotRegistrationViewModel()

    @Singleton
    @Provides
    internal fun providesHomeViewModel(job: Job, retrofitApiService: RetrofitApiService, threadPool : ExecutorCoroutineDispatcher): HomeViewModel =
            HomeViewModel(retrofitApiService,job,threadPool)

    @Singleton
    @Provides
    internal fun providesHomeMapViewModel(job: Job, retrofitApiService: RetrofitApiService, threadPool : ExecutorCoroutineDispatcher): HomeMapViewModel =
            HomeMapViewModel(retrofitApiService,job,threadPool)

    @Singleton
    @Provides
    internal fun providesEmployeeViewModel(): EmployeeViewModel =
            EmployeeViewModel()

    @Singleton
    @Provides
    internal fun providesLotInfoViewModel(): LotInfoViewModel =
            LotInfoViewModel()

    @Singleton
    @Provides
    internal fun providesParkingViewModel(): ParkingLotViewModel =
            ParkingLotViewModel()

    @Singleton
    @Provides
    internal fun providesPaymentsViewModel(job: Job, retrofitApiService: RetrofitApiService, threadPool : ExecutorCoroutineDispatcher): PaymentsViewModel =
            PaymentsViewModel(retrofitApiService,job,threadPool)

    @Singleton
    @Provides
    internal fun providesPaymentVerificationViewModel(job: Job, retrofitApiService: RetrofitApiService, threadPool : ExecutorCoroutineDispatcher): PaymentVerificationViewModel =
            PaymentVerificationViewModel(retrofitApiService,job,threadPool)

}